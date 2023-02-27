package parser

import BlockStatement
import CallExpression
import FunctionDeclaration
import FunctionExpression
import FunctionParameter
import Identifier
import ObjectLiteral
import Property
import ReturnStatement
import TypeAnnotation
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.Parser

class LambdaFunctionTest {
    @Test
    fun testFunctionAssignedToVar() {
        val src = """
            var x = funkcija(){};
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = FunctionExpression(
                            params = arrayListOf(),
                            returnType = null,
                            body = BlockStatement(
                                body = arrayListOf()
                            ),
                        ),
                    ),
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFullLambda() {
        val src = """
            var x = funkcija(a:broj, b:logicki): broj{
                vrati a;
            };
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "x",
                        value = FunctionExpression(
                            params = arrayListOf(
                                FunctionParameter(
                                    identifier = Identifier(
                                        symbol = "a"
                                    ),
                                    type = TypeAnnotation(
                                        typeName = "broj",
                                        isArrayType = false
                                    )
                                ),
                                FunctionParameter(
                                    identifier = Identifier(
                                        symbol = "b"
                                    ),
                                    type = TypeAnnotation(
                                        typeName = "logicki",
                                        isArrayType = false
                                    )
                                )
                            ),
                            returnType = TypeAnnotation(
                                typeName = "broj",
                                isArrayType = false
                            ),
                            body = BlockStatement(
                                body = arrayListOf(
                                    ReturnStatement(
                                        argument = Identifier(
                                            symbol = "a"
                                        )
                                    )
                                )
                            ),
                        ),
                    ),
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testLambdaAsArg() {
        val src = """
            onClick(funkcija(){});
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            CallExpression(
                callee = Identifier(
                    symbol = "onClick"
                ),
                args = arrayListOf(
                    FunctionExpression(
                        params = arrayListOf(),
                        returnType = null,
                        body = BlockStatement(
                            body = arrayListOf()
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testLambdaAsObjectProp() {
        val src = """
            var x = {
                a: funkcija(){}
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
                                    value = FunctionExpression(
                                        params = arrayListOf(),
                                        returnType = null,
                                        body = BlockStatement(
                                            body = arrayListOf()
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
}