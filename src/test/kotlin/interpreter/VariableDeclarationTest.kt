package interpreter

import BinaryExpression
import NumericLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Null
import udemy.Number
import udemy.Parser
import udemy.Text

class VariableDeclarationTest {
    @Test
    fun testSingleInitializedVarDeclaration(){
        val src = """
            var pie = 3.14;
            pie;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Number(
                value = 3.14
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testSingleUninitializedVarDeclaration(){
        val src = """
            var z;
            z;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Null()
        )

        assert(result == expectedResult)
    }

    @Test
    fun testDoubleInitializedVarDeclaration(){
        val src = """
            var xy = 10, yx = 5;
            xy;
            yx;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Number(value = 10.0),
            Number(value = 5.0)
        )

        assert(result == expectedResult)
    }

    @Test
    fun testDoubleUninitializedVarDeclaration(){
        val src = """
            var a, b;
            a;
            b;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Null(),
            Null()
        )

        assert(result == expectedResult)
    }

    @Test
    fun testSingleKonstDeclaration(){
        val src = """
            konst pie = 3.14;
            pie;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Number(
                value = 3.14
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testComplexVarDeclaration(){
        val src = """
            var f = 10 + 5 * 8;
            f;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Number(
                value = 50.0
            )
        )

        println(result)
        assert(result == expectedResult)
    }
}