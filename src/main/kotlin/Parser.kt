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
 * 7. Unary Expression
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
            else ->{
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
        val isAssignment = peekExpect(TokenType.Equals)
        if(isAssignment){
            return VariableDeclaration(isConstant, identifier, parseExpression())
        }
        else {
            if(isConstant) throw Exception("Constants must have a value")
            return VariableDeclaration(isConstant = false, identifier, null)
        }
    }

    private fun parseAssignmentExpression(): Expression{
        val left = parseObjectExpression()
        if (current().type == TokenType.Equals){
            // Assignment
            consume()
            val right = parseAssignmentExpression()
            return AssignmentExpression(assignee = left, value = right)
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
        var left = parsePrimaryExpression()
        while (current().value == "*" || current().value == "/" || current().value == "%"){
            val operator = consume().value
            val right = parsePrimaryExpression()
            left = BinaryExpression(left, right, operator)
        }
        return left
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
            TokenType.CloseParen -> TODO()
            TokenType.Equals -> TODO()
            TokenType.Var -> TODO()
            TokenType.BinaryOperator -> TODO()
            TokenType.EOF -> TODO()
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