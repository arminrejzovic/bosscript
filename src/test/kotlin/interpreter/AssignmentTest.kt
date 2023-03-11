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
import java.util.StringJoiner

class AssignmentTest {
    @Test
    fun testVariableReassignment(){
        val src = """
            var a = 10 + 5 * 8;
            a = 61;
            a;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Null(),
            Number(
                value = 61.0
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testConstantReassignment(){
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
        } catch (e: Exception){
            error = e.message.toString()
        }

        assert(error == expectedError)
    }
}