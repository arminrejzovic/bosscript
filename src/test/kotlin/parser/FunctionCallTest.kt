package parser

import BooleanLiteral
import CallExpression
import Identifier
import MemberExpression
import NumericLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class FunctionCallTest {
    @Test
    fun testSimpleCall(){
        val src = """
            random();
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = Identifier(
                    symbol = "random"
                ),
                args = arrayListOf()
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testCallOneArg(){
        val src = """
            random(10);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = Identifier(
                    symbol = "random"
                ),
                args = arrayListOf(
                    NumericLiteral(
                        value = 10.0
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testCallMultiArg(){
        val src = """
            random(10,x,netacno);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = Identifier(
                    symbol = "random"
                ),
                args = arrayListOf(
                    NumericLiteral(
                        value = 10.0
                    ),
                    Identifier(
                        symbol = "x"
                    ),
                    BooleanLiteral(
                        value = false
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testCallObjectFunction(){
        val src = """
            math.random(10,x,netacno);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = MemberExpression(
                    isComputed = false,
                    targetObject = Identifier(
                        symbol = "math"
                    ),
                    property = Identifier(
                        symbol = "random"
                    )
                ),
                args = arrayListOf(
                    NumericLiteral(
                        value = 10.0
                    ),
                    Identifier(
                        symbol = "x"
                    ),
                    BooleanLiteral(
                        value = false
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testChainedCall(){
        val src = """
            lambda(tacno)();
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = CallExpression(
                    callee = Identifier(
                        symbol = "lambda"
                    ),
                    args = arrayListOf(
                        BooleanLiteral(
                            value = true
                        )
                    )
                ),
                args = arrayListOf()
            )
        )
        assert(program.body == expectedResult)
    }
}