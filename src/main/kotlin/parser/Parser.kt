package parser

import errors.BosscriptParsingError
import errors.BosscriptSyntaxError
import lexer.Token
import lexer.TokenType
import lexer.tokenize

class Parser(val js: Boolean = false) {
    private var tokens: ArrayDeque<Token> = ArrayDeque()

    // -----------------------------------------------------------------------------------------------------------------

    // Utility methods

    private fun notEOF(): Boolean {
        return current().type != TokenType.EOF
    }

    private fun current(): Token {
        return tokens[0]
    }

    private fun consume(): Token {
        return tokens.removeFirst()
    }

    private fun getLineCol(): String {
        return current().getLineCol()
    }

    private fun checkValidAssignmentTarget(node: Expression): Expression {
        if (node.kind == NodeType.Identifier || node.kind == NodeType.MemberExpression) {
            return node
        }
        throw BosscriptSyntaxError("Navedenoj lijevoj strani nije moguće dodijeliti vrijednost.", node.start)
    }

    /**
     * Removes the first available token and throws Exception if it doesn't match the expected type.
     */
    private fun expect(expectedType: TokenType, errorMessage: String): Token {
        val prev = consume()
        if (prev.type != expectedType) {
            throw BosscriptParsingError(errorMessage, prev.start)
        }
        return prev
    }

    private fun warning(message: String) {
        val reset = "\u001b[0m"
        val yellow = "\u001b[33m"

        println("$yellow[UPOZORENJE] $message $reset")
    }

    // -----------------------------------------------------------------------------------------------------------------

    // Parsing methods

    fun parseProgram(src: String): Program {
        tokens = tokenize(src, js)
        return Program(parseStatementList())
    }

