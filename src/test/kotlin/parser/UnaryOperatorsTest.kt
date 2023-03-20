package parser

import org.junit.jupiter.api.Test

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