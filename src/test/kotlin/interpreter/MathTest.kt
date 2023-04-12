package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*

class MathTest {
    @Test
    fun testSimpleAddition(){
        val src = """
            10 + 8;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 18.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testSimpleSubtraction(){
        val src = """
            10 - 8;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 2.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testSimpleMultiplication(){
        val src = """
            10 * 8;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 80.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testSimpleDivision(){
        val src = """
            10 / 8;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 1.25
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testMultiplicationPrecedence(){
        val src = """
            10 * 5 + 8;
            10 + 5 * 8;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 58.0
            ),
            Broj(
                value = 50.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testExponentiationPrecedence(){
        val src = """
            10 * 5 ^ (10-3);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 781250.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testModulo(){
        val src = """
            10 % 3;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 1.0
            )
        )

        assert(result == expectedResult)
    }
}