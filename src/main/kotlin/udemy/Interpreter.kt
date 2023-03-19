package udemy

import ArrayLiteral
import AssignmentExpression
import BinaryExpression
import BlockStatement
import BooleanLiteral
import CallExpression
import DoWhileStatement
import Environment
import ForStatement
import FunctionDeclaration
import FunctionExpression
import Identifier
import IfStatement
import LogicalExpression
import MemberExpression
import NodeType
import NumericLiteral
import ObjectLiteral
import ReturnStatement
import Statement
import StringLiteral
import UnaryExpression
import UnlessStatement
import VariableDeclaration
import VariableStatement
import WhileStatement
import errors.SyntaxError
import isInteger
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

    private fun evaluate(node: Statement, environment: Environment = globalEnv): RuntimeValue{
        when(node.kind){
            // ---------------------------------------------------------------------------------------------------------
            // Literals
            NodeType.NumericLiteral -> {
                return Number(value = (node as NumericLiteral).value)
            }
            NodeType.StringLiteral -> {
                return Text(value = (node as StringLiteral).value)
            }
            NodeType.BooleanLiteral -> {
                return Bool(value = (node as BooleanLiteral).value)
            }
            NodeType.NullLiteral -> {
                return Null()
            }
            NodeType.ArrayLiteral -> {
                val arrayNode = node as ArrayLiteral
                val values = arrayNode.arr.map {
                    evaluate(it, environment)
                }
                return Array(
                    value = values as ArrayList<RuntimeValue>
                )
            }
            NodeType.Object -> {
                val objNode = node as ObjectLiteral
                val obj = Object(properties = hashMapOf())
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
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value = left.value as Double + right.value as Double
                        )
                    }
                    "-" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value = left.value as Double - right.value as Double
                        )
                    }
                    "*" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value = left.value as Double * right.value as Double
                        )
                    }
                    "/" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value = left.value as Double / right.value as Double
                        )
                    }
                    "%" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value = left.value as Double % right.value as Double
                        )
                    }
                    "^" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Number(
                            value =  (left.value as Double).pow(right.value as Double)
                        )
                    }
                    "<" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Bool(
                            value = (left.value as Double) < (right.value as Double)
                        )
                    }

                    "<=" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Bool(
                            value = (left.value as Double) <= (right.value as Double)
                        )
                    }

                    ">" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Bool(
                            value = (left.value as Double) > (right.value as Double)
                        )
                    }

                    ">=" -> {
                        if(left.value !is Double || right.value !is Double){
                            throw Exception("Type error: Operator '+' is not defined for provided operands")
                        }

                        return Bool(
                            value = (left.value as Double) >= (right.value as Double)
                        )
                    }

                    "==" -> {
                        return Bool(
                            value = left.value == right.value
                        )
                    }

                    "!=" -> {
                        return Bool(
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

            else -> {
                throw SyntaxError("Unexpected token, $node")
            }
        }
    }

    private fun evaluateUnaryExpression(expr: UnaryExpression, env: Environment): RuntimeValue {
        val arg = evaluate(expr.argument, env)

        // TODO handle ++ and -- (Should modify operand)

        when(expr.operator){
            "+" -> {
                if(arg is Text){
                    return Number(
                        value = arg.value.toDouble()
                    )
                }
                if(arg is Number){
                    return arg
                }
                throw Exception("Type error: Unary + is not defined for ${arg.javaClass.simpleName}")
            }
            "-" -> {
                if(arg is Text){
                    return Number(
                        value = -(arg.value.toDouble())
                    )
                }
                if(arg is Number){
                    return Number(
                        value = -arg.value
                    )
                }
                throw Exception("Type error: Unary - is not defined for ${arg.javaClass.simpleName}")
            }
            "++" -> {
                if(arg is Number){
                    arg.value++
                    return Number(
                        value = arg.value + 1
                    )
                }
                throw Exception("Type error: Increment operator is not defined for ${arg.javaClass.simpleName}")
            }
            "--" -> {
                if(arg is Number){
                    arg.value--
                    return Number(
                        value = arg.value - 1
                    )
                }
                throw Exception("Type error: Decrement operator is not defined for ${arg.javaClass.simpleName}")
            }
            else -> {
                throw Exception("Unexpected unary operator found: ${expr.operator}")
            }
        }
    }

    private fun evaluateLogicalExpression(expr: LogicalExpression, env: Environment): Bool {
        val left = evaluate(expr.left, env)
        val right = evaluate(expr.right, env)

        if(left.value !is Boolean || right.value !is Boolean){
            throw Exception("Type error: Logical expression operands must be booleans")
        }

        left as Bool
        right as Bool

        when(expr.operator){
            "&&" -> {
                return Bool(
                    value = left.value && right.value
                )
            }

            "||" -> {
                return Bool(
                    value = left.value || right.value
                )
            }

            else -> {
                throw Exception("Unrecognized logical operator")
            }
        }
    }

    private fun evaluateIdentifier(identifier: Identifier, environment: Environment): RuntimeValue {
        return environment.getVariable(identifier.symbol)
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

    private fun evaluateAssignmentExpression(expr: AssignmentExpression, env: Environment): RuntimeValue{
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
        if (target is Number && addedValue is Number){
            target.value %= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '%=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateDivisionAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Number && addedValue is Number){
            target.value /= addedValue.value
            return target
        }
        throw Exception("Type error: Operator '/=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateTimesAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue {
        val target = evaluate(expr.assignee, env)
        val addedValue = evaluate(expr.value, env)
        if (target is Number && addedValue is Number){
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
        if (target is Number && addedValue is Number){
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
        if (target is Number && addedValue is Number){
            target.value += addedValue.value
            return target
        }
        if(target is Text && addedValue is Text){
            target.value += addedValue
            return target
        }
        throw Exception("Type error: Operator '+=' is not defined for types ${target.javaClass.simpleName} and ${addedValue.javaClass.simpleName}")
    }

    private fun evaluateSimpleAssignment(expr: AssignmentExpression, env: Environment): RuntimeValue{
        if(expr.assignee is Identifier){
            return env.assignVariable(expr.assignee.symbol, evaluate(expr.value, env))
        }

        if(expr.assignee is MemberExpression){
            val target = evaluate(expr.assignee.targetObject, env)
            val newValue = evaluate(expr.value, env)

            if(target is Object){
                if(expr.assignee.property is Identifier){
                    // a.b.c = 10;
                    return target.setProperty(expr.assignee.property.symbol, newValue)
                }
                else{
                    // Computed value
                    // a.b["c"] = 10;
                    val prop = evaluate(expr.assignee.property, env)
                    if(prop is Text){
                        return target.setProperty(prop.value, evaluate(expr.value, env))
                    }
                    if(prop is Number){
                        throw Exception("${target.javaClass.simpleName} is not indexable")
                    }
                }
            }

            if(target is Array){
                val prop = evaluate(expr.assignee.property, env)
                if(prop is Number){
                    target.set(index = prop.value.toInt(), newValue)
                    return Null()
                }
            }
        }
        throw Exception("Invalid assignment operation ")
    }

    private fun evaluateBlockStatement(block: BlockStatement, env: Environment): RuntimeValue{
        val blockEnv = Environment(parent = env)
        var result: RuntimeValue = Null()
        for (stmt in block.body){
            if(result !is ReturnValue){
                val stmtResult = evaluate(stmt, blockEnv)
                if(stmtResult is ReturnValue){
                    result = stmtResult
                }
            }
        }
        return result
    }

    private fun evaluateFunctionDeclaration(declaration: FunctionDeclaration, env: Environment): Function{
        val fn = Function(
            name = declaration.name.symbol,
            params = declaration.params,
            returnType = declaration.returnType,
            body = declaration.body,
            parentEnv = env
        )

        env.declareVariable(name = declaration.name.symbol, fn)

        return fn
    }

    private fun evaluateFunctionExpression(expr: FunctionExpression, env: Environment): Function {
        return Function(
            name = "",
            params = expr.params,
            returnType = expr.returnType,
            body = expr.body,
            parentEnv = env
        )
    }

    private fun evaluateFunctionCall(call: CallExpression, env: Environment): RuntimeValue{
        when (val fn = evaluate(call.callee, env)) {
            is Function -> {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                fn.params.forEachIndexed{index, param ->
                    activationRecord[param.identifier.symbol] = evaluate(call.args[index], env)
                }
                val functionEnv = Environment(parent = env, variables = activationRecord)
                val functionResult = evaluateBlockStatement(fn.body, functionEnv)
                return if(functionResult is ReturnValue) functionResult.value else functionResult
            }

            is NativeFunction -> {
                val args = arrayListOf<RuntimeValue>()
                call.args.forEach {
                    args.add(evaluate(it, env))
                }
                return fn.call(*(args.toTypedArray()))
            }

            else -> {
                throw Exception("Is not a function")
            }
        }
    }

    private fun evaluateIfStatement(stmt: IfStatement, env: Environment): RuntimeValue{
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

    private fun evaluateUnlessStatement(stmt: UnlessStatement, env: Environment): RuntimeValue{
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
                i += step
                loopEnv.assignVariable(stmt.counter.symbol, Number(value = i))
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
                i -= step
                loopEnv.assignVariable(stmt.counter.symbol, Number(value = i))
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
        }
        return null
    }

    private fun evaluateDoWhileStatement(stmt: DoWhileStatement, env: Environment): RuntimeValue? {
        do{
            val iterationResult = evaluate(stmt.body, env)
            if(iterationResult is ReturnValue){
                return iterationResult
            }
        } while (evaluate(stmt.condition, env).value == true)

        return null
    }

    private fun evaluateReturnStatement(stmt: ReturnStatement, env: Environment): ReturnValue{
        if(stmt.argument == null){
            // void return
            return ReturnValue(value = Null())
        }
        return ReturnValue(value = evaluate(stmt.argument, env))
    }

    private fun evaluateMemberExpression(expr: MemberExpression, env: Environment): RuntimeValue{
        val target = evaluate(expr.targetObject, env)
        if(expr.isComputed){
            when(val prop = evaluate(expr.property)){
                is Text -> {
                    // obj["hello"];
                    return target.getProperty(prop.value)
                }
                is Number -> {
                    // obj[1];
                    if(!prop.value.isInteger()){
                        throw Exception("Index must be an integer")
                    }

                    if (target is Array){
                        return target.getElement(prop.value.toInt())
                    }
                    else{
                        throw Exception("${target.javaClass.simpleName} is not indexable")
                    }
                }
                else -> {
                    throw Exception("Type ${prop.javaClass} cannot be used as an index type")
                }
            }
        }
        else{
            val propertyName = expr.property as Identifier
            return target.getProperty(propertyName.symbol)
        }
    }
}

// TODO Extract methods from evaluate