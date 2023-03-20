package interpreter

import org.junit.jupiter.api.Test

class UnaryOperatorsTest {

    @Test
    fun testUnaryMinus(){
        val src = """
            var x = 2;
            var z = -x * 10;
            ispis(x);
            z;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = -20.0
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testUnaryPlusOnString(){
        val src = """
            var str_num = "5.2";
            var num = +str_num;
            num;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 5.2
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testUnaryPlusOnNumber(){
        val src = """
            var a = 5.2;
            var b = +a;
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = 5.2
        )

        assert(result.last() == expectedResult)
    }
}