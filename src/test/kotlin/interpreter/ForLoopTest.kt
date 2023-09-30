package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*
import org.junit.jupiter.api.Assertions

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

        val expectedResult = Broj(value = 16.0)

        Assertions.assertEquals(expectedResult, result.last())
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

        val expectedResult = Broj(value = 16.0)

        Assertions.assertEquals(expectedResult, result.last())
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

        val expectedResult = Broj(value = 23.0)

        Assertions.assertEquals(expectedResult, result.last())
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

        Assertions.assertTrue(result.last() is Null)
    }
}