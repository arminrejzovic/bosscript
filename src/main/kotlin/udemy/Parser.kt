package udemy

import ArrayLiteral
import AssignmentExpression
import BinaryExpression
import BlockStatement
import BooleanLiteral
import BreakStatement
import CallExpression
import DoWhileStatement
import EmptyStatement
import Expression
import ForStatement
import FunctionDeclaration
import FunctionExpression
import FunctionParameter
import Identifier
import IfStatement
import LogicalExpression
import MemberExpression
import ModelDefinitionStatement
import ModelProperty
import NodeType
import NullLiteral
import NumericLiteral
import ObjectLiteral
import Program
import Property
import ReturnStatement
import Statement
import StringLiteral
import Token
import TokenType
import TypeAnnotation
import UnaryExpression
import UnlessStatement
import VariableDeclaration
import VariableStatement
import WhileStatement
import tokenize
import java.util.StringJoiner


class Parser {
    private var tokens: ArrayList<Token> = arrayListOf()

    // -----------------------------------------------------------------------------------------------------------------

    // Utility methods

    private fun notEOF(): Boolean{
        return current().type != TokenType.EOF
    }

    private fun current(): Token{
        return tokens[0]
    }

    private fun consume(): Token {
        return tokens.removeAt(0)
    }

    private fun getLineCol(): String{
        return current().getLineCol()
    }

    private fun checkValidAssignmentTarget(node: Expression): Expression{
        if (node.kind == NodeType.Identifier || node.kind == NodeType.MemberExpression){
            return node
        }
        throw Exception("Invalid assignment target")
    }

    /**
     * Removes the first available token and throws Exception if it doesn't match the expected type.
     */
    private fun expect(expectedType: TokenType, errorMessage: String): Token{
        val prev = tokens.removeAt(0)
        if(prev.type != expectedType){
            println("Expected ${expectedType.name}, got ${prev.type}")
            throw Exception(errorMessage)
        }
        return prev
    }

    private fun warning(message: String){
        val reset = "\u001b[0m"
        val yellow = "\u001b[33m"

        println(yellow + "[WARN] $message" + reset)
    }

    // -----------------------------------------------------------------------------------------------------------------

    // Parsing methods

    fun parseProgram(src: String): Program {
        tokens = tokenize(src)
        return Program(parseStatementList())
    }

    private fun parseStatementList(): ArrayList<Statement>{
        val statementList = arrayListOf<Statement>()
        while (notEOF()){
            statementList.add(parseStatement())
        }

        return statementList
    }

    /**
     * Statement
     *      : ExpressionStatement
     *      | BlockStatement
     *      | EmptyStatement
     *      | VariableStatement
     *      | IfStatement
     *      | UnlessStatement
     *      | FunctionDeclaration
     *      | ReturnStatement
     *      | ModelDefinitionStatement
     *      ;
     */
    private fun parseStatement(): Statement{
        when(current().type){
            TokenType.OpenBrace -> {
                return parseBlockStatement()
            }
            TokenType.Semicolon -> {
                return parseEmptyStatement()
            }
            TokenType.Var, TokenType.Konst -> {
                return parseVariableStatement()
            }
            TokenType.Ako -> {
                return parseIfStatement()
            }
            TokenType.Osim -> {
                return parseUnlessStatement()
            }
            TokenType.Dok, TokenType.Za, TokenType.Radi -> {
                return parseIterationStatement()
            }
            TokenType.Break -> {
                return parseBreakStatement()
            }
            TokenType.Funkcija -> {
                return parseFunctionDeclaration()
            }
            TokenType.Vrati -> {
                return parseReturnStatement()
            }
            TokenType.Model -> {
                return parseModelDefinitionStatement()
            }
            TokenType.Svako -> {
                throw Exception("You are probably missing 'za' before 'svako'")
            }
            TokenType.Ili -> {
                throw Exception("'ili' block must follow an 'ako' block")
            }
            else -> {
                return parseExpressionStatement()
            }
        }
    }

