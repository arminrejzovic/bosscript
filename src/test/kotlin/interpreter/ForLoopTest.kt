package interpreter

import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Null
import udemy.Number

class ForLoopTest {
    @Test
    fun testForLoopNoStep(){
        val src = """
            var x = 10;
            za svako(i od 0 do 5){
                x += 1;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(value = 15.0)

        assert(result.last() == expectedResult)
    }

    @Test
    fun testForLoopWithStep(){
        val src = """
            var x = 10;
            za svako(i od 0 do 10 korak 2){
                x += 1;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(value = 15.0)

        assert(result.last() == expectedResult)
    }

    @Test
    fun testForLoopWithExpressions(){
        val src = """
            var x = 10;
            za svako(i od 10-6 do 10+6 korak 10^0){
                x += 1;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(value = 22.0)

        assert(result.last() == expectedResult)
    }

    @Test
    fun testBackwardForLoop(){
        val src = """
            za svako(i od 10 do 0){
                ispis(i);
            }
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null()
        )

        assert(result == expectedResult)
    }

    // TODO Enforce loop body being a block statement
}