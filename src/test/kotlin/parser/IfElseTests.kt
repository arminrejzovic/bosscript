package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IfElseTests {
    @Test
    fun testSimpleIf() {
        val src = """
            ako(x){
                x+=1;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testRegularIf() {
        val src = """
            ako (x > 10) {
                x+=1;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = BinaryExpression(
                    left = Identifier(
                        symbol = "x"
                    ),
                    right = NumericLiteral(
                        value = 10.0
                    ),
                    operator = ">"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleIfNoBlock() {
        val src = """
            ako(x) 
                x+=1;
            
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = AssignmentExpression(
                    assignee = Identifier(
                        symbol = "x",
                    ),
                    value = NumericLiteral(
                        value = 1.0
                    ),
                    assignmentOperator = "+="
                ),
                alternate = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleIfElse() {
        val src = """
            ako(x){
                x+=1;
            }
            inace{
                x=10;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 10.0
                            ),
                            assignmentOperator = "="
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testBranchingIfElse() {
        val src = """
            ako(x){
                x+=1;
            }
            ili ako(y){
                x=10;
            }
            inace{
                x = 20;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = IfStatement(
                    condition = Identifier(
                        symbol = "y"
                    ),
                    consequent = BlockStatement(
                        body = arrayListOf(
                            AssignmentExpression(
                                assignee = Identifier(
                                    symbol = "x",
                                ),
                                value = NumericLiteral(
                                    value = 10.0
                                ),
                                assignmentOperator = "="
                            )
                        )
                    ),
                    alternate = BlockStatement(
                        body = arrayListOf(
                            AssignmentExpression(
                                assignee = Identifier(
                                    symbol = "x",
                                ),
                                value = NumericLiteral(
                                    value = 20.0
                                ),
                                assignmentOperator = "="
                            ),
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testNestedIfElse() {
        val src = """
            ako(x) ako(y){
                z = x + y;
            } inace{z;}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            IfStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = IfStatement(
                    condition = Identifier(
                        symbol = "y"
                    ),
                    consequent = BlockStatement(
                        body = arrayListOf(
                            AssignmentExpression(
                                assignee = Identifier(
                                    symbol = "z",
                                ),
                                value = BinaryExpression(
                                    left = Identifier(
                                        symbol = "x",
                                    ),
                                    right = Identifier(
                                        symbol = "y",
                                    ),
                                    operator = "+"
                                ),
                                assignmentOperator = "="
                            )
                        )
                    ),
                    alternate = BlockStatement(
                        body = arrayListOf(
                            Identifier(
                                symbol = "z"
                            )
                        )
                    )
                ),
                alternate = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleUnless() {
        val src = """
            osim ako(x){
                x+=1;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            UnlessStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleUnlessElse() {
        val src = """
            osim ako(x){
                x+=1;
            }
            inace{
                x=10;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            UnlessStatement(
                condition = Identifier(
                    symbol = "x"
                ),
                consequent = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 1.0
                            ),
                            assignmentOperator = "+="
                        )
                    )
                ),
                alternate = BlockStatement(
                    body = arrayListOf(
                        AssignmentExpression(
                            assignee = Identifier(
                                symbol = "x",
                            ),
                            value = NumericLiteral(
                                value = 10.0
                            ),
                            assignmentOperator = "="
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testInvalidUnlessIfElse(){
        val src = """
            osim ako(x){
                x+=1;
            }
            ili ako(y){
                x=10;
            }
        """.trimIndent()

        val parser = Parser()
        val expectedException = "'ili' block must follow an 'ako' block"
        var exception = ""
        try {
            parser.parseProgram(src)
        }
        catch (e: Exception){
            exception = e.message.toString()
        }

        assert(exception == expectedException)
    }

    @Test
    fun testInvalidStandaloneElseIf(){
        val src = """
            ili ako(y){
                x=10;
            }
        """.trimIndent()

        val parser = Parser()
        val expectedException = "'ili' block must follow an 'ako' block"
        var exception = ""
        try {
            parser.parseProgram(src)
        }
        catch (e: Exception){
            exception = e.message.toString()
        }

        assert(exception == expectedException)
    }

    @Test
    fun testInvalidStandaloneElse(){
        val src = """
            inace{
                x=10;
            }
        """.trimIndent()

        val parser = Parser()
        val expectedException = "Identifier expected"
        var exception = ""
        try {
            val program = parser.parseProgram(src)

            println(program.body)
        }
        catch (e: Exception){
            exception = e.message.toString()
        }

        Assertions.assertEquals(expectedException, exception)
    }
}
