package interpreter

import BinaryExpression
import BlockStatement
import BooleanLiteral
import CallExpression
import FunctionParameter
import Identifier
import MemberExpression
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Function
import udemy.Number

class FunctionCallTest {
    @Test
    fun testFunctionUntypedParam() {
        val src = """
            funkcija xf(a){
                a + a;
            }
            xf(5);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Function(
                name = "xf",
                params = arrayListOf(
                    FunctionParameter(
                        Identifier(symbol = "a"),
                        type = null
                    )
                ),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf(
                        BinaryExpression(
                            left = Identifier(
                                symbol = "a"
                            ),
                            right = Identifier(
                                symbol = "a"
                            ),
                            operator = "+"
                        )
                    )
                ),
                parentEnv = null
            ),
            Number(
                value = 10.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testClosureCall() {
        val src = """
            funkcija xf(a){
                var x = 69;
                a + x;
            }
            xf(5);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Function(
                name = "xf",
                params = arrayListOf(
                    FunctionParameter(
                        Identifier(symbol = "a"),
                        type = null
                    )
                ),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "x",
                                    value = NumericLiteral(
                                        value = 69.0
                                    )
                                )
                            )
                        ),
                        BinaryExpression(
                            left = Identifier(
                                symbol = "a"
                            ),
                            right = Identifier(
                                symbol = "x"
                            ),
                            operator = "+"
                        )
                    )
                ),
                parentEnv = null
            ),
            Number(
                value = 74.0
            ),
            Number(
                value = 10.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testParamWithSameNameAsGlobalVar() {
        val src = """
            funkcija xf(x){
                5 + x;
            }
            xf(5);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Function(
                name = "xf",
                params = arrayListOf(
                    FunctionParameter(
                        Identifier(symbol = "x"),
                        type = null
                    )
                ),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf(
                        BinaryExpression(
                            left = NumericLiteral(
                                value = 5.0
                            ),
                            right = Identifier(
                                symbol = "x"
                            ),
                            operator = "+"
                        )
                    )
                ),
                parentEnv = null
            ),
            Number(
                value = 10.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testReturn() {
        val src = """
            funkcija xf(x){
                vrati 5 + x;
                x;
            }
            var a = xf(5);
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
                value = 10.0
            )

        assert(result.last() == expectedResult)
    }
}
