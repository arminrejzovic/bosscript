package parser

import BinaryExpression
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Parser

class VariableDeclarationTest {
    @Test
    fun testSingleInitializedVarDeclaration() {
        val src = """
            var x = 10;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "x", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSingleUninitializedVarDeclaration() {
        val src = """
            var x;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "x", value = null)
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testDoubleInitializedVarDeclaration() {
        val src = """
            var x = 10, y = 5;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "x", value = NumericLiteral(value = 10.0)),
                    VariableDeclaration(identifier = "y", value = NumericLiteral(value = 5.0))
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testDoubleUninitializedVarDeclaration() {
        val src = """
            var x, y;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "x", value = null),
                    VariableDeclaration(identifier = "y", value = null)
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSingleKonstDeclaration() {
        val src = """
            konst pi = 3.14;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "pi", value = NumericLiteral(value = 3.14))
                ),
                isConstant = true
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testComplexVarDeclaration() {
        val src = """
            var x = 10 + 5 * 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = BinaryExpression(
                            left = NumericLiteral(value = 10.0),
                            right = BinaryExpression(
                                left = NumericLiteral(value = 5.0),
                                right = NumericLiteral(value = 8.0),
                                operator = "*"
                            ),
                            operator = "+"
                        )
                    )
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testVariableNameRules() {
        val src = """
            var x = 10;
            var snake_case = 10;
            var ${'$'}var = 10;
            var longVariableName123 = 10;
            var camelCase = 10;
            var PascalCase = 10;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "x", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "snake_case", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "${'$'}var", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "longVariableName123", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "camelCase", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(identifier = "PascalCase", value = NumericLiteral(value = 10.0)),
                ),
                isConstant = false
            ),
        )

        assert(program.body == expectedResult)
    }
}