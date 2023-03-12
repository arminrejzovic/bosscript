package interpreter

import org.junit.jupiter.api.Test
import udemy.Interpreter

class NativeFunctionTest {
    @Test
    fun testPrintString(){
        val src = """
            ispis("Pozdrav svijet!");
            upozorenje("Upozorenje!");
            greska("Greska!");
            ispis("Ovo treba biti bijeli tekst");
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintArray(){
        val src = """
            ispis([1,2,3]);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintWithinFunction(){
        val src = """
            funkcija test(){
                ispis("test");
            }
            test();
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
    }
}