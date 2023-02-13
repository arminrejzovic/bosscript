package parser

import BinaryExpression
import BooleanLiteral
import Identifier
import NullLiteral
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import tokenize
import udemy.Parser

class EqualityTest {
    @Test
    fun testEqualityAssignment() {
        val src = """
            var x = y != z;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = BinaryExpression(
                            left = Identifier(
                                symbol = "y"
                            ),
                            right = Identifier(
                                symbol = "z"
                            ),
                            operator = "!="
                        )
                    )
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testBooleanEqualityComparison() {
        val src = """
            var x = y != nedefinisano;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = BinaryExpression(
                            left = Identifier(
                                symbol = "y"
                            ),
                            right = NullLiteral(),
                            operator = "!="
                        )
                    )
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }
}