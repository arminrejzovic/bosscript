package interpreter

import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Number
import udemy.Text

class IdentifierTest {
    @Test
    fun testIdentifier(){
        val src = """
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 10.0
            )
        )

        assert(result == expectedResult)
    }
}