package parser

import BinaryExpression
import BlockStatement
import BreakStatement
import ForStatement
import Identifier
import IfStatement
import NumericLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class BreakTest {
    @Test
    fun testForLoopBreak(){
        val src = """
            za svako(x od 0 do 10){
                ako(x == 3){
                    prekid
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ForStatement(
                counter = Identifier(
                    symbol = "x"
                ),
                startValue = NumericLiteral(
                    value = 0.0
                ),
                endValue = NumericLiteral(
                    10.0
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        IfStatement(
                            condition = BinaryExpression(
                                left = Identifier(
                                    symbol = "x"
                                ),
                                right = NumericLiteral(
                                    value = 3.0
                                ),
                                operator = "=="
                            ),
                            consequent = BlockStatement(
                                body = arrayListOf(
                                    BreakStatement()
                                )
                            ),
                            alternate = null
                        )
                    )
                ),
                step = null
            )
        )
        assert(program.body == expectedResult)
    }
}