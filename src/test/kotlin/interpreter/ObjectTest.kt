package interpreter

import BinaryExpression
import BooleanLiteral
import CallExpression
import Identifier
import NumericLiteral
import ObjectLiteral
import Property
import StringLiteral
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Object
import udemy.Parser
import udemy.Text

class ObjectTest {
    @Test
    fun testEmptyObjectVar() {
        val src = """
            var x = {};
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Object(
                properties = hashMapOf()
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testObjectVar() {
        val src = """
            var x = {
                a: "Hello",
                b: 10,
                c: tacno
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ObjectLiteral(
                            properties = arrayListOf(
                                Property(
                                    key = "a",
                                    value = StringLiteral(
                                        value = "Hello"
                                    )
                                ),
                                Property(
                                    key = "b",
                                    value = NumericLiteral(
                                        value = 10.0
                                    )
                                ),
                                Property(
                                    key = "c",
                                    value = BooleanLiteral(
                                        value = true
                                    )
                                ),
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
    fun testNestedEmptyObjectVar() {
        val src = """
            var x = {
                a: {},
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ObjectLiteral(
                            properties = arrayListOf(
                                Property(
                                    key = "a",
                                    value = ObjectLiteral(
                                        properties = arrayListOf()
                                    )
                                )
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
    fun testNestedObjectVar() {
        val src = """
            var x = {
                a: {
                    x: 2,
                    y: 4
                },
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ObjectLiteral(
                            properties = arrayListOf(
                                Property(
                                    key = "a",
                                    value = ObjectLiteral(
                                        properties = arrayListOf(
                                            Property(
                                                key = "x",
                                                value = NumericLiteral(
                                                    value = 2.0
                                                )
                                            ),
                                            Property(
                                                key = "y",
                                                value = NumericLiteral(
                                                    value = 4.0
                                                )
                                            )
                                        )
                                    )
                                )
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
    fun testComplexObjectProperties() {
        val src = """
            var x = {
                a: "Hello" + "World",
                b: 10 % 2
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ObjectLiteral(
                            properties = arrayListOf(
                                Property(
                                    key = "a",
                                    value = BinaryExpression(
                                        left = StringLiteral(value = "Hello"),
                                        right = StringLiteral(value = "World"),
                                        operator = "+"
                                    )
                                ),
                                Property(
                                    key = "b",
                                    value = BinaryExpression(
                                        left = NumericLiteral(value = 10.0),
                                        right = NumericLiteral(value = 2.0),
                                        operator = "%"
                                    )
                                )
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
    fun testFunctionCallAsProp() {
        val src = """
            var x = {
                a: example()
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = ObjectLiteral(
                            properties = arrayListOf(
                                Property(
                                    key = "a",
                                    value = CallExpression(
                                        callee = Identifier(
                                            symbol = "example"
                                        ),
                                        args = arrayListOf()
                                    )
                                )
                            )
                        )
                    )
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }
}