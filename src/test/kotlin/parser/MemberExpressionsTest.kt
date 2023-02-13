package parser

import Identifier
import MemberExpression
import NumericLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class MemberExpressionsTest {
    @Test
    fun testSimpleMember(){
        val src = """
            x.y;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            MemberExpression(
                isComputed = false,
                targetObject = Identifier(
                    symbol = "x"
                ),
                property = Identifier(
                    symbol = "y"
                )
            )
        )

        assert(program.body == expectedResult)
    }

    @Test
    fun testChainedMember(){
        val src = """
            x.y.z;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            MemberExpression(
                isComputed = false,
                targetObject = MemberExpression(
                    isComputed = false,
                    targetObject = Identifier(
                        symbol = "x"
                    ),
                    property = Identifier(
                        symbol = "y"
                    )
                ),
                property = Identifier(
                    symbol = "z"
                )
            )
        )

        assert(program.body == expectedResult)
    }

    @Test
    fun testComputedMember(){
        val src = """
            x[1];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            MemberExpression(
                isComputed = true,
                targetObject = Identifier(
                    symbol = "x"
                ),
                property = NumericLiteral(
                    value = 1.0
                )
            )
        )

        assert(program.body == expectedResult)
    }

    @Test
    fun testMemberNestedInComputedMember(){
        val src = """
            x[y.z];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            MemberExpression(
                isComputed = true,
                targetObject = Identifier(
                    symbol = "x"
                ),
                property = MemberExpression(
                    isComputed = false,
                    targetObject = Identifier(
                        symbol = "y"
                    ),
                    property = Identifier(
                        symbol = "z"
                    )
                )
            )
        )

        assert(program.body == expectedResult)
    }
}