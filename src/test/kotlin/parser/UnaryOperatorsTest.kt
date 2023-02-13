package parser

import BinaryExpression
import Identifier
import NumericLiteral
import UnaryExpression
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Parser

class UnaryOperatorsTest {

    @Test
    fun testExponentiationPrecedence(){
        val src = """
            var z = -x * -10;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "z",
                        value = BinaryExpression(
                            left = UnaryExpression(
                                operator = "-",
                                argument = Identifier(
                                    symbol = "x"
                                )
                            ),
                            right = UnaryExpression(
                                operator = "-",
                                argument = NumericLiteral(
                                    value = 10.0
                                )
                            ),
                            operator = "*"
                        )
                    )
                ),
                isConstant = false
            )
        )

        assert(program.body == expectedResult)
    }
}