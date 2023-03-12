package interpreter

import AssignmentExpression
import BinaryExpression
import BlockStatement
import Identifier
import IfStatement
import NumericLiteral
import UnlessStatement
import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Number
import udemy.Parser

class IfElseTests {
    @Test
    fun testSimpleIf() {
        val src = """
            ako(x == 10.0){
                x = 22;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 22.0
            )
        )

        println(result)
        println(expectedResult)
        assert(result.last() == expectedResult.last())
    }

    @Test
    fun testRegularIf() {
        val src = """
            ako (x > 10) {
                x = 69;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 10.0
            )
        )

        println(result)
        println(expectedResult)
        assert(result.last() == expectedResult.last())
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
            ako(x == 3){
                x=10;
            }
            inace{
                x=3;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 3.0
            )
        )

        println(result)
        println(expectedResult)
        assert(result.last() == expectedResult.last())
    }

    @Test
    fun testBranchingIfElse() {
        val src = """
            ako(x < 5){
                x = 5;
            }
            ili ako(x < 9){
                x = 9;
            }
            inace{
                x = 20;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 20.0
            )
        )

        println(result)
        println(expectedResult)
        assert(result.last() == expectedResult.last())
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
            osim ako(x > 20){
                x=1;
            }
            x;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Number(
                value = 1.0
            )
        )

        println(result)
        println(expectedResult)
        assert(result.last() == expectedResult.last())
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
}
