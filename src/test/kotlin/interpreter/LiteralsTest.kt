package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*

class LiteralsTest {
    @Test
    fun testStringLiteral(){
        val src = """
            "Test";
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Tekst(
                value = "Test"
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testNumericLiteral(){
        val src = """
            6969;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 6969.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testBooleanLiteral(){
        val src = """
            tacno;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Logicki(
                value = true
            )
        )

        assert(result == expectedResult)
    }
}