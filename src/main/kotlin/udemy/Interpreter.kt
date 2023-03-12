package udemy

import ArrayLiteral
import AssignmentExpression
import BinaryExpression
import BlockStatement
import BooleanLiteral
import CallExpression
import Environment
import FunctionDeclaration
import FunctionExpression
import Identifier
import IfStatement
import MemberExpression
import NodeType
import NumericLiteral
import ObjectLiteral
import ReturnStatement
import Statement
import StringLiteral
import UnlessStatement
import VariableDeclaration
import VariableStatement
import errors.SyntaxError
import kotlin.Exception
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

    init {
        globalEnv.declareVariable("x", Number(value = 10.0))
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

            NodeType.Identifier -> {
                val identifierNode = node as Identifier
                return environment.getVariable(identifierNode.symbol)
            }

            NodeType.VariableStatement -> {
                evaluateVariableStatement(node as VariableStatement, environment)
                return Null()
            }

            NodeType.AssignmentExpression -> {
                evaluateAssignmentExpression(node as AssignmentExpression, environment)
                return Null()
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
                val returnNode = node as ReturnStatement
                return if(returnNode.argument != null){
                    evaluate(returnNode.argument, environment)
                } else{
                    Null()
                }
            }

            NodeType.IfStatement -> {
                evaluateIfStatement(node as IfStatement, environment)
                return Null()
            }

            NodeType.UnlessStatement -> {
                evaluateUnlessStatement(node as UnlessStatement, environment)
                return Null()
            }

            NodeType.CallExpression -> {
                return evaluateFunctionCall(node as CallExpression, environment)
            }


            else -> {
                throw SyntaxError("Unexpected token, $node")
            }
        }
    }

    /* HELPER METHODS */

    private fun evaluateVariableStatement(stmt: VariableStatement, env: Environment){
        stmt.declarations.forEach {
            evaluateVariableDeclaration(it, env,  stmt.isConstant)
        }
    }

    private fun evaluateVariableDeclaration(declaration: VariableDeclaration, env: Environment, isConstant: Boolean) {
        val value = if (declaration.value == null) Null() else evaluate(declaration.value, env)

        env.declareVariable(declaration.identifier, value, isConstant)
    }

    private fun evaluateAssignmentExpression(expr: AssignmentExpression, env: Environment){
        if (expr.assignee.kind != NodeType.Identifier){
            throw Exception("Invalid assignment target")
        }
        env.assignVariable((expr.assignee as Identifier).symbol, evaluate(expr.value, env))
    }

    private fun evaluateBlockStatement(block: BlockStatement, env: Environment): RuntimeValue{
        val blockEnv = Environment(parent = env)
        var result: RuntimeValue = Null()
        block.body.forEach {
            if(it.kind == NodeType.ReturnStatement){
                return evaluate(it, blockEnv)
            }
            result = evaluate(it, blockEnv)
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
        val fn = evaluate(call.callee, env)
        if(fn !is Function){
            throw Exception("Is not a function!")
        }
        val activationRecord = hashMapOf<String, RuntimeValue>()
        fn.params.forEachIndexed{index, param ->
            activationRecord[param.identifier.symbol] = evaluate(call.args[index], env)
        }
        println(activationRecord)
        val functionEnv = Environment(parent = env, variables = activationRecord)

        return evaluateBlockStatement(fn.body, functionEnv)
    }

    private fun evaluateIfStatement(stmt: IfStatement, env: Environment){
        val condition = evaluate(stmt.condition, env)
        if(condition.value !is Boolean){
            throw Exception("Type Error: Condition is not a boolean")
        }

        if (condition.value == true){
            evaluate(stmt.consequent, env)
        }
        else if (stmt.alternate != null) {
            evaluate(stmt.alternate, env)
        }
    }

    private fun evaluateUnlessStatement(stmt: UnlessStatement, env: Environment){
        val condition = evaluate(stmt.condition, env)
        if(condition.value !is Boolean){
            throw Exception("Type Error: Condition is not a boolean")
        }

        if (condition.value == false){
            evaluate(stmt.consequent, env)
        }
        else if (stmt.alternate != null) {
            evaluate(stmt.alternate, env)
        }
    }

    private fun evaluateMemberExpression(expr: MemberExpression, env: Environment): RuntimeValue{
        val target = evaluate(expr.targetObject, env)
        val property = evaluate(expr.property, env)
        TODO()
    }
}