package interpreter

import errors.BosscriptRuntimeException
import interpreter.values.*
import interpreter.values.classes.ModelDefinition
import interpreter.values.classes.ModelObject
import isInt
import operatorToFunctionName
import parser.*
import typechecker.TypeChecker
import java.io.File
import kotlin.math.pow

// TODO Implement try-catch for native functions and other places where Exceptions are thrown without access to line-col

class Interpreter {
    private val globalEnv = Environment()
    private val parser = Parser(false)
    private var `this`: RuntimeValue = Null()

    fun evaluateProgram(src: String): ArrayList<RuntimeValue> {
        val program = parser.parseProgram(src)
        val result = arrayListOf<RuntimeValue>()
        program.body.forEach {
            result.add(evaluate(it))
        }
        return result
    }

    private fun evaluate(node: Statement, environment: Environment = globalEnv): RuntimeValue {
        when (node) {
            // ---------------------------------------------------------------------------------------------------------
            // Literals
            is NumericLiteral -> {
                return Broj(value = node.value)
            }

            is StringLiteral -> {
                return Tekst(value = node.value)
            }

            is BooleanLiteral -> {
                return Logicki(value = node.value)
            }

            is NullLiteral -> {
                return Null()
            }

            is ArrayLiteral -> {
                val values = node.arr.map {
                    evaluate(it, environment)
                }
                return Niz(
                    value = values as ArrayList<RuntimeValue>
                )
            }

            is ObjectLiteral -> {
                val obj = Objekat(properties = hashMapOf())
                `this` = obj
                node.properties.forEach {
                    obj.properties[it.key] = evaluate(it.value, environment)
                }
                `this` = Null()
                return obj
            }
            // ---------------------------------------------------------------------------------------------------------

            is BinaryExpression -> {
                return evaluateBinaryExpression(node, environment)
            }

            is UnaryExpression -> {
                return evaluateUnaryExpression(node, environment)
            }

            is Identifier -> {
                return evaluateIdentifier(node, environment)
            }

            is VariableStatement -> {
                evaluateVariableStatement(node, environment)
                return Null()
            }

            is AssignmentExpression -> {
                return evaluateAssignmentExpression(node, environment)
            }

            is BlockStatement -> {
                return evaluateBlockStatement(node, environment)
            }

            is FunctionDeclaration -> {
                return evaluateFunctionDeclaration(node, environment)
            }

            is FunctionExpression -> {
                return evaluateFunctionExpression(node, environment)
            }

            is ReturnStatement -> {
                return evaluateReturnStatement(node, environment)
            }

            is BreakStatement -> {
                return evaluateBreakStatement(node, environment)
            }

            is IfStatement -> {
                return evaluateIfStatement(node, environment)
            }

            is UnlessStatement -> {
                return evaluateUnlessStatement(node, environment)
            }

            is CallExpression -> {
                return evaluateFunctionCall(node, environment)
            }

            is ForStatement -> {
                return evaluateForStatement(node, environment) ?: Null()
            }

            is WhileStatement -> {
                return evaluateWhileStatement(node, environment) ?: Null()
            }

            is DoWhileStatement -> {
                return evaluateDoWhileStatement(node, environment) ?: Null()
            }

            is MemberExpression -> {
                return evaluateMemberExpression(node, environment)
            }

            is LogicalExpression -> {
                return evaluateLogicalExpression(node, environment)
            }

            is ImportStatement -> {
                return evaluateImportStatement(node, environment)
            }

            is TipDefinitionStatement -> {
                return evaluateTypeDefinition(node, environment)
            }

            is ModelDefinitionStatement -> {
                return evaluateModelDefinition(node, environment)
            }

            is TryCatchStatement -> {
                return evaluateTryCatch(node, environment)
            }

            else -> {
                throw BosscriptRuntimeException(
                    text = "Nepodržan token pronađen.",
                    location = node.start
                )
            }
        }
    }

