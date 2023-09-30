package interpreter

import interpreter.values.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BlockTest {
    @Test
    fun testEmptyBlock(){
        val src = """
            {}
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null()
        )

        Assertions.assertTrue(result.last() is Null)
    }

    @Test
    fun testSimpleBlock(){
        val src = """
            {
                10+1;
                "String";
            }
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        Assertions.assertTrue(result.last() is Null)
    }

    @Test
    fun testNestedBlock(){
        val src = """
            {
                10+1;
                "String";
                {
                    "Nested";
                }
            }
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        Assertions.assertTrue(result.last() is Null)
    }

    @Test
    fun testVariableScopeInBlocks(){
        val src = """
            var x = 10;
            {
                var x = 11;
                {
                    var x = 20;
                    x;
                }
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )

        println(result)
        assert(result.last() == expectedResult)
    }
}