package interpreter

import AssignmentExpression
import BinaryExpression
import BlockStatement
import BooleanLiteral
import Identifier
import IfStatement
import LogicalExpression
import NullLiteral
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Array
import udemy.Bool
import udemy.Interpreter
import udemy.Number
import udemy.Parser

class LogicalOperatorsTest {
    @Test
    fun testGreaterThan() {
        val src = """
            5 > 3;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = true
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testLessThan() {
        val src = """
            5 < 3;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = false
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsNumberVsIdentifier() {
        val src = """
            var x = 10;
            5 == x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Bool(
            value = false
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testEqualsNumbers() {
        val src = """
            5 == 3;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = false
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsStrings() {
        val src = """
            "5" == "3";
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = false
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsStringsTrue() {
        val src = """
            "Hello" == "Hello";
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = true
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsBooleans() {
        val src = """
            tacno == tacno;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = true
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsArrays() {
        val src = """
            [1,2,3,4] == [1,2,3];
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = false
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsArraysTrue() {
        val src = """
            [1,2,3] == [1,2,3];
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Bool(
                value = true
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testEqualsObjects() {
        val src = """
            var a = {ime: "Armin"};
            var b = {ime: "Armin"};
            
            a == b;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Bool(
            value = true
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testComplexLogic() {
        val src = """
            funkcija test(x: broj,y: broj,z: broj){
               
                ako(y > 5 || (z > 10 && x < 3)){
                    vrati tacno;
                    ispis("TRUE AFTER RETURN");
                }
                inace {
                    vrati netacno;
                    ispis("FALSE AFTER RETURN");
                }
            }
            
            var a = test(3, 4, 7);
            var b = test(2, 6, 11);
            ispis(a,b);
            [a,b];
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Array(
            value = arrayListOf(
                Bool(false),
                Bool(true)
            )
        )

        assert(result.last() == expectedResult)
    }
}