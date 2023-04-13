package interpreter

import parser.Parser
import errors.SyntaxError
import interpreter.values.*
import isInt
import parser.*
import java.io.File
import kotlin.math.pow

class Interpreter {
    private val globalEnv = Environment()
    private val parser = Parser()

    fun evaluateProgram(src: String): ArrayList<RuntimeValue>{
        val program = parser.parseProgram(src)
        val result = arrayListOf<RuntimeValue>()
        program.body.forEach {
            result.add(evaluate(it))
        }
        return result
    }

    private fun evaluate(node: Statement, environment: Environment = globalEnv): RuntimeValue {
        when(node.kind){
            // ---------------------------------------------------------------------------------------------------------
            // Literals
            NodeType.NumericLiteral -> {
                return Broj(value = (node as NumericLiteral).value)
            }
            NodeType.StringLiteral -> {
                return Tekst(value = (node as StringLiteral).value)
            }
            NodeType.BooleanLiteral -> {
                return Logicki(value = (node as BooleanLiteral).value)
            }
            NodeType.NullLiteral -> {
                return Null()
            }
            NodeType.ArrayLiteral -> {
                val arrayNode = node as ArrayLiteral
                val values = arrayNode.arr.map {
                    evaluate(it, environment)
                }
                return Niz(
                    value = values as ArrayList<RuntimeValue>
                )
            }
            NodeType.Object -> {
                val objNode = node as ObjectLiteral
                val obj = Objekat(properties = hashMapOf())
                objNode.properties.forEach {
                    obj.properties[it.key] = evaluate(it.value, environment)
                }
                return obj
            }
            // ---------------------------------------------------------------------------------------------------------

            NodeType.BinaryExpression -> {
                val binExpNode = node as BinaryExpression
                val left = evaluate(binExpNode.left, environment)
                val right = evaluate(binExpNode.right, environment)
                when(binExpNode.operator){
                    "+" -> {
                        if(left.value is Double && right.value is Double){
                            return Broj(
                                value = left.value as Double + right.value as Double
                            )
                        }
                        if(left.value is String && right.value is String){
                            return Tekst(
                                value = left.value as String + right.value as String
                            )
                        }

                        throw Exception("Type error: Operator '+' is not defined for ${left.javaClass.simpleName} and ${right.javaClass.simpleName}")
                    }
                    "-" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Broj(
                            value = left.value as Double - right.value as Double
                        )
                    }
                    "*" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Broj(
                            value = left.value as Double * right.value as Double
                        )
                    }
                    "/" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Broj(
                            value = left.value as Double / right.value as Double
                        )
                    }
                    "%" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Broj(
                            value = left.value as Double % right.value as Double
                        )
                    }
                    "^" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Broj(
                            value = (left.value as Double).pow(right.value as Double)
                        )
                    }
                    "<" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Logicki(
                            value = (left.value as Double) < (right.value as Double)
                        )
                    }

                    "<=" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Logicki(
                            value = (left.value as Double) <= (right.value as Double)
                        )
                    }

                    ">" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Logicki(
                            value = (left.value as Double) > (right.value as Double)
                        )
                    }

                    ">=" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Logicki(
                            value = (left.value as Double) >= (right.value as Double)
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

                    else -> {
                        throw NotImplementedError("Unsupported operator")
                    }
                }
            }

            NodeType.UnaryExpression -> {
                return evaluateUnaryExpression(node as UnaryExpression, environment)
            }

            NodeType.Identifier -> {
                return evaluateIdentifier(node as Identifier, environment)
            }

            NodeType.VariableStatement -> {
                evaluateVariableStatement(node as VariableStatement, environment)
                return Null()
            }

            NodeType.AssignmentExpression -> {
                return evaluateAssignmentExpression(node as AssignmentExpression,  environment)
            }

            NodeType.Block -> {
                return evaluateBlockStatement(node as BlockStatement, environment)
            }

            NodeType.FunctionDeclaration -> {
                return evaluateFunctionDeclaration(node as FunctionDeclaration, environment)
            }

            NodeType.FunctionExpression -> {
                return evaluateFunctionExpression(node as FunctionExpression, environment)
            }

            NodeType.ReturnStatement -> {
                return evaluateReturnStatement(node as ReturnStatement, environment)
            }

            NodeType.BreakStatement -> {
                return evaluateBreakStatement(node as BreakStatement, environment)
            }

            NodeType.IfStatement -> {
                return evaluateIfStatement(node as IfStatement, environment)
            }

            NodeType.UnlessStatement -> {
                return evaluateUnlessStatement(node as UnlessStatement, environment)
            }

            NodeType.CallExpression -> {
                return evaluateFunctionCall(node as CallExpression, environment)
            }

            NodeType.ForStatement -> {
                return evaluateForStatement(node as ForStatement, environment) ?: Null()
            }

            NodeType.WhileStatement -> {
                return evaluateWhileStatement(node as WhileStatement, environment) ?: Null()
            }

            NodeType.DoWhileStatement -> {
                return evaluateDoWhileStatement(node as DoWhileStatement, environment) ?: Null()
            }

            NodeType.MemberExpression -> {
                return evaluateMemberExpression(node as MemberExpression, environment)
            }

            NodeType.LogicalExpression -> {
                return evaluateLogicalExpression(node as LogicalExpression, environment)
            }

            NodeType.ImportStatement -> {
                return evaluateImportStatement(node as ImportStatement, environment)
            }

            NodeType.ModelDefinition -> {
                return evaluateModelDefinition(node as ModelDefinitionStatement, environment)
            }

            else -> {
                throw SyntaxError("Unexpected token, $node")
            }
        }
    }

    private fun evaluatePackage(src: String, env: Environment){
        val program = parser.parseProgram(src)
        program.body.forEach {
            evaluate(it, env)
        }
    }

    private fun evaluateImportStatement(stmt: ImportStatement, env: Environment): RuntimeValue {
        val stdlibPackage = stdlib[stmt.packageName]
        if(stdlibPackage != null){
            evaluateImportStdlibPackage(stdlibPackage, env, stmt.imports)
        }
        else{
            evaluateImportUserPackage(stmt.packageName, stmt.imports, env)
        }

        return Null()
    }

    private fun evaluateImportUserPackage(packageName: String, imports: ArrayList<Identifier>?, env: Environment) {
        val pckg = File(packageName)
        if (!pckg.exists()) {
            throw Exception("Package $packageName does not exist")
        }

        val tempEnv = Environment()
        evaluatePackage(pckg.readText(), tempEnv)
        if (imports == null) {
            // Full import
            env.importEnv(tempEnv)
        }
        else {
            imports.forEach {
                val packageElement = tempEnv.getVariable(it.symbol)
                env.declareVariable(it.symbol, packageElement)
            }
        }
    }

    private fun evaluateImportStdlibPackage(target: Environment, env: Environment, imports: ArrayList<Identifier>?) {
        if(imports == null){
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
        val arg = evaluate(expr.argument, env)

        when(expr.operator){
            "+" -> {
                if(arg is Tekst){
                    return Broj(
                        value = arg.value.toDouble()
                    )
                }
                if(arg is Broj){
                    return arg
                }
                throw Exception("Type error: Unary + is not defined for ${arg.javaClass.simpleName}")
            }
            "-" -> {
                if(arg is Tekst){
                    return Broj(
                        value = -(arg.value.toDouble())
                    )
                }
                if(arg is Broj){
                    return Broj(
                        value = -arg.value
                    )
                }
                throw Exception("Type error: Unary - is not defined for ${arg.javaClass.simpleName}")
            }
            "++" -> {
                if(arg is Broj){
                    arg.value++
                    return Broj(
                        value = arg.value + 1
                    )
                }
                throw Exception("Type error: Increment operator is not defined for ${arg.javaClass.simpleName}")
            }
            "--" -> {
                if(arg is Broj){
                    arg.value--
                    return Broj(
                        value = arg.value - 1
                    )
                }
                throw Exception("Type error: Decrement operator is not defined for ${arg.javaClass.simpleName}")
            }

            "!" -> {
                if(arg is Logicki){
                    return Logicki(
                        value = !arg.value
                    )
                }
                return Logicki(
                    value = arg is Null
                )
            }

            else -> {
                throw Exception("Unexpected unary operator found: ${expr.operator}")
            }
        }
    }

    private fun evaluateLogicalExpression(expr: LogicalExpression, env: Environment): Logicki {
        val left = evaluate(expr.left, env)
        val right = evaluate(expr.right, env)

        if(left.value !is Boolean || right.value !is Boolean){
            throw Exception("Type error: Logical expression operands must be booleans")
        }

        left as Logicki
        right as Logicki

        when(expr.operator){
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
                throw Exception("Unrecognized logical operator")
            }
        }
    }

    private fun evaluateIdentifier(identifier: Identifier, environment: Environment): RuntimeValue {
        val model = environment.resolveModelDefinition(identifier.symbol)
        return model ?: environment.getVariable(identifier.symbol)
    }

    private fun evaluateVariableStatement(stmt: VariableStatement, env: Environment){
        stmt.declarations.forEach {
            evaluateVariableDeclaration(it, env,  stmt.isConstant)
        }
    }

    private fun evaluateVariableDeclaration(declaration: VariableDeclaration, env: Environment, isConstant: Boolean) {
        val value = if (declaration.value == null) Null() else evaluate(declaration.value, env)

        env.declareVariable(declaration.identifier, value, isConstant)
    }

    private fun evaluateAssignmentExpression(expr: AssignmentExpression, env: Environment): RuntimeValue {
        if (expr.assignee.kind != NodeType.Identifier && expr.assignee.kind != NodeType.MemberExpression){
            throw Exception("Invalid assignment target: ${expr.assignee}")
        }

        return when(expr.assignmentOperator){
            "="  -> evaluateSimpleAssignment(expr, env)
            "+=" -> evaluatePlusAssignment(expr, env)
            "-=" -> evaluateMinusAssignment(expr, env)
            "*=" -> evaluateTimesAssignment(expr, env)
            "/=" -> evaluateDivisionAssignment(expr, env)
            "%=" -> evaluateRemainderAssignment(expr, env)
            else -> throw Exception("Invalid assignment operator ${expr.assignmentOperator}")
        }
    }

    private fun evaluateRemainderAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj){
            target.value %= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '%=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateDivisionAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj){
            target.value /= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '/=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateTimesAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj){
            target.value *= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '*=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    /**
     * Evaluates -= operator
     */
    private fun evaluateMinusAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj){
            target.value -= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '-=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    /**
     * Evaluates += operator
     */
    private fun evaluatePlusAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Broj && addedValue is Broj){
            target.value += addedValue.value
            return target
        }
        if(target is Tekst && addedValue is Tekst){
            target.value += addedValue
            return target
        }
        throw Exception("Type error: Operator '+=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateSimpleAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        if(expr.assignee is Identifier){
            return env.assignVariable(expr.assignee.symbol, evaluate(expr.value, env))
        }

        if(expr.assignee is MemberExpression){
            val target = evaluate(expr.assignee.targetObject, env)
            val newValue = evaluate(expr.value, env)

            if(target is Objekat){
                if(expr.assignee.property is Identifier){
                    // a.b.c = 10;
                    return target.setProperty(expr.assignee.property.symbol, newValue)
                }
                else{
                    // Computed value
                    // a.b["c"] = 10;
                    val prop = evaluate(expr.assignee.property, env)
                    if(prop is Tekst){
                        return target.setProperty(prop.value, evaluate(expr.value, env))
                    }
                    if(prop is Broj){
                        throw Exception("${target.javaClass.simpleName} is not indexable")
                    }
                }
            }

            if(target is Niz){
                val prop = evaluate(expr.assignee.property, env)
                if(prop is Broj){
                    target.set(index = prop.value.toInt(), newValue)
                    return Null()
                }
            }
        }
        throw Exception("Invalid assignment operation ")
    }

    private fun evaluateBlockStatement(block: BlockStatement, env: Environment): RuntimeValue {
        val blockEnv = Environment(parent = env)
        var result: RuntimeValue = Null()
        for (stmt in block.body){
            if(result !is ReturnValue && result !is BreakValue){
                val stmtResult = evaluate(stmt, blockEnv)
                if(stmtResult is ReturnValue || stmtResult is BreakValue){
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
            name = "",
            params = expr.params,
            returnType = expr.returnType,
            body = expr.body,
            parentEnv = env
        )
    }

    private fun evaluateFunctionCall(call: CallExpression, env: Environment): RuntimeValue {
        when (val fn = evaluate(call.callee, env)) {
            is Funkcija -> {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                fn.params.forEachIndexed{index, param ->
                    activationRecord[param.identifier.symbol] = evaluate(call.args[index], env)
                }
                val functionEnv = Environment(parent = env, variables = activationRecord)
                val functionResult = evaluateBlockStatement(fn.body, functionEnv)
                return if(functionResult is ReturnValue) functionResult.value else functionResult
            }

            is NativeFunkcija -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }
                return fn.call(*(args.toTypedArray()))
            }

            is Model -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }

                return fn.constructor(args, env)
            }

            else -> {
                throw Exception("Is not a function")
            }
        }
    }

    private fun evaluateIfStatement(stmt: IfStatement, env: Environment): RuntimeValue {
        val condition = evaluate(stmt.condition, env)
        if(condition.value !is Boolean){
            throw Exception("Type Error: Condition is not a boolean")
        }

        var result: RuntimeValue = Null()

        if (condition.value == true){
            result = evaluate(stmt.consequent, env)
        }
        else if (stmt.alternate != null) {
            result = evaluate(stmt.alternate, env)
        }

        return result
    }

    private fun evaluateUnlessStatement(stmt: UnlessStatement, env: Environment): RuntimeValue {
        val condition = evaluate(stmt.condition, env)
        if(condition.value !is Boolean){
            throw Exception("Type Error: Condition is not a boolean")
        }

        var result: RuntimeValue = Null()

        if (condition.value == false){
            result = evaluate(stmt.consequent, env)
        }
        else if (stmt.alternate != null) {
            result = evaluate(stmt.alternate, env)
        }

        return result
    }

    private fun evaluateForStatement(stmt: ForStatement, env: Environment): ReturnValue?{
        val startValue = evaluate(stmt.startValue, env)
        val endValue = evaluate(stmt.endValue, env)

        if(startValue.value !is Double || endValue.value !is Double){
            throw Exception("Type Error: For loop bounds must be numbers")
        }

        val start = startValue.value as Double
        val end = endValue.value as Double

        val step: Double
        if(stmt.step == null){
            // Infer the step if not provided
            step = 1.0
        }
        else{
            // Otherwise interpret the provided step
            val stepVal = evaluate(stmt.step, env)
            if(stepVal.value !is Double){
                throw Exception("Type Error: For loop step must be a number")
            }
            step = stepVal.value as Double
        }


        val activationRecord = hashMapOf<String, RuntimeValue>()
        activationRecord[stmt.counter.symbol] = startValue
        val loopEnv = Environment(variables = activationRecord, parent =  env)

        if (start < end){
            // Regular ascending loop
            var i = start
            while (i < end) {
                val iterationResult = evaluate(stmt.body, loopEnv)
                if(iterationResult is ReturnValue){
                    return iterationResult
                }
                if(iterationResult is BreakValue){
                    return null
                }
                i += step
                loopEnv.assignVariable(stmt.counter.symbol, Broj(value = i))
            }
        }
        else{
            // Backward loop
            var i = start
            while (i > end) {
                val iterationResult = evaluate(stmt.body, loopEnv)
                if(iterationResult is ReturnValue){
                    return iterationResult
                }
                if(iterationResult is BreakValue){
                    return null
                }
                i -= step
                loopEnv.assignVariable(stmt.counter.symbol, Broj(value = i))
            }
        }
        return null
    }

    private fun evaluateWhileStatement(stmt: WhileStatement, env: Environment): RuntimeValue? {
        while (evaluate(stmt.condition, env).value == true){
            val iterationResult = evaluate(stmt.body, env)
            if(iterationResult is ReturnValue){
                return iterationResult
            }
            if(iterationResult is BreakValue){
                return null
            }
        }
        return null
    }

    private fun evaluateDoWhileStatement(stmt: DoWhileStatement, env: Environment): RuntimeValue? {
        do{
            val iterationResult = evaluate(stmt.body, env)
            if(iterationResult is ReturnValue){
                return iterationResult
            }
            if(iterationResult is BreakValue){
                return null
            }
        } while (evaluate(stmt.condition, env).value == true)

        return null
    }

    private fun evaluateReturnStatement(stmt: ReturnStatement, env: Environment): ReturnValue {
        if(stmt.argument == null){
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
        if(expr.isComputed){
            when(val prop = evaluate(expr.property, env)){
                is Tekst -> {
                    // obj["hello"];
                    return target.getProperty(prop.value)
                }
                is Broj -> {
                    // obj[1];
                    if(!prop.value.isInt()){
                        throw Exception("Index must be an integer")
                    }

                    if (target is Niz){
                        return target.getElement(prop.value.toInt())
                    }
                    else{
                        throw Exception("${target.javaClass.simpleName} is not indexable")
                    }
                }
                else -> {
                    throw Exception("Type ${prop.javaClass.simpleName} cannot be used as an index type")
                }
            }
        }
        else{
            val propertyName = expr.property as Identifier
            return target.getProperty(propertyName.symbol)
        }
    }

    private fun evaluateModelDefinition(modelDefinition: ModelDefinitionStatement, env: Environment): RuntimeValue{
        env.addModelDefinition(modelDefinition)
        return Null()
    }
}

// TODO Extract methods from evaluate