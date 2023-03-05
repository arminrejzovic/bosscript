package interpreter

import BooleanLiteral
import NumericLiteral
import StringLiteral
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Number

class LiteralsTest {
    @Test
    fun testStringLiteral(){
        val src = """
            "Test";
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Text(
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
            Number(
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
            Bool(
                value = true
            )
        )

        assert(result == expectedResult)
    }
}