package interpreter

import BinaryExpression
import BlockStatement
import NumericLiteral
import StringLiteral
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Number

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

        assert(result == expectedResult)
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

        val expectedResult = arrayListOf(
            Text(
                value = "String"
            )
        )

        assert(result == expectedResult)
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

        val expectedResult = arrayListOf(
            Text(
                value = "Nested"
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testVariableScopeInBlocks(){
        val src = """
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

        val expectedResult = arrayListOf(
            Number(
                value = 20.0
            ),
            Number(
                value = 10.0
            )
        )

        println(result)
        assert(result == expectedResult)
    }
}