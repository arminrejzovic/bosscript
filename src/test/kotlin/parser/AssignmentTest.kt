package parser

import org.junit.jupiter.api.Test

class AssignmentTest {
    @Test
    fun testVariableDeclaration(){
        val src = """
            var x = 10 + 5 * 8;
            x = 61;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations= arrayListOf(
                    VariableDeclaration(
                        identifier="x",
                        value= BinaryExpression(
                            left= NumericLiteral(value=10.0),
                            right= BinaryExpression(
                                left= NumericLiteral(value=5.0),
                                right= NumericLiteral(value=8.0),
                                operator="*"
                            ),
                            operator="+"
                        )
                    )
                ),
                isConstant=false
            ),
            AssignmentExpression(
                assignee = Identifier(symbol = "x"),
                value = NumericLiteral(value = 61.0),
                assignmentOperator = "="
            )
        )
        assert(program.body == expectedResult)
    }
}