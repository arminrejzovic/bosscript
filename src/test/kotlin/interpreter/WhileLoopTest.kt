package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*

class WhileLoopTest {
    @Test
    fun testBasicWhile(){
        val src = """
            var x = 0;
            dok(x != 10){
                x += 1;
            }
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )
        assert(result.last() == expectedResult)
    }

    @Test
    fun testComplexWhile(){
        val src = """
            var x = 0;
            var y = nedefinisano;
            dok(x != 10 && y == nedefinisano){
                x += 1;
            }
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )
        assert(result.last() == expectedResult)
    }

    @Test
    fun testDoWhile(){
        val src = """
            var x = 0;
            radi{
                x += 1;
            } dok(x != 10);
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )
        assert(result.last() == expectedResult)
    }

    @Test
    fun testShorthandWhile(){
        val src = """
            var x = 0;
            dok(x != 10) => x += 1;
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )
        assert(result.last() == expectedResult)
    }

    @Test
    fun testShorthandWhileWithPlusPlus(){
        val src = """
            var x = 0;
            dok(x != 10) => ++x;
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 10.0
        )
        assert(result.last() == expectedResult)
    }
}