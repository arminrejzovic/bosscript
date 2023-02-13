package parser

import BinaryExpression
import BlockStatement
import NumericLiteral
import StringLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class BlockTests {
    @Test
    fun testEmptyBlock(){
        val src = """
            {}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BlockStatement(body = arrayListOf())
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleBlock(){
        val src = """
            {
                10+1;
                "String";
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BlockStatement(
                body = arrayListOf(
                    BinaryExpression(left=NumericLiteral(value=10.0), right=NumericLiteral(value=1.0), operator="+"),
                    StringLiteral(value="String")
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testNestedBlock(){
        val src = """
            {
                10+1;
                "String";
                {
                    "Nested";
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BlockStatement(
                body = arrayListOf(
                    BinaryExpression(left=NumericLiteral(value=10.0), right=NumericLiteral(value=1.0), operator="+"),
                    StringLiteral(value="String"),
                    BlockStatement(
                        body = arrayListOf(
                            StringLiteral(value="Nested"),
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }
}