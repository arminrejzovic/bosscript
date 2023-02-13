/**
 * Order of precedence
 * --- Parsed First ---
 * 0. Assignment Expression
 * 1. Member Expression
 * 2. Function Call
 * 3. Logical Expression
 * 4. Comparison Expression
 * 5. Additive Expression
 * 6. Multiplicative Expression
 * 7. Call Expression
 * 8. Member Expression
 * 8. Primary Expression
 * --- Parsed Last ---
 */

class Parser {
    private var tokens: ArrayList<Token> = arrayListOf()

    private fun notEOF(): Boolean{
        return current().type != TokenType.EOF
    }

    private fun parseStatement(): Statement{
        when(current().type){
            TokenType.Var -> {
                return parseVariableDeclaration()
            }
            TokenType.Konst -> {
                return parseVariableDeclaration(isConstant = true)
            }
            else -> {
                return parseExpression()
            }
        }
    }

    // var x = nista
    // var x = 10
    // konst pi = 3.14
    // var y <- error
    private fun parseVariableDeclaration(isConstant: Boolean = false): Statement {
        // TODO allow declaring variables without initial value
        // consume var or konst keyword
        consume()
        val identifier = expect(TokenType.Identifier, "Expected identifier").value
        val isAssignment = peekExpect(TokenType.SimpleAssign)
        if(isAssignment){
            return VariableDeclaration(identifier, parseExpression())
        }
        else {
            if(isConstant) throw Exception("Constants must have a value")
            return VariableDeclaration(identifier, null)
        }
    }

    private fun parseAssignmentExpression(): Expression{
        val left = parseObjectExpression()
        if (current().type == TokenType.SimpleAssign){
            // Assignment
            consume()
            val right = parseAssignmentExpression()
            return AssignmentExpression(assignee = left, value = right, assignmentOperator = "")
        }
        return left
    }

    private fun parseExpression(): Expression{
        return parseAssignmentExpression()
    }

    // Left hand precedence -> 10 + 3 - 2 === (10+3) - (2)
    private fun parseAdditiveExpression(): Expression{
        var left = parseMultiplicativeExpression()
        while (current().value == "+" || current().value == "-"){
            val operator = consume().value
            val right = parseMultiplicativeExpression()
            left = BinaryExpression(left, right, operator)
        }
        return left
    }

    /**
     *  Parses multiplication, division and modulo
     */
    private fun parseMultiplicativeExpression(): Expression{
        var left = parseCallMemberExpression()
        while (current().value == "*" || current().value == "/" || current().value == "%"){
            val operator = consume().value
            val right = parseCallMemberExpression()
            left = BinaryExpression(left, right, operator)
        }
        return left
    }

    private fun parseCallMemberExpression(): Expression{
        val member = parseMemberExpression()
        if(current().type == TokenType.OpenParen){
            return parseCallExpression(member)
        }
        return member
    }

    private fun parseMemberExpression(): Expression{
        var obj = parsePrimaryExpression()
        while (current().type == TokenType.Dot || current().type == TokenType.OpenBracket){
            val operator = consume()
            var property: Expression
            var isComputed: Boolean

            if(operator.type == TokenType.Dot){
                // objekat x = {...}
                // x.ime

                isComputed = false
                property = parsePrimaryExpression()

                if(property.kind != NodeType.Identifier){
                    throw Exception("RHS is not a property. Expected an Identifier")
                }
            }

            else{
                // x["ime"], x[kljuc], x[dobaviKljuc()], etc.
                isComputed = true
                property = parseExpression()
                expect(TokenType.CloseBracket, "Expected ].")
            }

            obj = MemberExpression(targetObject = obj, property = property, isComputed = isComputed)

        }
        return obj
    }

    private fun parseCallExpression(caller: Expression): Expression{
        var callExpression = CallExpression(
            args=parseArgs(),
            callee=caller
        )
        if(current().type == TokenType.OpenParen){
            callExpression = parseCallExpression(callExpression) as CallExpression
        }

        return callExpression
    }

    private fun parseArgs(): ArrayList<Expression>{
        expect(TokenType.OpenParen, "Expected open parentheses")
        val args = if (current().type == TokenType.CloseParen) arrayListOf() else parseArgumentsList()

        expect(TokenType.CloseParen, "Missing closing parenthesis")
        return args
    }

    private fun parseArgumentsList(): ArrayList<Expression>{
        val args = arrayListOf(parseAssignmentExpression())

        while (current().type == TokenType.Comma && consume() != null){
            args.add(parseAssignmentExpression())
        }

        return args
    }


    private fun parsePrimaryExpression(): Expression{
        when(current().type){
            TokenType.Identifier -> {
                return Identifier(consume().value)
            }
            TokenType.Number -> {
                return NumericLiteral(consume().value.toDouble())
            }
            TokenType.DoubleQuote -> {
                consume()
                val str = expect(TokenType.String, "String literal expected")
                expect(TokenType.DoubleQuote, "Expected \"\"")
                return StringLiteral("${str.value}")
            }
            TokenType.OpenParen -> {
                consume()
                val value = parseExpression()
                expect(TokenType.CloseParen, "Missing closing parenthesis")
                return value
            }
            else -> {
                throw Exception("Unexpected token found: [${current()}]")
            }
        }
    }

    private fun parseObjectExpression(): Expression{
        if (current().type != TokenType.OpenBrace){
            return parseAdditiveExpression()
        }
        consume() // consume the open brace

        val properties = arrayListOf<Property>()

        while (notEOF() && current().type != TokenType.CloseBrace){
            val key = expect(TokenType.Identifier, "Object key expected").value

            expect(TokenType.Colon, "Missing :")
            val value = parseExpression()

            properties.add(Property(key, value))

            if(current().type != TokenType.CloseBrace){
                expect(TokenType.Comma, "Expected , or }")
            }
        }
        expect(TokenType.CloseBrace, "Expected }")

        return ObjectLiteral(properties)
    }

    private fun current(): Token{
        return tokens[0]
    }

    private fun consume(): Token {
        return tokens.removeAt(0)
    }

    private fun expect(expectedType: TokenType, errorMessage: String): Token{
        val prev = tokens.removeAt(0)
        if(prev.type != expectedType){
            throw Exception(errorMessage)
        }
        return prev
    }

    private fun peekExpect(expectedType: TokenType): Boolean{
        if(current().type == expectedType){
            consume()
            return true
        }
        return false
    }

    fun generateAST(src: String): Program{
        tokens = tokenize(src)
        val program = Program(arrayListOf())

        while (notEOF()){
            program.body.add(parseStatement())
        }

        return program
    }
}