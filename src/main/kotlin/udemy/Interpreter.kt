package udemy

import ArrayLiteral
import BinaryExpression
import BooleanLiteral
import Environment
import Identifier
import NodeType
import NumericLiteral
import ObjectLiteral
import Statement
import StringLiteral
import VariableDeclaration
import VariableStatement
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
                parseVariableStatement(node as VariableStatement, environment)
                return Null()
            }

            else -> {
                throw SyntaxError("Unexpected token, $node")
            }
        }
    }

    /* HELPER METHODS */

    private fun parseVariableStatement(stmt: VariableStatement, env: Environment){
        stmt.declarations.forEach {
            parseVariableDeclaration(it, env,  stmt.isConstant)
        }
    }

    private fun parseVariableDeclaration(declaration: VariableDeclaration, env: Environment, isConstant: Boolean) {
        val value = if (declaration.value == null) Null() else evaluate(declaration.value, env)

        env.declareVariable(declaration.identifier, value, isConstant)
    }
}