    private fun parseModelDefinitionStatement(): ModelDefinitionStatement {
        expect(TokenType.Model, "A model is defined using the model keyword")
        val name = parseIdentifier()
        expect(TokenType.OpenBrace, "A model definition is surrounded with braces")
        val properties = arrayListOf<ModelProperty>()
        while (current().type != TokenType.EOF && current().type != TokenType.CloseBrace){
            properties.add(parseModelProperty())
        }
        expect(TokenType.CloseBrace, "Missing }")

        if(properties.isEmpty()){
            warning("Model '${name.symbol}' is empty")
        }

        return ModelDefinitionStatement(
            name = name,
            properties = properties
        )
    }

    private fun parseModelProperty(): ModelProperty{
        val name = expect(TokenType.Identifier, "Model property name expected, got ${current().type}").value

        expect(TokenType.Colon, "Missing :")
        val type = parseTypeAnnotation()

        if(current().type != TokenType.CloseBrace){
            expect(TokenType.Comma, "Expected , or }")
        }

        return ModelProperty(
            name = name,
            type = type
        )
    }

    private fun parseExpressionStatement(): Expression{
        val expression = parseExpression()
        expect(TokenType.Semicolon, "Missing ;")
        return expression
    }

    private fun parseIterationStatement(): Statement{
        when(current().type){
            TokenType.Za -> {
                return parseForStatement()
            }
            TokenType.Dok -> {
                return parseWhileStatement()
            }
            TokenType.Radi -> {
                return parseDoWhileStatement()
            }
            else -> {
                throw Exception("Unexpected token found")
            }
        }
    }

    /**
     * WhileStatement
     *      : "dok" "(" Expression ")" Statement
     *      ;
     */
    private fun parseWhileStatement(): WhileStatement{
        expect(TokenType.Dok, "Expected 'dok'")
        expect(TokenType.OpenParen, "Expected '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Expected ')'")

        return WhileStatement(
            condition = condition,
            body = parseStatement()
        )
    }

    private fun parseForStatement(): Statement{
        expect(TokenType.Za, "Expected 'za'")
        expect(TokenType.Svako, "Missing 'svako' following 'za'")
        expect(TokenType.OpenParen, "Expected '('")
        val counter = parseIdentifier()
        expect(TokenType.Od, "Expected starting condition for loop, missing keyword 'od'")
        val startCondition = parseExpression()
        expect(TokenType.Do, "Expected ending condition for loop, missing keyword 'do'")
        val endCondition = parseExpression()
        var step: Expression? = null
        if(current().type == TokenType.Korak){
            consume(/*korak*/)
            step = parseExpression()
        }
        expect(TokenType.CloseParen, "Expected ')'")
        val body = parseStatement()
        return ForStatement(
            counter = counter,
            startValue = startCondition,
            endValue = endCondition,
            step = step,
            body = body
        )
    }

    private fun parseDoWhileStatement(): DoWhileStatement{
        expect(TokenType.Radi, "Expected 'radi'")
        val body = parseStatement()
        expect(TokenType.Dok, "Expected 'dok' after 'radi'")
        expect(TokenType.OpenParen, "Expected '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Expected ')'")
        expect(TokenType.Semicolon, "Missing ';'")

        return DoWhileStatement(
            condition = condition,
            body = body
        )
    }

    private fun parseBreakStatement(): BreakStatement{
        expect(TokenType.Break, "Expected 'prekid'")
        return BreakStatement()
    }

    /**
     * IfStatement
     *      : "ako" "(" Expression ")" Statement
     *      | "ako" "(" Expression ")" Statement "else" Statement
     */
    private fun parseIfStatement(): IfStatement{
        expect(TokenType.Ako, "Expected 'ako'")
        expect(TokenType.OpenParen, "Expected '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Expected ')'")
        val consequent = parseStatement()

        var alternate: Statement? = null
        if(current().type == TokenType.Ili){
            consume()
            alternate = parseIfStatement()
        }
        else if(current().type == TokenType.Inace){
            consume()
            alternate = parseStatement()
        }

        return IfStatement(
            condition=condition,
            consequent=consequent,
            alternate = alternate
        )
    }

