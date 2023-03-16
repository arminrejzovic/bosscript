package interpreter

import BinaryExpression
import BlockStatement
import BooleanLiteral
import CallExpression
import FunctionParameter
import Identifier
import MemberExpression
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Function
import udemy.Number

class FunctionCallTest {
    @Test
    fun testFunctionUntypedParam() {
        val src = """
            funkcija xf(a){
                a + a;
            }
            xf(5);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Null()

        assert(result.last() == expectedResult)
    }

    @Test
    fun testClosureCall() {
        val src = """
            var x = 10;
            funkcija xf(a){
                var x = 69;
                a + x;
            }
            xf(5);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
            value = 10.0
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testParamWithSameNameAsGlobalVar() {
        val src = """
            funkcija xf(x){
                5 + x;
            }
            xf(5);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Null()

        assert(result.last() == expectedResult)
    }

    @Test
    fun testReturn() {
        val src = """
            funkcija xf(x){
                vrati 5 + x;
                ispis(x);
            }
            var a = xf(5);
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
                value = 10.0
            )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testVoidReturn() {
        val src = """
            funkcija void(x){
                vrati se;
                ispis(x);
            }
            var a = void(5);
            ispis(a);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Null()

        assert(result.last() == expectedResult)
    }

    @Test
    fun testMultipleReturns() {
        val src = """
            funkcija e(x){
                ako(x == 3){
                    ispis("ako 3");
                    vrati 3;
                }
                ili ako(x == 4){
                    ispis("ako 4");
                    vrati 4;
                }
                ili ako (x == 5){
                    ispis("ako 5");
                    vrati 5;
                }
                inace {
                    vrati 100;
                }
            }
            ispis(e(3));
            ispis("****");
            ispis(e(5));
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }


    @Test
    fun testMultipleReturnsNoElse() {
        val src = """
            funkcija e(x){
                ako(x == 3){
                    ispis("ako 3");
                    vrati 3;
                }
                ako(x == 4){
                    ispis("ako 4");
                    vrati 4;
                }
                ako (x == 5){
                    ispis("ako 5");
                    vrati 5;
                }
            }
            var tri = e(3);
            ispis("****");
            var pet = e(5);
            ispis(tri, pet);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
        println(result)
    }

    @Test
    fun testNestedReturn() {
        val src = """
            funkcija e(x){
                ako(x > 3){
                    ako(x == 5){
                        vrati 5;
                    }
                    vrati 69;
                }
            }
            
            var x = e(5);
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
        assert(result.last() == Number(value = 5.0))
    }

    @Test
    fun testReturnInLoop() {
        val src = """
            funkcija e(){
                za svako(x od 1 do 10){
                    ako(x % 5 == 0){
                        vrati x;
                    }
                }
            }
            
            var x = e();
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
        assert(result.last() == Number(value = 5.0))
    }

    @Test
    fun testVoidFunction() {
        val src = """
            funkcija e(){
                var z = "Hello";
                z;
            }
            
            var x = e();
            ispis(x);
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)
        assert(result.last() == Null())
    }
}
