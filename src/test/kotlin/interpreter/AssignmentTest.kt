package interpreter

import AssignmentExpression
import BinaryExpression
import Identifier
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Array
import udemy.Number
import udemy.Object
import java.util.StringJoiner

class AssignmentTest {
    @Test
    fun testVariableReassignment() {
        val src = """
            var a = 10 + 5 * 8;
            a = 61;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Number(
                value = 61.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testConstantReassignment() {
        val src = """
            konst a = 10 + 5 * 8;
            a = 61;
            a;
        """.trimIndent()

        val interpreter = Interpreter()

        var error = ""
        val expectedError = "Constants cannot be reassigned"

        try {
            interpreter.evaluateProgram(src)
        } catch (e: Exception) {
            error = e.message.toString()
        }

        assert(error == expectedError)
    }

    @Test
    fun testAllComplexAssign() {
        val src = """
            var a = 10;
            ispis(a += 2);
            
            ispis(a -= 2);
            
            ispis(a *= 2);
            
            ispis(a /= 2);
            
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
            value = 10.0
        )


        println(result)
        assert(result.last() == expectedResult)
    }

    @Test
    fun tesObjectPropertyComplexAssign() {
        val src = """
            var a = {
                x: 10,
                b: 5,
            };
            
            a.x += 2;
            a.b -= 2;
            
            ispis(a);
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Object(
            properties = hashMapOf(
                "x" to Number(value = 12.0),
                "b" to Number(value = 3.0)
            )
        )

        assert(result.last() == expectedResult)
    }
}