    /**
     * UnlessStatement
     *      : "osim" "ako" "(" Expression ")" Statement
     *      | "osim" "ako" "(" Expression ")" Statement "else" Statement
     *      ;
     */
    private fun parseUnlessStatement(): UnlessStatement{
        expect(TokenType.Osim, "Expected 'osim'")
        expect(TokenType.Ako, "Expected 'ako'")
        expect(TokenType.OpenParen, "Expected '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Expected ')'")
        val consequent = parseStatement()

        var alternate: Statement? = null
        if(current().type == TokenType.Inace){
            consume()
            alternate = parseStatement()
        }

        return UnlessStatement(
            condition=condition,
            consequent=consequent,
            alternate = alternate
        )
    }


    /**
     * FunctionDeclaration
     *      : "funkcija" Identifier "(" [FormalParameterList] ")" BlockStatement
     *      ;
     */
    private fun parseFunctionDeclaration(): FunctionDeclaration {
        expect(TokenType.Funkcija, "Error at ${getLineCol()}: Functions are declared with 'funkcija' keyword")
        val functionName = parseIdentifier()
        expect(TokenType.OpenParen, "Missing '('")

        var params: ArrayList<FunctionParameter> = arrayListOf()

        if(current().type != TokenType.CloseParen){
            params = parseFormalParameterList()
        }

        expect(TokenType.CloseParen, "Missing ')'")
        var returnType: TypeAnnotation? = null

        if(current().type == TokenType.Colon){
            // Non-void return function
            consume()
            returnType = parseTypeAnnotation()
        }

        val functionBody: BlockStatement = if(current().type == TokenType.Arrow){
            // Inline function
            consume(/*The arrow*/)
            BlockStatement(body = arrayListOf(ReturnStatement(argument = parseExpression())))
        } else{
            // Normal function
            parseBlockStatement()
        }

        return FunctionDeclaration(
            name = functionName,
            params = params,
            returnType = returnType,
            body = functionBody
        )
    }

    /**
     * ReturnStatement
     *      : "vrati" ("se" | [Expression]) ";"
     *      ;
     */
    private fun parseReturnStatement(): ReturnStatement {
        expect(TokenType.Vrati, "Missing return statement")
        var argument: Expression? = null

        if(current().type != TokenType.Se){
            argument = parseExpression()
        } else{
            expect(TokenType.Se, "Void returns require keyword 'se'")
        }

        expect(TokenType.Semicolon, "Missing ';'")

        return ReturnStatement(
            argument = argument
        )
    }

