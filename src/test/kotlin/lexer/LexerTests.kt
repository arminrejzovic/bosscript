package lexer

import interpreter.Interpreter
import org.junit.jupiter.api.Test

class LexerTests {
    @Test
    fun testFibonacciFunction(){
        val src = """
            paket "matematika";

            funkcija main(){
                za svako (x od 0 do 1 korak 0.01){
                    ispis(sin(x));
                }
            }
        """.trimIndent()

        val tokens = tokenize(src, false)
        println(tokens)
    }
}