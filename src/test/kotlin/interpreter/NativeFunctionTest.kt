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
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintArray(){
        val src = """
            ispis([1,2,3]);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
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
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintObject(){
        val src = """
            var obj = {
                a: 10,
                b: "Hello",
                c: [1,2,3],
                d: tacno
            };
            ispis(obj);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintNativeFunction(){
        val src = """
            ispis(ispis);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintUserDefinedFunction(){
        val src = """
            funkcija test(a, b){
                vrati a + b;
            }
            ispis(test);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testPrintMultiple(){
        val src = """
            ispis("Hello", "World", 1);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }
}