    /**
     * FormalParameterList
     *      : Identifier
     *      | FormalParameterList "," Identifier
     */
    private fun parseFormalParameterList(): ArrayList<FunctionParameter>{
        val params = arrayListOf<FunctionParameter>()

        do {
            val typename = parseIdentifier()
            var typeAnnotation: TypeAnnotation? = null
            if(current().type == TokenType.Colon){
                consume()
                typeAnnotation = parseTypeAnnotation()
            }
            params.add(FunctionParameter(identifier = typename, type = typeAnnotation))
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Missing ','") != null)

        return params
    }

    /**
     * BlockStatement
     *      : "{" [StatementList] "}"
     *      ;
     */
    private fun parseBlockStatement(): BlockStatement{
        expect(TokenType.OpenBrace, "Expected '{'")
        val body = arrayListOf<Statement>()
        while (current().type !== TokenType.CloseBrace){
            body.add(parseStatement())
        }
        expect(TokenType.CloseBrace, "Expected '}'")
        return BlockStatement(body)
    }

    /**
     * EmptyStatement
     *      : ";"
     *      ;
     */
    private fun parseEmptyStatement(): EmptyStatement{
        expect(TokenType.Semicolon, "")
        return EmptyStatement()
    }


    /**
     * VariableStatement
     *      : "var" | "konst" VariableDeclarationList
     *      ;
     */
    private fun parseVariableStatement(): VariableStatement{
        val modifier = consume()
        if(modifier.value != "var" && modifier.value != "konst"){
            throw Exception("Unexpected token at [${getLineCol()}]")
        }
        val declarations = parseVariableDeclarationList()
        expect(TokenType.Semicolon, "Missing ;")

        return VariableStatement(declarations = declarations, isConstant = modifier.value == "konst")
    }

    /**
     * VariableDeclarationList
     *      : VariableDeclaration
     *      | VariableDeclarationList "," VariableDeclaration
     *      ;
     */
    private fun parseVariableDeclarationList(): ArrayList<VariableDeclaration>{
        val declarations = arrayListOf<VariableDeclaration>()

        do {
            declarations.add(parseVariableDeclaration())
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Expected ,") != null)

        return declarations
    }

    /**
     * VariableDeclaration
     *      : Identifier [VariableInitializer]
     *      ;
     */
    private fun parseVariableDeclaration(): VariableDeclaration {
        val identifier = parseIdentifier()
        var initializer: Expression? = null
        if(current().type != TokenType.Semicolon && current().type != TokenType.Comma){
            initializer = parseVariableInitializer()
        }
        return VariableDeclaration(
            identifier = identifier.symbol,
            value = initializer
        )
    }

    /**
     * VariableInitializer:
     *      : SimpleAssign AssignmentExpression
     *      ;
     */
    private fun parseVariableInitializer(): Expression{
        expect(TokenType.SimpleAssign, "Expected assignment operator")
        return parseExpression()
    }

    /**
     * Expression:
     *      : FunctionExpression
     *      | AdditiveExpression
     *      ;
     */
    private fun parseExpression(): Expression{
        if(current().type == TokenType.Funkcija){
            return parseFunctionExpression()
        }
        return parseAssignmentExpression()
    }

    private fun parseFunctionExpression(): FunctionExpression{
        expect(TokenType.Funkcija, "Functions are declared using the funkcija keyword")
        expect(TokenType.OpenParen, "Expected (")
        var params: ArrayList<FunctionParameter> = arrayListOf()

        if(current().type != TokenType.CloseParen){
            params = parseFormalParameterList()
        }

        expect(TokenType.CloseParen, "Missing ')'")
        var returnType: TypeAnnotation? = null

        if(current().type == TokenType.Colon){
            // Non-void return function
            consume()
            returnType = parseTypeAnnotation()
        }

        val functionBody: BlockStatement = if(current().type == TokenType.Arrow){
            // Inline function
            consume(/*The arrow*/)
            BlockStatement(body = arrayListOf(ReturnStatement(argument = parseExpression())))
        } else{
            // Normal function
            parseBlockStatement()
        }

        return FunctionExpression(
            params = params,
            returnType = returnType,
            body = functionBody
        )
    }

    /**
     * AssignmentExpression:
     *      : LogicalOrExpression
     *      | LeftHandSideExpression AssignmentOperator LogicalOr
     *      ;
     */
    private fun parseAssignmentExpression(): Expression{
        val left = parseLogicalOrExpression()


        if(current().type != TokenType.ComplexAssign && current().type != TokenType.SimpleAssign){
            return left
        }

        return AssignmentExpression(
            assignee = checkValidAssignmentTarget(left),
            assignmentOperator = parseAssignmentOperator(),
            value = parseAssignmentExpression()
        )
    }

    /**
     * LogicalOrExpression
     *      : LogicalOrExpression
     *      | LogicalAndExpression "||" LogicalOrExpression
     *      ;
     */
    private fun parseLogicalOrExpression(): Expression{
        var left = parseLogicalAndExpression()

        while (current().type == TokenType.LogicalOr){
            val operator = consume().value
            val right = parseLogicalAndExpression()

            left = LogicalExpression(left, right, operator)
        }

        return left
    }

    /**
     * LogicalAndExpression
     *      : EqualityExpression
     *      | EqualityExpression "&&" LogicalAndExpression
     *      ;
     */
    private fun parseLogicalAndExpression(): Expression{
        var left = parseEqualityExpression()

        while (current().type == TokenType.LogicalAnd){
            val operator = consume().value
            val right = parseEqualityExpression()

            left = LogicalExpression(left, right, operator)
        }

        return left
    }

    /**
     * EqualityExpression
     *      : RelationalExpression
     *      | RelationalExpression EQUALITY_OPERATOR EqualityExpression
     *      ;
     */
    private fun parseEqualityExpression(): Expression{
        var left = parseRelationalExpression()

        while (current().type == TokenType.EqualityOperator){
            val operator = consume().value

            val right = parseEqualityExpression()
            left = BinaryExpression(left, right, operator)
        }

        return left
    }

    /**
     * RelationalExpression
     *      : AdditiveExpression
     *      | AdditiveExpression RELATIONAL_OPERATOR RelationalExpression
     *      ;
     */
    private fun parseRelationalExpression(): Expression{
        var left = parseAdditiveExpression()

        while (current().type == TokenType.RelationalOperator){
            val operator = consume().value

            val right = parseRelationalExpression()
            left = BinaryExpression(left, right, operator)
        }

        return left
    }

    /**
     * LeftHandSideExpression
     *      : CallMemberExpression
     *      ;
     */
    private fun parseLeftHandSideExpression(): Expression {
        return parseCallMemberExpression()
    }

    /**
     * CallMemberExpression
     *      : MemberExpression
     *      | CallExpression
     *      ;
     */
    private fun parseCallMemberExpression(): Expression {
        val member = parseMemberExpression()

        if(current().type == TokenType.OpenParen){
            return parseCallExpression(member)
        }

        return member
    }

    /**
     * CallExpression
     *      : Callee Arguments
     *      ;
     *
     * Callee
     *      : MemberExpression
     *      | CallExpression
     *      ;
     */
    private fun parseCallExpression(callee: Expression): CallExpression {
        var callExpression = CallExpression(
            callee = callee,
            args = parseArguments()
        )

        if(current().type == TokenType.OpenParen){
            callExpression = parseCallExpression(callExpression)
        }

        return callExpression
    }

    /**
     * Arguments
     *      : "(" [ArgumentsList] ")"
     *      ;
     */
    private fun parseArguments(): ArrayList<Expression>{
        expect(TokenType.OpenParen, "Missing '(")
        var args = arrayListOf<Expression>()
        if(current().type != TokenType.CloseParen){
            args = parseArgumentList()
        }
        expect(TokenType.CloseParen, "Missing ')'")
        return args
    }

    private fun parseArgumentList(): ArrayList<Expression>{
        val argList = arrayListOf<Expression>()

        do {
            argList.add(parseExpression())
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Missing ','") != null)

        return argList
    }

    /**
     * MemberExpression
     *      : PrimaryExpression
     *      | MemberExpression "." Identifier
     *      | MemberExpression "[" Expression "]"
     *      ;
     */
    private fun parseMemberExpression(): Expression{
        var targetObject = parsePrimaryExpression()

        while (current().type == TokenType.Dot || current().type == TokenType.OpenBracket){
            if(current().type == TokenType.Dot){
                consume()
                val property = parseIdentifier()
                targetObject = MemberExpression(
                    isComputed = false,
                    targetObject = targetObject,
                    property = property
                )
            }
            else if(current().type == TokenType.OpenBracket){
                consume()
                val property = parseExpression()
                expect(TokenType.CloseBracket, "Missing ']'")

                targetObject = MemberExpression(
                    isComputed = true,
                    targetObject = targetObject,
                    property = property
                )
            }
        }

        return targetObject
    }

    /**
     * Identifier
     *      : IDENTIFIER
     *      ;
     */

    private fun parseIdentifier(): Identifier{
        val identifier = expect(TokenType.Identifier, "Identifier expected").value
        return Identifier(symbol = identifier)
    }

    /**
     * TypeAnnotation
     *      : Identifier
     *      | Identifier[]
     *      ;
     */
    private fun parseTypeAnnotation(): TypeAnnotation{
        val typename = parseIdentifier()
        var isArray = false
        if(current().type == TokenType.OpenBracket){
            consume()
            expect(TokenType.CloseBracket, "Missing ]")
            isArray = true
        }
        return TypeAnnotation(
            typeName = typename.symbol,
            isArrayType = isArray
        )
    }

    private fun parseAssignmentOperator(): String{
        if(current().type == TokenType.SimpleAssign || current().type == TokenType.ComplexAssign){
            return consume().value
        }
        throw Exception("Unexpected Token")
    }

    /**
     * AdditiveExpression
     *      : MultiplicativeExpression
     *      | AdditiveExpression ("*" | "/" | "%") MultiplicativeExpression
     */
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
     * MultiplicativeExpression
     *      : ExponentiationExpression
     *      | MultiplicativeExpression ("*" | "/" | "%") ExponentiationExpression
     */
    private fun parseMultiplicativeExpression(): Expression{
        var left = parseExponentiationExpression()

        while (current().value == "*" || current().value == "/" || current().value == "%"){
            val operator = consume().value

            val right = parseExponentiationExpression()
            left = BinaryExpression(left, right, operator)
        }

        return left
    }

    /**
     * ExponentiationExpression
     *      : UnaryExpression
     *      | UnaryExpression "^" UnaryExpression
     */
    private fun parseExponentiationExpression(): Expression{
        var left = parseUnaryExpression()

        while (current().type == TokenType.Exponent){
            val operator = consume().value

            val right = parseUnaryExpression()
            left = BinaryExpression(left, right, operator)
        }

        return left
    }

    /**
     * UnaryExpression
     *      : LeftHandSideExpression
     *      | "+"/"-" UnaryExpression
     *      | "!" UnaryExpression
     *      ;
     */
    private fun parseUnaryExpression(): Expression{
        var operator: String? = null

        if(current().value == "+" || current().value == "-"){
            operator = consume().value
        }

        if(operator != null){
            return UnaryExpression(
                operator = operator,
                argument = parseUnaryExpression()
            )
        }

        return parseLeftHandSideExpression()
    }

    /**
     * PrimaryExpression
     *      : Literal
     *      | ParenthesizedExpression
     *      | Identifier
     *      | ThisExpression
     *      | NewExpression
     *      ;
     */
    private fun parsePrimaryExpression(): Expression{
        when(current().type){
            TokenType.Number -> {
                return parseNumericLiteral()
            }
            TokenType.DoubleQuote -> {
                return parseStringLiteral()
            }
            TokenType.OpenParen -> {
                return parseParenthesizedExpression()
            }
            TokenType.OpenBracket -> {
                return parseArrayLiteral()
            }
            TokenType.OpenBrace -> {
                return parseObjectLiteral()
            }
            TokenType.Tacno, TokenType.Netacno -> {
                return parseBooleanLiteral()
            }
            TokenType.Nedefinisano -> {
                return parseNullLiteral()
            }
            else -> {
                return parseIdentifier()
            }
        }
    }

    /**
     * NumericLiteral
     *      : "+/-" 1*(1-9[_])[.1*(1-9[_])]
     */
    private fun parseNumericLiteral(): NumericLiteral{
        return NumericLiteral(value = consume().value.toDouble())
    }

    /**
     * StringLiteral
     *      : " [s+] "
     */
    private fun parseStringLiteral(): StringLiteral{
        consume() //opening double quote
        val str = expect(TokenType.String, "Unexpected token at [${getLineCol()}}]: String literal expected")
        expect(TokenType.DoubleQuote, "Expected closing double quote (\")")
        return StringLiteral(str.value)
    }

    /**
     * BooleanLiteral
     *      : "tacno"
     *      | "netacno"
     *      ;
     */
    private fun parseBooleanLiteral(): BooleanLiteral {
        return BooleanLiteral(
            value = consume().type == TokenType.Tacno
        )
    }

    private fun parseNullLiteral(): NullLiteral {
        consume()
        return NullLiteral()
    }

    /**
     * ParenthesizedExpression
     *      : "(" Expression ")"
     *      ;
     */
    private fun parseParenthesizedExpression(): Expression {
        expect(TokenType.OpenParen, "Expected '('")
        val expression = parseExpression()
        expect(TokenType.CloseParen, "Expected ')'")
        return expression
    }

    private fun parseArrayLiteral(): ArrayLiteral{
        val array = arrayListOf<Expression>()
        expect(TokenType.OpenBracket, "Missing [")

        if(current().type == TokenType.CloseBracket){
            // Empty array
            consume()
            return ArrayLiteral(arrayListOf())
        }

        do {
            val exp = parseExpression()
            array.add(exp)
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Missing ','") != null)

        expect(TokenType.CloseBracket, "Missing ]")

        return ArrayLiteral(
            arr = array
        )
    }

    private fun parseObjectLiteral(): ObjectLiteral{
        expect(TokenType.OpenBrace, "Missing {")
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

        return ObjectLiteral(
            properties = properties
        )
    }
}