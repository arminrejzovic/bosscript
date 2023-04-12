package interpreter

import org.junit.jupiter.api.Test
import interpreter.values.*

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
            Broj(
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
            Broj(value = 10.0),
            Broj(value = 5.0)
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
            Broj(
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
            Broj(
                value = 50.0
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testArrayVarDeclaration(){
        val src = """
            var f = [10, 5, 8];
            f;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Niz(
                value = arrayListOf(
                    Broj(
                        value = 10.0
                    ),
                    Broj(
                        value = 5.0
                    ),
                    Broj(
                        value = 8.0
                    ),
                )
            )
        )
        assert(result == expectedResult)
    }

    @Test
    fun testObjectVarDeclaration(){
        val src = """
            var f = {a: 10, b: 5, c: 8};
            f;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Objekat(
                properties = hashMapOf(
                    "a" to Broj(
                        value = 10.0
                    ),
                    "b" to Broj(
                        value = 5.0
                    ),
                    "c" to Broj(
                        value = 8.0
                    ),
                )
            )
        )
        println(result)
        assert(result == expectedResult)
    }
}