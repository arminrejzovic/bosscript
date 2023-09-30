package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WhileLoopTest {
    @Test
    fun testBasicWhile(){
        val src = """
            var x = 0;
            dok(x != 10){
                x += 1;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = NumericLiteral(
                            value = 0.0
                        )
                    )
                ),
                isConstant = false
            ),
            WhileStatement(
                condition = BinaryExpression(
                    left = Identifier(
                        symbol = "x"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = "!="
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x"
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                )
            )
        )
        Assertions.assertEquals(program.body, expectedResult)
    }

    @Test
    fun testComplexWhile(){
        val src = """
            var x = 0;
            dok(x != 10 && y == nedefinisano){
                x += 1;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = NumericLiteral(
                            value = 0.0
                        )
                    )
                ),
                isConstant = false
            ),
            WhileStatement(
                condition = LogicalExpression(
                    left = BinaryExpression(
                        left = Identifier(
                            symbol = "x"
                        ),
                        right = NumericLiteral(
                            value = 10.0
                        ),
                        operator = "!="
                    ),
                    right = BinaryExpression(
                        left = Identifier(
                            symbol = "y"
                        ),
                        right = NullLiteral(),
                        operator = "=="
                    ),
                    operator = "&&"
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x"
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                )
            )
        )
        Assertions.assertEquals(program.body, expectedResult)
    }

    @Test
    fun testDoWhile(){
        val src = """
            radi{
                x += 1;
            } dok(x != 10);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            DoWhileStatement(
                condition = BinaryExpression(
                    left = Identifier(
                        symbol = "x"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = "!="
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x"
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testShorthandWhile(){
        val src = """
            var x = 0;
            dok(x != 10) => x += 1;
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = NumericLiteral(
                            value = 0.0
                        )
                    )
                ),
                isConstant = false
            ),
            WhileStatement(
                condition = BinaryExpression(
                    left = Identifier(
                        symbol = "x"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = "!="
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x"
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }
}