package parser

import ArrayLiteral
import AssignmentExpression
import BooleanLiteral
import Identifier
import NumericLiteral
import StringLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Parser

class ArrayTest {

    @Test
    fun testEmptyArray(){
        val src = """
            [];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ArrayLiteral(
                arr = arrayListOf()
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSimpleArray(){
        val src = """
            [1,2,3];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ArrayLiteral(
                arr = arrayListOf(
                    NumericLiteral(value = 1.0),
                    NumericLiteral(value = 2.0),
                    NumericLiteral(value = 3.0)
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testMixedTypeArray(){
        val src = """
            [1,2,"3"];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ArrayLiteral(
                arr = arrayListOf(
                    NumericLiteral(value = 1.0),
                    NumericLiteral(value = 2.0),
                    StringLiteral(value = "3")
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testArrayAssignedToVar(){
        val src = """
            var x = [1,2,"3"];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ArrayLiteral(
                            arr = arrayListOf(
                                NumericLiteral(value = 1.0),
                                NumericLiteral(value = 2.0),
                                StringLiteral(value = "3")
                            )
                        )
                    )
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testArrayReassignedToVar(){
        val src = """
            x = [1,netacno,3];
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            AssignmentExpression(
                assignee = Identifier(symbol = "x"),
                value = ArrayLiteral(
                    arr = arrayListOf(
                        NumericLiteral(value = 1.0),
                        BooleanLiteral(value = false),
                        NumericLiteral(value = 3.0)
                    )
                ),
                assignmentOperator = "="
            )
        )
        assert(program.body == expectedResult)
    }
}