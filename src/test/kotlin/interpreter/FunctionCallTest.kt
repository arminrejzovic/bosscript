package interpreter

import BinaryExpression
import BlockStatement
import BooleanLiteral
import CallExpression
import FunctionParameter
import Identifier
import MemberExpression
import NumericLiteral
import org.junit.jupiter.api.Test
import udemy.Function
import udemy.Interpreter
import udemy.Number
import udemy.Parser

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
}