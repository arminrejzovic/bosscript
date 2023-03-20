package parser

import org.junit.jupiter.api.Test

class MathTest {
    @Test
    fun testSimpleAddition(){
        val src = """
            10 + 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(left= NumericLiteral(value=10.0), right= NumericLiteral(value=8.0), operator="+")
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleSubtraction(){
        val src = """
            10 - 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(left= NumericLiteral(value=10.0), right= NumericLiteral(value=8.0), operator="-")
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleMultiplication(){
        val src = """
            10 * 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(left= NumericLiteral(value=10.0), right= NumericLiteral(value=8.0), operator="*")
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleDivision(){
        val src = """
            10 / 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(left= NumericLiteral(value=10.0), right= NumericLiteral(value=8.0), operator="/")
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testMultiplicationPrecedence(){
        val src = """
            10 * 5 + 8;
            10 + 5 * 8;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(
                left= BinaryExpression(
                    left= NumericLiteral(value=10.0),
                    right= NumericLiteral(value=5.0),
                    operator="*"
                ),
                right= NumericLiteral(value=8.0),
                operator="+"
            ),
            BinaryExpression(
                left= NumericLiteral(value=10.0),
                right= BinaryExpression(
                    left= NumericLiteral(value=5.0),
                    right= NumericLiteral(value=8.0),
                    operator="*"
                ),
                operator="+"
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testExponentiationPrecedence(){
        val src = """
            10 * 5 ^ (10-3);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(
                left= NumericLiteral(value=10.0),
                right= BinaryExpression(
                    left= NumericLiteral(value=5.0),
                    right= BinaryExpression(
                        left = NumericLiteral(
                            value = 10.0
                        ),
                        right = NumericLiteral(
                            value = 3.0
                        ),
                        operator = "-"
                    ),
                    operator="^"
                ),
                operator="*"
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }
}