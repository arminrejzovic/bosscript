package benchmarks

import org.junit.jupiter.api.Test
import udemy.Interpreter

class PerformanceTests {
    @Test
    fun testFibonacciFunction(){
        val src = """
            funkcija fib(n: broj): broj{
                ako (n <= 1){
                    vrati n;
                }
                inace {
                    vrati fib(n-1) + fib(n-2);
                }
            }
            
            var n = fib(10);
            ispis(n);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
    }

    @Test
    fun testFactorialFunction(){
        val src = """
            funkcija fakt(n: broj): broj{
                ako (n == 0){
                    vrati 1;
                }
                inace {
                    vrati n * fakt(n-1);
                }
            }
            
            var n = fakt(169);
            ispis(n);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
        println(result.last())
    }
}