    private fun evaluateBinaryExpression(expr: BinaryExpression, env: Environment): RuntimeValue {
        val left = evaluate(expr.left, env)
        val right = evaluate(expr.right, env)
        if(left is ModelObject){
            return evaluateModelBinaryExpression(left, right, expr.operator, env)
        }
        when (expr.operator) {
            "+" -> {
                if (left is Broj && right is Broj) {
                    return Broj(
                        value = left.value + right.value
                    )
                }
                else if (left is Tekst && right is Tekst) {
                    return Tekst(
                        value = left.value + right.value
                    )
                }

                else if(left is Tekst){
                    val rightStr = right.toString()
                    return Tekst(
                        value = left.value + rightStr
                    )
                }
                throw BosscriptRuntimeException(
                    text = "Operator '+' nije definisan za operande ${left.typename} i ${right.typename}",
                    location = expr.start
                )
            }

            "-" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '-' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Broj(
                    value = left.value - right.value
                )
            }

            "*" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '*' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Broj(
                    value = left.value * right.value
                )
            }

            "/" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '/' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Broj(
                    value = left.value / right.value
                )
            }

            "%" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '%' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Broj(
                    value = left.value % right.value
                )
            }

            "^" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '^' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Broj(
                    value = left.value.pow(right.value)
                )
            }

            "<" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '<' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Logicki(
                    value = left.value < right.value
                )
            }

            "<=" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '<=' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Logicki(
                    value = left.value <= right.value
                )
            }

            ">" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '>' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Logicki(
                    value = left.value > right.value
                )
            }

            ">=" -> {
                if (left !is Broj || right !is Broj) {
                    throw BosscriptRuntimeException(
                        text = "Operator '>=' nije definisan za operande ${left.typename} i ${right.typename}",
                        location = expr.start
                    )
                }

                return Logicki(
                    value = left.value >= right.value
                )
            }

            "==" -> {
                return Logicki(
                    value = left.value == right.value
                )
            }

            "!=" -> {
                return Logicki(
                    value = left.value != right.value
                )
            }

            "?:" -> {
                if(left is Null){
                    return right
                }
                return left
            }

            else -> {
                throw NotImplementedError("Nepodržani operator pronađen")
            }
        }
    }

    private fun evaluateModelBinaryExpression(left: ModelObject, right: RuntimeValue, operator: String, env: Environment): RuntimeValue {
        when(operator){
            "+", "-", "*", "/", "<", ">", "==", "!=" -> {
                val operatorFun = left.getProperty(operatorToFunctionName(operator))
                if(operatorFun !is Funkcija){
                    throw BosscriptRuntimeException(
                        text = "$operatorFun nije funkcija",
                        location = Pair(-1, -1) // TODO
                    )
                }
                `this` = left
                val activationRecord = hashMapOf<String, RuntimeValue>("@" to `this`)
                val typeChecker = TypeChecker(env)

                if(operatorFun.params[0].type != null){
                    operatorFun.params[0].type?.let { typeChecker.expect(it, right) } // TODO
                }

                activationRecord[operatorFun.params[0].identifier.symbol] = right

                val functionEnv = Environment(parent = env, variables = activationRecord)
                val functionResult = evaluateBlockStatement(operatorFun.body, functionEnv)

                if (operatorFun.returnType != null) {
                    when (functionResult) {
                        is ReturnValue -> typeChecker.expect(operatorFun.returnType, functionResult.value) // TODO
                        else -> typeChecker.expect(operatorFun.returnType, functionResult) // TODO
                    }
                }

                return if (functionResult is ReturnValue) functionResult.value else functionResult
            }

            else -> {
                throw BosscriptRuntimeException(
                    text = "Operator $operator nije definisan za dati model '${left.typename}",
                    location = Pair(-1, -1) // TODO
                )
            }
        }
    }

    private fun evaluateModelDefinition(stmt: ModelDefinitionStatement, env: Environment): RuntimeValue {
        val classname = stmt.className.symbol
        var superclass: ModelDefinition? = null
        val members = hashMapOf<String, RuntimeValue>()
        val privateMembers = mutableSetOf<String>()

        if (stmt.parentClassName != null) {
            superclass = evaluateIdentifier(stmt.parentClassName, env) as ModelDefinition
        }

        val constructor = Funkcija(
            name = stmt.constructor.name.symbol,
            params = stmt.constructor.params,
            returnType = stmt.constructor.returnType,
            body = stmt.constructor.body,
            parentEnv = env
        )

        val modelEnv = Environment(env)

        if (stmt.publicBlock != null) {
            stmt.publicBlock.getBody().forEach {
                when (it) {
                    is FunctionDeclaration -> {
                        members[it.name.symbol] = evaluateFunctionDeclaration(it, modelEnv)
                    }

                    is VariableStatement -> {
                        it.declarations.forEach { vd ->
                            val memberValue = evaluate(vd.value ?: NullLiteral(), modelEnv)
                            if(vd.type != null && memberValue !is Null){
                                val typeChecker = TypeChecker(modelEnv)
                                typeChecker.expect(vd.type, memberValue, vd.start)
                            }
                            members[vd.identifier] = memberValue
                        }
                    }

                    else -> {
                        throw BosscriptRuntimeException(
                            text = "Pronađen nedozvoljen iskaz unutar javnog bloka modela.",
                            location = it.start
                        )
                    }
                }
            }
        }
        if (stmt.privateBlock != null) {
            stmt.privateBlock.getBody().forEach {
                when (it) {
                    is FunctionDeclaration -> {
                        privateMembers.add(it.name.symbol)
                        members[it.name.symbol] = evaluateFunctionDeclaration(it, modelEnv)
                    }

                    is VariableStatement -> {
                        it.declarations.forEach { vd ->
                            val memberValue = evaluate(vd.value ?: NullLiteral(), modelEnv)
                            if(vd.type != null && memberValue !is Null){
                                val typeChecker = TypeChecker(modelEnv)
                                typeChecker.expect(vd.type, memberValue, vd.start)
                            }
                            privateMembers.add(vd.identifier)
                            members[vd.identifier] = memberValue
                        }
                    }

                    else -> {
                        throw BosscriptRuntimeException(
                            text = "Pronađen nedozvoljen iskaz unutar privatnog bloka modela.",
                            location = it.start
                        )
                    }
                }
            }
        }

        val definition = ModelDefinition(
            className = classname,
            superclass = superclass,
            constructor = constructor,
            members = members,
            privateMembers = privateMembers
        )

        env.declareVariable(classname, definition)

        return Null()
    }

    private fun evaluateTryCatch(stmt: TryCatchStatement, env: Environment): RuntimeValue {
        var result: RuntimeValue = Null()
        try {
            result = evaluate(stmt.tryBlock, env)
        }
        catch (e: BosscriptRuntimeException){
            val catchEnv = Environment(env)
            catchEnv.declareVariable(stmt.exceptionIdentifier.symbol, e.exceptionObject, isConstant = true)
            result = evaluate(stmt.catchBlock, catchEnv)
        }
        catch (e: Exception) {
            val catchEnv = Environment(env)
            catchEnv.declareVariable(
                stmt.exceptionIdentifier.symbol,
                Objekat(properties = hashMapOf(
                    "poruka" to Tekst("${e.message}")
                )),
                isConstant = true
            )
            result = evaluate(stmt.catchBlock, catchEnv)
        }

        if (stmt.finallyBlock != null) {
            val finallyEnv = Environment(env)
            result = evaluate(stmt.finallyBlock, finallyEnv)
        }

        return result
    }
    private fun evaluatePackage(src: String, env: Environment) {
        val packageAST = parser.parseProgram(src)
        packageAST.body.forEach {
            if(
                it is FunctionDeclaration
                || it is VariableStatement
                || it is ModelDefinitionStatement
                || it is TipDefinitionStatement
            ){
                evaluate(it, env)
            }
        }
    }

    private fun evaluateImportStatement(stmt: ImportStatement, env: Environment): RuntimeValue {
        val stdlibPackage = stdlib[stmt.packageName]
        if (stdlibPackage != null) {
            evaluateImportStdlibPackage(stdlibPackage, env, stmt.imports)
        } else {
            evaluateImportUserPackage(stmt.packageName, stmt.imports, env)
        }

        return Null()
    }

    private fun evaluateImportUserPackage(packageName: String, imports: ArrayList<Identifier>?, env: Environment) {
        val pckg = File(packageName)
        if (!pckg.exists()) {
            throw BosscriptRuntimeException(
                text = "Paket $packageName ne postoji",
                location = Pair(-1, -1) // TODO
            )
        }

        val tempEnv = Environment()
        evaluatePackage(pckg.readText(), tempEnv)
        if (imports == null) {
            // Full import
            env.importEnv(tempEnv)
        }
        else {
            imports.forEach {
                val packageElement = tempEnv.getVariableOrNull(it.symbol)
                if(packageElement == null){
                    val typeDef = tempEnv.resolveTypeDefinition(it.symbol) ?: throw BosscriptRuntimeException(text = "${it.symbol} ne postoji u paketu $packageName", location = it.start)
                    env.importTypeDefinition(typeDef)
                }
                else {
                    env.declareVariable(it.symbol, packageElement)
                }
            }
        }
    }

    private fun evaluateImportStdlibPackage(target: Environment, env: Environment, imports: ArrayList<Identifier>?) {
        if (imports == null) {
            // Full import
            env.importEnv(target)
        }
        else {
            imports.forEach {
                val packageElement = target.getVariable(it.symbol)
                env.declareVariable(it.symbol, packageElement)
            }
        }
    }

    private fun evaluateUnaryExpression(expr: UnaryExpression, env: Environment): RuntimeValue {
        val arg = evaluate(expr.operand, env)

        when (expr.operator) {
            "+" -> {
                if (arg is Tekst) {
                    return Broj(
                        value = arg.value.toDouble()
                    )
                }
                if (arg is Broj) {
                    return arg
                }
                throw BosscriptRuntimeException(
                    text = "Unarni operator '+' nije definisan za operand ${arg.typename}",
                    location = expr.start
                )
            }

            "-" -> {
                if (arg is Tekst) {
                    return Broj(
                        value = -(arg.value.toDouble())
                    )
                }
                if (arg is Broj) {
                    return Broj(
                        value = -arg.value
                    )
                }
                throw BosscriptRuntimeException(
                    text = "Unarni operator '-' nije definisan za operand ${arg.typename}",
                    location = expr.start
                )
            }

            "++" -> {
                if (arg is Broj) {
                    arg.value++
                    return Broj(
                        value = arg.value + 1
                    )
                }
                throw BosscriptRuntimeException(
                    text = "Unarni operator inkrementiranja '++' nije definisan za operand ${arg.typename}",
                    location = expr.start
                )
            }

            "--" -> {
                if (arg is Broj) {
                    arg.value--
                    return Broj(
                        value = arg.value - 1
                    )
                }
                throw BosscriptRuntimeException(
                    text = "Unarni operator dekrementiranja '--' nije definisan za operand ${arg.typename}",
                    location = expr.start
                )
            }

            "!" -> {
                if (arg is Logicki) {
                    return Logicki(
                        value = !arg.value
                    )
                }
                return Logicki(
                    value = arg is Null
                )
            }

            else -> {
                throw BosscriptRuntimeException(
                    text = "Pronađen nepodržani operator ${expr.operator}",
                    location = expr.start
                )
            }
        }
    }

    private fun evaluateLogicalExpression(expr: LogicalExpression, env: Environment): Logicki {
        val left = evaluate(expr.left, env)
        val right = evaluate(expr.right, env)

        if (left.value !is Boolean){
            throw BosscriptRuntimeException(
                text = "Vrijednost $left (${left.typename}) nije odgovarajućeg tipa. Oba operanda logičkog izraza moraju biti tipa 'logički'.",
                location = expr.left.start
            )
        }

        if (right.value !is Boolean){
            throw BosscriptRuntimeException(
                text = "Vrijednost $right (${right.typename}) nije odgovarajućeg tipa. Oba operanda logičkog izraza moraju biti tipa 'logički'.",
                location = expr.right.start
            )
        }

        left as Logicki
        right as Logicki

        when (expr.operator) {
            "&&" -> {
                return Logicki(
                    value = left.value && right.value
                )
            }

            "||" -> {
                return Logicki(
                    value = left.value || right.value
                )
            }

            else -> {
                throw BosscriptRuntimeException(
                    text = "Pronađen nepodržani operator ${expr.operator}",
                    location = expr.start
                )
            }
        }
    }

    private fun evaluateIdentifier(identifier: Identifier, environment: Environment): RuntimeValue {
        val variable = environment.getVariableOrNull(identifier.symbol)
        if(variable == null) {
            return environment.resolveTypeDefinition(identifier.symbol) ?: throw BosscriptRuntimeException(
                text = "${identifier.symbol} ne postoji.",
                location = identifier.start
            )
        }
        return variable
    }

    private fun evaluateVariableStatement(stmt: VariableStatement, env: Environment) {
        stmt.declarations.forEach {
            evaluateVariableDeclaration(it, env, stmt.isConstant)
        }
    }

    private fun evaluateVariableDeclaration(declaration: VariableDeclaration, env: Environment, isConstant: Boolean) {
        val value = if (declaration.value == null) Null() else evaluate(declaration.value, env)

        env.declareVariable(declaration.identifier, value, declaration.type, isConstant)
    }

    private fun evaluateAssignmentExpression(expr: AssignmentExpression, env: Environment): RuntimeValue {
        if (expr.assignee !is Identifier && expr.assignee !is MemberExpression) {
            throw BosscriptRuntimeException(
                text = "Datoj lijevoj strani nije moguće dodijeliti vrijednost",
                location = expr.assignee.start
            )
        }

        return when (expr.assignmentOperator) {
            "=" -> evaluateSimpleAssignment(expr, env)
            "+=" -> evaluatePlusAssignment(expr, env)
            "-=" -> evaluateMinusAssignment(expr, env)
            "*=" -> evaluateTimesAssignment(expr, env)
            "/=" -> evaluateDivisionAssignment(expr, env)
            "%=" -> evaluateRemainderAssignment(expr, env)
            else -> throw BosscriptRuntimeException(
                text = "Pronađen nepodržani operator ${expr.assignmentOperator}",
                location = expr.start
            )
        }
    }

    private fun evaluateRemainderAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj) {
            target.value %= addedValue.value
            return target
        }
        throw BosscriptRuntimeException(
            text = "Operator '%=' nije definisan za operande ${target.typename} i ${addedValue.typename}",
            location = expr.start
        )
    }

    private fun evaluateDivisionAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj) {
            target.value /= addedValue.value
            return target
        }
        throw BosscriptRuntimeException(
            text = "Operator '/=' nije definisan za operande ${target.typename} i ${addedValue.typename}",
            location = expr.start
        )
    }

    private fun evaluateTimesAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj) {
            target.value *= addedValue.value
            return target
        }
        throw BosscriptRuntimeException(
            text = "Operator '*=' nije definisan za operande ${target.typename} i ${addedValue.typename}",
            location = expr.start
        )
    }

    /**
     * Evaluates -= operator
     */
    private fun evaluateMinusAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj) {
            target.value -= addedValue.value
            return target
        }
        throw BosscriptRuntimeException(
            text = "Operator '-=' nije definisan za operande ${target.typename} i ${addedValue.typename}",
            location = expr.start
        )
    }

    /**
     * Evaluates += operator
     */
    private fun evaluatePlusAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj) {
            target.value += addedValue.value
            return target
        }
        if (target is Tekst && addedValue is Tekst) {
            target.value = target.value + addedValue.value
            return target
        }
        throw BosscriptRuntimeException(
            text = "Operator '+=' nije definisan za operande ${target.typename} i ${addedValue.typename}",
            location = expr.start
        )
    }

    private fun evaluateSimpleAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        if (expr.assignee is Identifier) {
            return env.assignVariable(expr.assignee.symbol, evaluate(expr.value, env))
        }

        if (expr.assignee is MemberExpression) {
            val target = evaluate(expr.assignee.targetObject, env)
            val newValue = evaluate(expr.value, env)

            if(target is ReadonlyObject){
                throw BosscriptRuntimeException(
                    text = "Nije moguće dodijeliti novu vrijednost polju zaključanog objekta",
                    location = expr.assignee.start
                )
            }

            if (target is Objekat) {
                `this` = target
                if(expr.assignee.isComputed){
                    val prop = evaluate(expr.assignee.property, env)
                    if (prop is Tekst) {
                        return target.setProperty(prop.value, evaluate(expr.value, env))
                    }
                    if (prop is Broj) {
                        throw BosscriptRuntimeException(
                            text = "Tip '${target.typename}' ne podržava pristupanje poljima preko indeksa",
                            location = expr.assignee.start
                        )
                    }
                }
                else if (expr.assignee.property is Identifier) {
                    // a.b.c = 10;
                    return target.setProperty(expr.assignee.property.symbol, newValue)
                }
                else {
                    throw BosscriptRuntimeException(
                        text = "Neispravno dodjeljivanje vrijednosti",
                        location = expr.start
                    )
                }
            }

            if (target is Niz) {
                val prop = evaluate(expr.assignee.property, env)
                if (prop is Broj) {
                    target.set(index = prop.value.toInt(), newValue)
                    return Null()
                }
            }

            if (target is ModelObject) {
                val modelDefinition = env.getVariableOrNull(target.typename)
                    ?: throw BosscriptRuntimeException(text = "${target.typename} ne postoji.", location = expr.start)
                modelDefinition as ModelDefinition

                val prop = (expr.assignee.property as Identifier).symbol

                if (modelDefinition.isPrivate(prop)) {
                    if (env.getVariableOrNull("@") == null || env.getVariableOrNull("@")!!.typename != target.typename) {
                        throw BosscriptRuntimeException(
                            text = "'$prop' je privatno polje modela ${target.typename}.",
                            location = expr.assignee.property.start
                        )
                    }
                }

                `this` = target
                target.setInstanceMember(prop, newValue)
                return Null()
            }

        }
        throw BosscriptRuntimeException(
            text = "Neispravno dodjeljivanje vrijednosti",
            location = expr.start
        )
    }

    fun evaluateBlockStatement(block: BlockStatement, env: Environment): RuntimeValue {
        val blockEnv = Environment(parent = env)
        var result: RuntimeValue = Null()
        for (stmt in block.body) {
            if (result !is ReturnValue && result !is BreakValue) {
                val stmtResult = evaluate(stmt, blockEnv)
                if (stmtResult is ReturnValue || stmtResult is BreakValue) {
                    result = stmtResult
                }
            }
        }
        return result
    }

    private fun evaluateFunctionDeclaration(declaration: FunctionDeclaration, env: Environment): Funkcija {
        val fn = Funkcija(
            name = declaration.name.symbol,
            params = declaration.params,
            returnType = declaration.returnType,
            body = declaration.body,
            parentEnv = env
        )

        env.declareVariable(name = declaration.name.symbol, fn)

        return fn
    }

    private fun evaluateFunctionExpression(expr: FunctionExpression, env: Environment): Funkcija {
        return Funkcija(
            name = "λ",
            params = expr.params,
            returnType = expr.returnType,
            body = expr.body,
            parentEnv = env
        )
    }

    private fun evaluateFunctionCall(call: CallExpression, env: Environment): RuntimeValue {
        when (val fn = evaluate(call.callee, env)) {
            is Funkcija -> {
                val activationRecord = hashMapOf("@" to `this`)
                val typeChecker = TypeChecker(env)

                if(fn.params.size != call.args.size){
                    throw BosscriptRuntimeException(
                        text = "Očekivani broj argumenata je ${fn.params.size}, a proslijeđeno je ${call.args.size}",
                        location = call.start
                    )
                }

                fn.params.forEachIndexed { index, param ->
                    val providedParam = evaluate(call.args[index], env)
                    if (param.type != null) {
                        typeChecker.expect(param.type, providedParam)
                    }
                    activationRecord[param.identifier.symbol] = providedParam
                }

                val functionEnv = Environment(parent = env, variables = activationRecord)
                val functionResult = evaluateBlockStatement(fn.body, functionEnv)

                if (fn.returnType != null) {
                    when (functionResult) {
                        is ReturnValue -> typeChecker.expect(fn.returnType, functionResult.value)
                        else -> typeChecker.expect(fn.returnType, functionResult)
                    }
                }

                return if (functionResult is ReturnValue) functionResult.value else functionResult
            }

            is NativeFunction -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }
                return fn.call(args)
            }

            is ContextualNativeFunction -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }
                return fn.call(args, this)
            }

            is Tip -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }

                return fn.constructor(args, env)
            }

            is ModelDefinition -> {
                return evaluateModelInstantiation(fn, call.args, env)
            }

            else -> {
                throw BosscriptRuntimeException(
                    text = "${fn.typename} nije funkcija",
                    location = call.callee.start
                )
            }
        }
    }

    private fun evaluateModelInstantiation(
        definition: ModelDefinition,
        constructorArgs: ArrayList<Expression>,
        env: Environment
    ): ModelObject {

        if(definition.superclass == null){
            val prototype = ModelObject(
                prototype = null,
                instanceObject = definition.members
            )
            val instance = ModelObject(
                prototype = prototype,
                typename = definition.className
            )

            `this` = instance

            val activationRecord = hashMapOf<String, RuntimeValue>("@" to `this`)
            val typeChecker = TypeChecker(env)

            if(definition.constructor.params.size != constructorArgs.size){
                throw BosscriptRuntimeException(
                    text = "Očekivani broj argumenata je ${definition.constructor.params.size}, a proslijeđeno je ${constructorArgs.size}",
                    location = constructorArgs[0].start
                )
            }

            definition.constructor.params.forEachIndexed { index, param ->
                val providedParam = evaluate(constructorArgs[index], env)
                if (param.type != null) {
                    typeChecker.expect(param.type, providedParam)
                }
                activationRecord[param.identifier.symbol] = providedParam
            }

            val functionEnv = Environment(parent = env, variables = activationRecord)
            evaluateBlockStatement(definition.constructor.body, functionEnv)

            return instance
        }

        else {
            val firstExpression = definition.constructor.body.body.first()
            if(firstExpression !is CallExpression || firstExpression.callee != Identifier("prototip")){
                throw BosscriptRuntimeException(
                    text = "Nedostaje poziv konstruktoru prototipa modela 'prototip()'",
                    location = firstExpression.start
                )
            }

            val typeChecker = TypeChecker(env)
            val activationRecord = hashMapOf<String, RuntimeValue>()

            definition.constructor.params.forEachIndexed { index, param ->
                val providedParam = evaluate(constructorArgs[index], env)
                if (param.type != null) {
                    typeChecker.expect(param.type, providedParam)
                }
                activationRecord[param.identifier.symbol] = providedParam
            }

            val functionEnv = Environment(parent = env, variables = activationRecord)

            val prototype = ModelObject(
                prototype = evaluateModelInstantiation(definition.superclass, firstExpression.args, functionEnv),
                instanceObject = definition.members,
                typename = definition.superclass.className
            )

            val instance = ModelObject(
                prototype = prototype,
                typename = definition.className
            )

            `this` = instance
            functionEnv.declareVariable("@", `this`)
            val constructor = BlockStatement(ArrayList(definition.constructor.body.body))
            constructor.body.removeAt(0)
            evaluateBlockStatement(constructor, functionEnv)

            return instance
        }
    }

    private fun evaluateIfStatement(stmt: IfStatement, env: Environment): RuntimeValue {
        val condition = evaluate(stmt.condition, env)
        if (condition.value !is Boolean) {
            throw BosscriptRuntimeException(
                text = "Uslov ako-izraza mora biti tipa 'logički'. '${condition.typename}' nije odgovarajući tip.",
                location = stmt.condition.start
            )
        }

        var result: RuntimeValue = Null()

        if (condition.value == true) {
            result = evaluate(stmt.consequent, env)
        }
        else if (stmt.alternate != null) {
            result = evaluate(stmt.alternate, env)
        }

        return result
    }

    private fun evaluateUnlessStatement(stmt: UnlessStatement, env: Environment): RuntimeValue {
        val condition = evaluate(stmt.condition, env)
        if (condition.value !is Boolean) {
            throw BosscriptRuntimeException(
                text = "Uslov osim-ako-izraza mora biti tipa 'logički'. '${condition.typename}' nije odgovarajući tip.",
                location = stmt.condition.start
            )
        }

        var result: RuntimeValue = Null()

        if (condition.value == false) {
            result = evaluate(stmt.consequent, env)
        } else if (stmt.alternate != null) {
            result = evaluate(stmt.alternate, env)
        }

        return result
    }

    private fun evaluateForStatement(stmt: ForStatement, env: Environment): ReturnValue? {
        val startValue = evaluate(stmt.startValue, env)
        val endValue = evaluate(stmt.endValue, env)



        if (startValue.value !is Double) {
            throw BosscriptRuntimeException(
                text = "Početna vrijednost petlje mora biti broj. ${startValue.typename} nije odgovarajući tip.",
                location = stmt.startValue.start
            )
        }

        if (endValue.value !is Double) {
            throw BosscriptRuntimeException(
                text = "Gornja granica petlje mora biti broj. ${startValue.typename} nije odgovarajući tip.",
                location = stmt.endValue.start
            )
        }

        val start = startValue.value as Double
        val end = endValue.value as Double

        val step: Double
        if (stmt.step == null) {
            // Infer the step if not provided
            step = 1.0
        }
        else {
            // Otherwise interpret the provided step
            val stepVal = evaluate(stmt.step, env)
            if (stepVal.value !is Double) {
                throw BosscriptRuntimeException(
                    text = "Korak petlje mora biti broj. ${startValue.typename} nije odgovarajući tip.",
                    location = stmt.endValue.start
                )
            }
            step = stepVal.value as Double
        }


        val activationRecord = hashMapOf<String, RuntimeValue>()
        activationRecord[stmt.counter.symbol] = startValue
        val loopEnv = Environment(variables = activationRecord, parent = env)

        if (start < end) {
            // Regular ascending loop
            var i = start
            while (i < end + 1) {
                val iterationResult = evaluate(stmt.body, loopEnv)
                if (iterationResult is ReturnValue) {
                    return iterationResult
                }
                if (iterationResult is BreakValue) {
                    return null
                }
                i += step
                loopEnv.assignVariable(stmt.counter.symbol, Broj(value = i))
            }
        }
        else {
            // Backward loop
            var i = start
            while (i > end - 1) {
                val iterationResult = evaluate(stmt.body, loopEnv)
                if (iterationResult is ReturnValue) {
                    return iterationResult
                }
                if (iterationResult is BreakValue) {
                    return null
                }
                i -= step
                loopEnv.assignVariable(stmt.counter.symbol, Broj(value = i))
            }
        }
        return null
    }

    private fun evaluateWhileStatement(stmt: WhileStatement, env: Environment): RuntimeValue? {
        while (evaluate(stmt.condition, env).value == true) {
            val iterationResult = evaluate(stmt.body, env)
            if (iterationResult is ReturnValue) {
                return iterationResult
            }
            if (iterationResult is BreakValue) {
                return null
            }
        }
        return null
    }

    private fun evaluateDoWhileStatement(stmt: DoWhileStatement, env: Environment): RuntimeValue? {
        do {
            val iterationResult = evaluate(stmt.body, env)
            if (iterationResult is ReturnValue) {
                return iterationResult
            }
            if (iterationResult is BreakValue) {
                return null
            }
        } while (evaluate(stmt.condition, env).value == true)

        return null
    }

    private fun evaluateReturnStatement(stmt: ReturnStatement, env: Environment): ReturnValue {
        if (stmt.argument == null) {
            // void return
            return ReturnValue(value = Null())
        }
        return ReturnValue(value = evaluate(stmt.argument, env))
    }

    private fun evaluateBreakStatement(stmt: BreakStatement, env: Environment): BreakValue {
        return BreakValue()
    }

    private fun evaluateMemberExpression(expr: MemberExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.targetObject, env)
        `this` = target
        if (expr.isComputed) {
            val prop = evaluate(expr.property, env)
            when (prop) {
                is Tekst -> {
                    // obj["hello"];
                    return target.getProperty(prop.value)
                }

                is Broj -> {
                    // obj[1];
                    if (!prop.value.isInt()) {
                        throw BosscriptRuntimeException(
                            text = "Indeks mora biti cijeli broj. ${prop.value} nije validan indeks.",
                            location = expr.property.start
                        )
                    }

                    if (target is Niz) {
                        return target.getElement(prop.value.toInt())
                    }
                    else if (target is Tekst) {
                        return Tekst(
                            value = "${target.value[prop.value.toInt()]}"
                        )
                    }

                    throw BosscriptRuntimeException(
                        text = "Tip '${target.typename}' ne podržava pristupanje poljima preko indeksa",
                        location = expr.property.start
                    )
                }

                else -> {
                    throw BosscriptRuntimeException(
                        text = "Vrijednost tipa '${target.typename}' se ne može koristiti kao indeks",
                        location = expr.property.start
                    )
                }
            }

        }
        else {
            val propertyName = (expr.property as Identifier).symbol

            if (target is ModelObject && propertyName != "__proto__") {
                val modelDefinition = env.getVariableOrNull(target.typename)
                    ?: throw BosscriptRuntimeException(text = "${target.typename} ne postoji.", location = expr.start)
                modelDefinition as ModelDefinition

                if (modelDefinition.isPrivate(propertyName)) {
                    if (env.getVariableOrNull("@") == null || env.getVariableOrNull("@")!!.typename != target.typename) {
                        throw BosscriptRuntimeException(
                            text = "'$propertyName' je privatno polje modela ${target.typename}.",
                            location = expr.property.start
                        )
                    }
                }
            }
            return target.getProperty(propertyName)
        }
    }

    private fun evaluateTypeDefinition(typeDefinition: TipDefinitionStatement, env: Environment): RuntimeValue {
        if (typeDefinition.parentType != null) {
            val parent = evaluateIdentifier(typeDefinition.parentType, env) as Tip
            typeDefinition.properties.addAll(0, parent.properties)
        }
        env.addTypeDefinition(typeDefinition)
        return Null()
    }
}