package udemy

import ArrayLiteral
import BinaryExpression
import BooleanLiteral
import Environment
import NodeType
import NumericLiteral
import ObjectLiteral
import Statement
import StringLiteral
import errors.SyntaxError
import java.lang.Exception
import kotlin.math.exp
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
                    obj.properties[it.key] = evaluate(it, environment)
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
                    else -> {
                        throw NotImplementedError("Unsupported operator")
                    }
                }
            }

            else -> {
                throw SyntaxError("Unexpected token")
            }
        }
    }
}