    private fun parseStatementList(): ArrayList<Statement> {
        val statementList = arrayListOf<Statement>()
        while (notEOF()) {
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
     *      | ImportStatement
     *      | TryCatchStatement
     *      ;
     */
    private fun parseStatement(): Statement {
        when (current().type) {
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

            TokenType.Tip -> {
                return parseTypeDefinitionStatement()
            }

            TokenType.Model -> {
                return parseModelDefinitionStatement()
            }

            TokenType.Paket -> {
                return parseImportStatement()
            }

            TokenType.Try -> {
                return parseTryCatchStatement()
            }

            TokenType.Svako -> {
                throw BosscriptParsingError("Neočekivani token 'svako'. Vjerovatno ste mislili 'za svako'.", current().start)
            }

            else -> {
                return parseExpressionStatement()
            }
        }
    }

    private fun parseJavascriptSnippet(): JavascriptSnippet {
        if(!js){
            throw BosscriptParsingError("Javascript kod nije dozvoljen ovdje.", current().start)
        }
        val start = current().start
        val end = current().end
        val jsCode = expect(TokenType.Javascript, "Nedostaje očekivani Javascript blok")
        return JavascriptSnippet(jsCode.value, start, end)
    }

    private fun parseModelDefinitionStatement(): ModelDefinitionStatement {
        val start = current().start
        expect(TokenType.Model, "Nedostaje ključna riječ 'model'")
        val classname = parseIdentifier()
        var parentClassName: Identifier? = null
        var privateBlock: ModelBlock? = null
        var publicBlock: ModelBlock? = null
        var constructor: FunctionDeclaration? = null

        if (current().value == "<") {
            consume(/* < */)
            parentClassName = parseIdentifier()
        }

        expect(TokenType.OpenBrace, "Nedostaje '{'")

        while(current().type != TokenType.CloseBrace){
            if(current().type == TokenType.Constructor && constructor == null){
                consume(/* konstruktor */)
                expect(TokenType.OpenParen, "Nedostaje '('")

                var params: ArrayList<FunctionParameter> = arrayListOf()

                if (current().type != TokenType.CloseParen) {
                    params = parseFormalParameterList()
                }

                expect(TokenType.CloseParen, "Nedostaje ')'")

                val functionBody: BlockStatement = parseBlockStatement()


                constructor = FunctionDeclaration(
                    name = Identifier("konstruktor"),
                    params = params,
                    returnType = null,
                    body = functionBody
                )
            }
            else if (current().type == TokenType.Private && privateBlock == null) {
                consume(/* privatno */)
                privateBlock = parseModelBlock()
            }
            else if (current().type == TokenType.Public && publicBlock == null) {
                consume(/* javno */)
                publicBlock = parseModelBlock()
            }
            else {
                throw BosscriptParsingError("Neočekivani token pronađen. Definicija modela može sadržavati samo privatni blok, javni blok i konstruktor", current().start)
            }
        }

        val end = current().end
        expect(TokenType.CloseBrace, "Nedostaje '}'")

        if(constructor == null){
            throw BosscriptParsingError("Model ${classname.symbol} nema definisan konstruktor.", start)
        }

        return ModelDefinitionStatement(
            className = classname,
            parentClassName = parentClassName,
            constructor = constructor,
            privateBlock = privateBlock,
            publicBlock = publicBlock,
            start, end
        )
    }

    private fun parseModelBlock(): ModelBlock {
        val start = current().start
        expect(TokenType.OpenBrace, "Nedostaje '{'")
        val modelBlock = ModelBlock()
        while (current().type !== TokenType.CloseBrace) {
            modelBlock.addStatement(parseStatement())
        }
        val end = current().end
        expect(TokenType.CloseBrace, "Nedostaje '}'")
        modelBlock.start = start
        modelBlock.end = end

        return modelBlock
    }

    private fun parseTryCatchStatement(): TryCatchStatement {
        val start = current().start
        expect(TokenType.Try, "Nedostaje 'probaj'")
        val tryBlock = parseBlockStatement()
        expect(TokenType.Catch, "Nedostaje 'spasi' blok")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val exceptionIdentifier = parseIdentifier()
        expect(TokenType.CloseParen, "Nedostaje ')'")

        val catchBlock = parseBlockStatement()
        var end = catchBlock.end

        var finallyBlock: BlockStatement? = null
        if (current().type == TokenType.Finally) {
            consume(/* finally */)
            finallyBlock = parseBlockStatement()
            end = finallyBlock.end
        }

        return TryCatchStatement(
            tryBlock = tryBlock,
            catchBlock = catchBlock,
            exceptionIdentifier = exceptionIdentifier,
            finallyBlock = finallyBlock,
            start, end
        )
    }

    private fun parseImportStatement(): ImportStatement {
        val start = current().start
        var end = current().end
        expect(TokenType.Paket, "Nedostaje ključna riječ 'paket'")
        val packageName = parseStringLiteral()
        val imports = arrayListOf<Identifier>()
        if (current().type == TokenType.Semicolon) {
            // Full package import
            end = current().end
            consume(/*semicolon*/)
            return ImportStatement(
                packageName = packageName.value,
                imports = null,
                start, end
            )
        }
        expect(TokenType.OpenBrace, "Nedostaje '{'")

        do {
            imports.add(parseIdentifier())
        } while (current().type != TokenType.CloseBrace && current().type == TokenType.Comma && expect(
                TokenType.Comma,
                "Nedostaje ','"
            ) != null
        )

        expect(TokenType.CloseBrace, "Nedostaje '}'")
        expect(TokenType.Semicolon, "Nedostaje ';'")
        end = current().end
        return ImportStatement(
            packageName = packageName.value,
            imports = imports,
            start, end
        )
    }

    private fun parseTypeDefinitionStatement(): TipDefinitionStatement {
        val start = current().start
        expect(TokenType.Tip, "Nedostaje ključna riječ 'tip'")
        val name = parseIdentifier()
        var parentType: Identifier? = null
        if (current().value == "<") {
            // Inheritance
            consume(/* < */)
            parentType = parseIdentifier()
        }
        expect(TokenType.OpenBrace, "Nedostaje '{'")
        val properties = arrayListOf<TypeProperty>()
        while (current().type != TokenType.EOF && current().type != TokenType.CloseBrace) {
            properties.add(parseTypeProperty())
        }
        val end = current().end
        expect(TokenType.CloseBrace, "Nedostaje '}'")

        if (properties.isEmpty()) {
            warning("Tip '${name.symbol}' je prazan.")
        }

        return TipDefinitionStatement(
            name = name,
            parentType = parentType,
            properties = properties,
            start, end
        )
    }

    private fun parseTypeProperty(): TypeProperty {
        val start = current().start
        val name = expect(TokenType.Identifier, "Nedozvoljeni token '${current().value}' pronađen unutar definicije tipa.").value

        expect(TokenType.Colon, "Nedostaje ':'")
        val type = parseTypeAnnotation()
        val end = current().end
        expect(TokenType.Semicolon, "Nedostaje ';'")

        return TypeProperty(
            name = name,
            type = type,
            start, end
        )
    }

    private fun parseExpressionStatement(): Expression {
        val expression = parseExpression()
        expect(TokenType.Semicolon, "Nedostaje ';'")
        return expression
    }

    private fun parseIterationStatement(): Statement {
        when (current().type) {
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
                throw BosscriptParsingError("Pronađen neočekivani token", current().start)
            }
        }
    }

    /**
     * WhileStatement
     *      : "dok" "(" Expression ")" Statement
     *      ;
     */
    private fun parseWhileStatement(): WhileStatement {
        val start = current().start
        expect(TokenType.Dok, "Nedostaje 'dok'")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Nedostaje ')'")

        // Same rules like in for-loop (shorthand and full loop)
        var body = BlockStatement(body = arrayListOf())
        if (current().type == TokenType.Arrow) {
            // Shorthand syntax
            consume(/*Arrow*/)
            body.body.add(parseStatement())
        } else {
            body = parseBlockStatement()
        }

        val end = body.end

        return WhileStatement(
            condition = condition,
            body = body,
            start, end
        )
    }

    private fun parseForStatement(): ForStatement {
        val start = current().start
        expect(TokenType.Za, "Nedostaje ključna riječ 'za'")
        expect(TokenType.Svako, "Nedostaje ključna riječ 'svako' nakon 'za'")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val counter = parseIdentifier()
        expect(TokenType.Od, "Nedostaje ključna riječ 'od'")
        val startCondition = parseExpression()
        expect(TokenType.Do, "Nedostaje ključna riječ 'do'")
        val endCondition = parseExpression()
        var step: Expression? = null
        if (current().type == TokenType.Korak) {
            consume(/*korak*/)
            step = parseExpression()
        }
        expect(TokenType.CloseParen, "Nedostaje ')'")

        // For Statement body must be Block Statement
        // But shorthand syntax for single-line for loops is allowed: za svako(...) => ispis();
        // Shorthand syntax is parsed as BlockStatement(body=<the single expression>)
        var body = BlockStatement(body = arrayListOf())

        if (current().type == TokenType.Arrow) {
            // Shorthand syntax
            consume(/*Arrow*/)
            body.body.add(parseStatement())
        } else {
            // Regular loop
            body = parseBlockStatement()
        }

        val end = body.end

        return ForStatement(
            counter = counter,
            startValue = startCondition,
            endValue = endCondition,
            step = step,
            body = body,
            start, end
        )
    }

    private fun parseDoWhileStatement(): DoWhileStatement {
        val start = current().start
        expect(TokenType.Radi, "Nedostaje ključna riječ 'radi'.")
        val body = parseBlockStatement()
        expect(TokenType.Dok, "Nedostaje ključna riječ 'dok' nakon 'radi' bloka.")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Nedostaje ')'")
        val end = current().end
        expect(TokenType.Semicolon, "Nedostaje ';'")

        return DoWhileStatement(
            condition = condition,
            body = body,
            start, end
        )
    }

    private fun parseBreakStatement(): BreakStatement {
        val start = current().start
        val end = current().end
        expect(TokenType.Break, "Nedostaje ključna riječ 'prekid'")
        return BreakStatement(start, end)
    }

    /**
     * IfStatement
     *      : "ako" "(" Expression ")" Statement
     *      | "ako" "(" Expression ")" Statement "else" Statement
     */
    private fun parseIfStatement(): IfStatement {
        val start = current().start
        expect(TokenType.Ako, "Nedostaje ključna riječ 'ako'")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Nedostaje ')'")
        val consequent = parseStatement()
        var end = consequent.end

        var alternate: Statement? = null
        if (current().type == TokenType.Ili) {
            consume()
            alternate = parseIfStatement()
        } else if (current().type == TokenType.Inace) {
            consume()
            alternate = parseStatement()
        }

        if(alternate != null){
            end = alternate.end
        }

        return IfStatement(
            condition = condition,
            consequent = consequent,
            alternate = alternate,
            start, end
        )
    }

    /**
     * UnlessStatement
     *      : "osim" "ako" "(" Expression ")" Statement
     *      | "osim" "ako" "(" Expression ")" Statement "else" Statement
     *      ;
     */
    private fun parseUnlessStatement(): UnlessStatement {
        val start = current().start
        expect(TokenType.Osim, "Nedostaje ključna riječ 'osim'")
        expect(TokenType.Ako, "Nedostaje ključna riječ 'ako'")
        expect(TokenType.OpenParen, "Nedostaje '('")
        val condition = parseExpression()
        expect(TokenType.CloseParen, "Nedostaje ')'")
        val consequent = parseStatement()

        var end = consequent.end

        var alternate: Statement? = null
        if (current().type == TokenType.Inace) {
            consume()
            alternate = parseStatement()
            end = alternate.end
        }

        return UnlessStatement(
            condition = condition,
            consequent = consequent,
            alternate = alternate,
            start, end
        )
    }


    /**
     * FunctionDeclaration
     *      : "funkcija" Identifier "(" [FormalParameterList] ")" BlockStatement
     *      ;
     */
    private fun parseFunctionDeclaration(): FunctionDeclaration {
        val start = current().start
        expect(TokenType.Funkcija, "Nedostaje ključna riječ 'funkcija'")
        val functionName = parseIdentifier()
        expect(TokenType.OpenParen, "Nedostaje '('")

        var params: ArrayList<FunctionParameter> = arrayListOf()

        if (current().type != TokenType.CloseParen) {
            params = parseFormalParameterList()
        }

        expect(TokenType.CloseParen, "Nedostaje ')'")
        var returnType: TypeAnnotation? = null

        if (current().type == TokenType.Colon) {
            // Non-void return function
            consume()
            returnType = parseTypeAnnotation()
        }

        val functionBody: BlockStatement = if (current().type == TokenType.Arrow) {
            // Inline function
            consume(/*The arrow*/)
            BlockStatement(body = arrayListOf(ReturnStatement(argument = parseExpression())))
        } else {
            // Normal function
            parseBlockStatement()
        }

        val end = functionBody.end

        return FunctionDeclaration(
            name = functionName,
            params = params,
            returnType = returnType,
            body = functionBody,
            start, end
        )
    }

    /**
     * ReturnStatement
     *      : "vrati" ("se" | [Expression]) ";"
     *      ;
     */
    private fun parseReturnStatement(): ReturnStatement {
        val start = current().start
        expect(TokenType.Vrati, "Nedostaje ključna riječ 'vrati'")
        var argument: Expression? = null

        if (current().type != TokenType.Se) {
            argument = parseExpression()
        } else {
            expect(TokenType.Se, "Nedostaje ključna riječ 'se', potrebna u slučaju vraćanja bez povratne vrijedosti")
        }

        val end = current().end
        expect(TokenType.Semicolon, "Nedostaje ';'")

        return ReturnStatement(
            argument = argument,
            start, end
        )
    }

    /**
     * FormalParameterList
     *      : Identifier
     *      | FormalParameterList "," Identifier
     */
    private fun parseFormalParameterList(): ArrayList<FunctionParameter> {
        val params = arrayListOf<FunctionParameter>()
        do {
            val start = current().start
            var end = current().end
            val typename = parseIdentifier()
            var typeAnnotation: TypeAnnotation? = null
            if (current().type == TokenType.Colon) {
                consume()
                typeAnnotation = parseTypeAnnotation()
                end = typeAnnotation.end
            }
            params.add(FunctionParameter(identifier = typename, type = typeAnnotation, start, end))
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Nedostaje ','") != null)

        return params
    }

    /**
     * BlockStatement
     *      : "{" [StatementList] "}"
     *      ;
     */
    private fun parseBlockStatement(): BlockStatement {
        val start = current().start
        expect(TokenType.OpenBrace, "Nedostaje '{'")
        val body = arrayListOf<Statement>()
        while (current().type !== TokenType.CloseBrace) {
            body.add(parseStatement())
        }
        val end = current().end
        expect(TokenType.CloseBrace, "Nedostaje '}'")
        return BlockStatement(body, start, end)
    }

    /**
     * EmptyStatement
     *      : ";"
     *      ;
     */
    private fun parseEmptyStatement(): EmptyStatement {
        val start = current().start
        val end = current().end
        expect(TokenType.Semicolon, "Nedostaje ';'")
        return EmptyStatement(start, end)
    }


    /**
     * VariableStatement
     *      : "var" | "konst" VariableDeclarationList
     *      ;
     */
    private fun parseVariableStatement(): VariableStatement {
        val start = current().start
        val modifier = consume()
        if (modifier.value != "var" && modifier.value != "konst") {
            throw BosscriptParsingError("Pronađen neočekivani token ${modifier.value}", modifier.start)
        }
        val declarations = parseVariableDeclarationList()
        val end = current().end
        expect(TokenType.Semicolon, "Nedostaje ';'")

        return VariableStatement(declarations = declarations, isConstant = modifier.value == "konst", start, end)
    }

    /**
     * VariableDeclarationList
     *      : VariableDeclaration
     *      | VariableDeclarationList "," VariableDeclaration
     *      ;
     */
    private fun parseVariableDeclarationList(): ArrayList<VariableDeclaration> {
        val declarations = arrayListOf<VariableDeclaration>()

        do {
            declarations.add(parseVariableDeclaration())
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Nedostaje ','") != null)

        return declarations
    }

    /**
     * VariableDeclaration
     *      : Identifier [VariableInitializer]
     *      ;
     */
    private fun parseVariableDeclaration(): VariableDeclaration {
        val start = current().start
        val identifier = parseIdentifier()
        var initializer: Expression? = null
        var typeAnnotation: TypeAnnotation? = null

        if(current().type == TokenType.Colon){
            // Type Annotation specified
            consume(/* colon */)
            typeAnnotation = parseTypeAnnotation()
        }

        if (current().type != TokenType.Semicolon && current().type != TokenType.Comma) {
            initializer = parseVariableInitializer()
        }

        val end = initializer?.end ?: current().end

        return VariableDeclaration(
            identifier = identifier.symbol,
            value = initializer,
            type = typeAnnotation,
            start, end
        )
    }

    /**
     * VariableInitializer:
     *      : SimpleAssign AssignmentExpression
     *      ;
     */
    private fun parseVariableInitializer(): Expression {
        expect(TokenType.SimpleAssign, "Nedostaje operator dodjeljivanja ('=')")
        return parseExpression()
    }

    /**
     * Expression:
     *      : FunctionExpression
     *      | AdditiveExpression
     *      ;
     */
    private fun parseExpression(): Expression {
        if (current().type == TokenType.Funkcija) {
            return parseFunctionExpression()
        }
        return parseAssignmentExpression()
    }

    private fun parseFunctionExpression(): FunctionExpression {
        val start = current().start
        expect(TokenType.Funkcija, "Nedostaje ključna riječ funkcija")
        expect(TokenType.OpenParen, "Nedostaje '('")
        var params: ArrayList<FunctionParameter> = arrayListOf()

        if (current().type != TokenType.CloseParen) {
            params = parseFormalParameterList()
        }

        expect(TokenType.CloseParen, "Nedostaje ')'")
        var returnType: TypeAnnotation? = null

        if (current().type == TokenType.Colon) {
            // Non-void return function
            consume()
            returnType = parseTypeAnnotation()
        }

        val functionBody: BlockStatement = if (current().type == TokenType.Arrow) {
            // Inline function
            consume(/*The arrow*/)
            BlockStatement(body = arrayListOf(ReturnStatement(argument = parseExpression())))
        } else {
            // Normal function
            parseBlockStatement()
        }

        val end = functionBody.end

        return FunctionExpression(
            params = params,
            returnType = returnType,
            body = functionBody,
            start, end
        )
    }

    /**
     * AssignmentExpression:
     *      : LogicalOrExpression
     *      | LeftHandSideExpression AssignmentOperator LogicalOr
     *      ;
     */
    private fun parseAssignmentExpression(): Expression {
        val left = parseElvisExpression()
        if (current().type != TokenType.ComplexAssign && current().type != TokenType.SimpleAssign) {
            return left
        }

        val assignee = checkValidAssignmentTarget(left)
        val operator = parseAssignmentOperator()
        val value = parseAssignmentExpression()

        val start = left.start
        val end = value.end

        return AssignmentExpression(
            assignee,
            value,
            operator,
            start, end
        )
    }

    private fun parseElvisExpression(): Expression {
        var left = parseLogicalOrExpression()
        val start = left.start

        while (current().value == "?:") {
            val operator = consume().value
            val right = parseElvisExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * LogicalOrExpression
     *      : LogicalOrExpression
     *      | LogicalAndExpression "||" LogicalOrExpression
     *      ;
     */
    private fun parseLogicalOrExpression(): Expression {
        var left = parseLogicalAndExpression()
        val start = left.start

        while (current().type == TokenType.LogicalOr) {
            val operator = consume().value
            val right = parseLogicalAndExpression()
            val end = right.end
            left = LogicalExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * LogicalAndExpression
     *      : EqualityExpression
     *      | EqualityExpression "&&" LogicalAndExpression
     *      ;
     */
    private fun parseLogicalAndExpression(): Expression {
        var left = parseEqualityExpression()
        val start = left.start

        while (current().type == TokenType.LogicalAnd) {
            val operator = consume().value
            val right = parseEqualityExpression()
            val end = right.end
            left = LogicalExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * EqualityExpression
     *      : RelationalExpression
     *      | RelationalExpression EQUALITY_OPERATOR EqualityExpression
     *      ;
     */
    private fun parseEqualityExpression(): Expression {
        var left = parseRelationalExpression()
        val start = left.start

        while (current().type == TokenType.EqualityOperator) {
            val operator = consume().value

            val right = parseEqualityExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * RelationalExpression
     *      : AdditiveExpression
     *      | AdditiveExpression RELATIONAL_OPERATOR RelationalExpression
     *      ;
     */
    private fun parseRelationalExpression(): Expression {
        var left = parseAdditiveExpression()
        val start = left.start

        while (current().type == TokenType.RelationalOperator) {
            val operator = consume().value

            val right = parseRelationalExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * LeftHandSideExpression
     *      : CallMemberExpression
     *      ;
     */
    private fun parseLeftHandSideExpression(): Expression {
        return parseMemberExpression()
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
        val start = callee.start
        expect(TokenType.OpenParen, "Nedostaje '('")
        var args = arrayListOf<Expression>()
        if (current().type != TokenType.CloseParen) {
            args = parseArgumentList()
        }
        val end = current().end
        expect(TokenType.CloseParen, "Nedostaje ')'")
        var callExpression = CallExpression(
            callee = callee,
            args = args,
            start = start,
            end = end
        )

        if (current().type == TokenType.OpenParen) {
            callExpression = parseCallExpression(callExpression)
        }

        return callExpression
    }

    private fun parseArgumentList(): ArrayList<Expression> {
        val argList = arrayListOf<Expression>()

        do {
            argList.add(parseExpression())
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Nedostaje ','") != null)

        return argList
    }

    /**
     * MemberExpression
     *      : PrimaryExpression
     *      | MemberExpression "." Identifier
     *      | MemberExpression "[" Expression "]"
     *      ;
     */
    private fun parseMemberExpression(): Expression {
        var targetObject = parsePrimaryExpression()

        if (targetObject is Identifier && targetObject.symbol == "@" && (current().type == TokenType.Identifier || current().type == TokenType.OpenBracket)) {
            tokens.add(0, Token(".", TokenType.Dot, Pair(Int.MAX_VALUE, Int.MAX_VALUE), Pair(Int.MAX_VALUE, Int.MAX_VALUE)))
        }

        while (current().type == TokenType.Dot || current().type == TokenType.OpenBracket || current().type == TokenType.OpenParen) {
            if (current().type == TokenType.Dot) {
                consume()
                val property = parseIdentifier()
                targetObject = MemberExpression(
                    isComputed = false,
                    targetObject = targetObject,
                    property = property,
                    targetObject.start,
                    property.end
                )
            }
            else if (current().type == TokenType.OpenBracket) {
                consume()
                val property = parseExpression()
                val end = current().end
                expect(TokenType.CloseBracket, "Nedostaje ']'")

                targetObject = MemberExpression(
                    isComputed = true,
                    targetObject = targetObject,
                    property = property,
                    targetObject.start, end
                )
            }
            else if (current().type == TokenType.OpenParen) {
                val callExpression = parseCallExpression(targetObject)
                /*val callExpression = CallExpression(
                    callee = targetObject,
                    args = parseArguments()
                )*/
                targetObject = callExpression
            }
        }

        return targetObject
    }

    /**
     * Identifier
     *      : IDENTIFIER
     *      ;
     */

    private fun parseIdentifier(): Identifier {
        val start = current().start
        val end = current().end

        if (current().type == TokenType.This) {
            consume(/* @ */)
            return Identifier(symbol = "@", start, end)
        }

        val identifier = expect(TokenType.Identifier, "Nedostaje očekivani identifikator").value

        return Identifier(symbol = identifier, start, end)
    }

    /**
     * TypeAnnotation
     *      : Identifier
     *      | Identifier[]
     *      ;
     */
    private fun parseTypeAnnotation(): TypeAnnotation {
        val start = current().start
        var end = current().end

        val typename = parseIdentifier()
        var isArray = false
        if (current().type == TokenType.OpenBracket) {
            consume()
            end = current().end
            expect(TokenType.CloseBracket, "Nedostaje ']'")
            isArray = true
        }
        return TypeAnnotation(
            typeName = typename.symbol,
            isArrayType = isArray,
            start, end
        )
    }

    private fun parseAssignmentOperator(): String {
        if (current().type == TokenType.SimpleAssign || current().type == TokenType.ComplexAssign) {
            return consume().value
        }
        throw BosscriptParsingError("Pronađen neočekivan token ${current().value}", current().start)
    }

    /**
     * AdditiveExpression
     *      : MultiplicativeExpression
     *      | AdditiveExpression ("*" | "/" | "%") MultiplicativeExpression
     */
    private fun parseAdditiveExpression(): Expression {
        var left = parseMultiplicativeExpression()
        val start = left.start

        while (current().value == "+" || current().value == "-") {
            val operator = consume().value

            val right = parseMultiplicativeExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * MultiplicativeExpression
     *      : ExponentiationExpression
     *      | MultiplicativeExpression ("*" | "/" | "%") ExponentiationExpression
     */
    private fun parseMultiplicativeExpression(): Expression {
        var left = parseExponentiationExpression()
        val start = left.start

        while (current().value == "*" || current().value == "/" || current().value == "%") {
            val operator = consume().value

            val right = parseExponentiationExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
        }

        return left
    }

    /**
     * ExponentiationExpression
     *      : UnaryExpression
     *      | UnaryExpression "^" UnaryExpression
     */
    private fun parseExponentiationExpression(): Expression {
        var left = parseUnaryExpression()
        val start = left.start

        while (current().type == TokenType.Exponent) {
            val operator = consume().value

            val right = parseUnaryExpression()
            val end = right.end
            left = BinaryExpression(left, right, operator, start, end)
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
    private fun parseUnaryExpression(): Expression {
        var operator: String? = null
        val validOperators = listOf("+", "-", "++", "--", "!")
        val start = current().start

        if (current().value in validOperators) {
            operator = consume().value
        }

        if (operator != null) {
            val operand = parseUnaryExpression()
            return UnaryExpression(
                operator = operator,
                operand = operand,
                start,
                operand.end
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
    private fun parsePrimaryExpression(): Expression {
        when (current().type) {
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

            TokenType.Javascript -> {
                return parseJavascriptSnippet()
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
    private fun parseNumericLiteral(): NumericLiteral {
        val start = current().start
        val end = current().end
        return NumericLiteral(
            value = consume().value.toDouble(),
            start, end
        )
    }

    /**
     * StringLiteral
     *      : " [s+] "
     */
    private fun parseStringLiteral(): StringLiteral {
        val start = current().start
        consume() //opening double quote
        val str = expect(TokenType.String, "Nedostaje očekivani tekst")
        val end = current().end
        expect(TokenType.DoubleQuote, "Nedostaju navodnici na kraju teksta ('\"')")
        return StringLiteral(str.value, start, end)
    }

    /**
     * BooleanLiteral
     *      : "tacno"
     *      | "netacno"
     *      ;
     */
    private fun parseBooleanLiteral(): BooleanLiteral {
        val start = current().start
        val end = current().end
        return BooleanLiteral(
            value = consume().type == TokenType.Tacno,
            start, end
        )
    }

    private fun parseNullLiteral(): NullLiteral {
        val start = current().start
        val end = current().end
        consume()
        return NullLiteral(start = start, end = end)
    }

    /**
     * ParenthesizedExpression
     *      : "(" Expression ")"
     *      ;
     */
    private fun parseParenthesizedExpression(): Expression {
        expect(TokenType.OpenParen, "Nedostaje '('")
        val expression = parseExpression()
        expect(TokenType.CloseParen, "Nedostaje ')'")
        return expression
    }

    private fun parseArrayLiteral(): ArrayLiteral {
        val array = arrayListOf<Expression>()
        val start = current().start
        expect(TokenType.OpenBracket, "Nedostaje '['")

        if (current().type == TokenType.CloseBracket) {
            // Empty array
            val end = current().end
            consume()
            return ArrayLiteral(arrayListOf(), start, end)
        }

        do {
            val exp = parseExpression()
            array.add(exp)
        } while (current().type == TokenType.Comma && expect(TokenType.Comma, "Nedostaje ','") != null)

        val end = current().end
        expect(TokenType.CloseBracket, "Nedostaje ']'")

        return ArrayLiteral(
            arr = array,
            start, end
        )
    }

    private fun parseObjectLiteral(): ObjectLiteral {
        val start = current().start
        expect(TokenType.OpenBrace, "Nedostaje '{'")
        val properties = arrayListOf<Property>()

        while (notEOF() && current().type != TokenType.CloseBrace) {
            val key = expect(TokenType.Identifier, "Nedostaje očekivani ključ. Ključ mora biti alfanumerička vrijednost - identifikator ili tekst").value

            expect(TokenType.Colon, "Nedostaje ':'")
            val value = parseExpression()

            properties.add(Property(key, value))

            if (current().type != TokenType.CloseBrace) {
                expect(TokenType.Comma, "Nedostaje ',' ili '}'")
            }
        }
        val end = current().end
        expect(TokenType.CloseBrace, "Nedostaje '}'")

        return ObjectLiteral(
            properties = properties,
            start, end
        )
    }
}