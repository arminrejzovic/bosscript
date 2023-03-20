package parser

import org.junit.jupiter.api.Test

class LogicalOperatorsTest {
    @Test
    fun testLogicalAnd(){
        val src = """
            var x = t && f;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = LogicalExpression(
                            left = Identifier(
                                symbol = "t"
                            ),
                            right = Identifier(
                                symbol = "f"
                            ),
                            operator = "&&"
                        )
                    )
                ),
                isConstant = false
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testLogicalOr(){
        val src = """
            var x = t || f;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = LogicalExpression(
                            left = Identifier(
                                symbol = "t"
                            ),
                            right = Identifier(
                                symbol = "f"
                            ),
                            operator = "||"
                        )
                    )
                ),
                isConstant = false
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testEqualityWithRelational(){
        val src = """
            x == y > z;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(
                left = Identifier(
                    symbol = "x"
                ),
                right = BinaryExpression(
                    left = Identifier(
                        symbol = "y"
                    ),
                    right = Identifier(
                        symbol = "z"
                    ),
                    operator = ">"
                ),
                operator = "=="
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testEqualityII(){
        val src = """
            x + 5 > 10 == tacno;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(
                right = BooleanLiteral(
                    value = true
                ),
                left = BinaryExpression(
                    left = BinaryExpression(
                        left = Identifier(
                            symbol = "x"
                        ),
                        right = NumericLiteral(
                            value = 5.0
                        ),
                        operator = "+"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = ">"
                ),
                operator = "=="
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testComplexIf(){
        val src = """
            ako(y > 5 || (z > 10 && x < 3)){}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition= LogicalExpression(
                    left = BinaryExpression(
                        left = Identifier(
                            symbol = "y"
                        ),
                        right = NumericLiteral(
                            value = 5.0
                        ),
                        operator = ">"
                    ),
                    right = LogicalExpression(
                        left = BinaryExpression(
                            left = Identifier(
                                symbol = "z"
                            ),
                            right = NumericLiteral(
                                value = 10.0
                            ),
                            operator = ">"
                        ),
                        right = BinaryExpression(
                            left = Identifier(
                                symbol = "x"
                            ),
                            right = NumericLiteral(
                                value = 3.0
                            ),
                            operator = "<"
                        ),
                        operator = "&&"
                    ),
                    operator = "||"
                ),
                consequent = BlockStatement(
                    body = arrayListOf()
                ),
                alternate = null
            )
        )

        println(program.body)
        assert(program.body == expectedResult)
    }

    @Test
    fun testTrueOrFalseAndFalse(){
        val src = """
            tacno || netacno && netacno;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            BinaryExpression(
                right = BooleanLiteral(
                    value = true
                ),
                left = BinaryExpression(
                    left = BinaryExpression(
                        left = Identifier(
                            symbol = "x"
                        ),
                        right = NumericLiteral(
                            value = 5.0
                        ),
                        operator = "+"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = ">"
                ),
                operator = "=="
            )
        )

        println(program.body)
        //assert(program.body == expectedResult)
    }
}