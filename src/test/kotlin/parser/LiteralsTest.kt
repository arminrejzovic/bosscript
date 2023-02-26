package parser

import BooleanLiteral
import NumericLiteral
import StringLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class LiteralsTest {
    @Test
    fun testStringLiteral(){
        val src = """
            "Test";
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            StringLiteral(value="Test")
        )

        assert(program.body == expectedResult)
    }

    @Test
    fun testNumericLiteral(){
        val src = """
            6969;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            NumericLiteral(value=6969.0)
        )

        assert(program.body == expectedResult)
    }

    @Test
    fun testBooleanLiteral(){
        val src = """
            tacno;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BooleanLiteral(value=true)
        )

        assert(program.body == expectedResult)
    }
}