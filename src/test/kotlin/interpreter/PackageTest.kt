package interpreter

import org.junit.jupiter.api.Test
import kotlin.math.E
import kotlin.math.PI
import interpreter.values.*

class PackageTest {

    @Test
    fun testMathPackageImport() {
        val src = """
            paket "matematika";
            
            pi;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = PI
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testMathPackagePartialImport() {
        val src = """
            paket "matematika" {pi, e};
            
            [pi, e];
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Niz(
            value = arrayListOf(
                Broj(
                    value = PI
                ),
                Broj(
                    value = E
                )
            )
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testMathPackagePartialImportOnNotImported() {
        val src = """
            paket "matematika" {pi, e};
            
            ispis(arcsin);
        """.trimIndent()

        var error = ""
        val expectedError = "arcsin does not exist"

        try{
            val interpreter = Interpreter()
            interpreter.evaluateProgram(src)
        }
        catch (e: Exception){
            error = e.message ?: ""
        }


        assert(error == expectedError)
    }

    @Test
    fun testUserDefinedImport() {
        val src = """
            paket "./src/main/boss/test.boss";
            
            ispisiPort();
        """.trimIndent()

        try{
            val interpreter = Interpreter()
            interpreter.evaluateProgram(src)
        }
        catch (e: Exception){
            println(e.message)
        }
    }
}