package interpreter

import org.junit.jupiter.api.Test

class BreakTest {
    @Test
    fun testForLoopBreak() {
        val src = """
            za svako(x od 0 do 10){
                ispis(x);
                ako(x == 3){
                    prekid
                }
            }
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }

    @Test
    fun testBreakInFunction() {
        val src = """
            funkcija test(){
                var z = 0;
                za svako (x od 0 do 5){
                    z += x^x;
                    ispis(z, " -> ", x);
                    ako(x == 3){
                        prekid
                    }
                }
                vrati z;
            }
            var t = test();
            ispis(t);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }
}