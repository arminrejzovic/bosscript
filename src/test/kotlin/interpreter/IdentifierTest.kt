package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*
class IdentifierTest {
    @Test
    fun testIdentifier(){
        val src = """
            var x = 10;
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Broj(
                value = 10.0
            )
        )

        assert(result.last() == expectedResult.last())
    }
}