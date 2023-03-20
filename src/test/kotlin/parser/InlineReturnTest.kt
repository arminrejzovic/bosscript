package parser

import org.junit.jupiter.api.Test

class InlineReturnTest {
    @Test
    fun testFunctionInlineReturn() {
        val src = """
            var x = funkcija() => "Hello";
            var z = 10;
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
                                body = arrayListOf(
                                    ReturnStatement(
                                        argument = StringLiteral(
                                            value = "Hello"
                                        )
                                    )
                                )
                            ),
                        ),
                    ),
                ),
                isConstant = false
            ),

            VariableStatement(
                declarations = arrayListOf(
                    VariableDeclaration(
                        identifier = "z",
                        value = NumericLiteral(
                            value = 10.0
                        ),
                    ),
                ),
                isConstant = false
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFunctionTypedInlineReturn() {
        val src = """
            var x = funkcija(): tekst => "Hello";
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
                            returnType = TypeAnnotation(
                                typeName = "tekst",
                                isArrayType = false
                            ),
                            body = BlockStatement(
                                body = arrayListOf(
                                    ReturnStatement(
                                        argument = StringLiteral(
                                            value = "Hello"
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
    fun testFunctionDeclarationTypedInlineReturn() {
        val src = """
            funkcija kvadrat(x: broj): broj => x*x
            kvadrat(5);
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
           FunctionDeclaration(
               name = Identifier(
                   symbol = "kvadrat"
               ),
               params = arrayListOf(
                   FunctionParameter(
                       identifier = Identifier(
                           symbol = "x"
                       ),
                       type = TypeAnnotation(
                           typeName = "broj",
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
                           argument = BinaryExpression(
                               left = Identifier(
                                   symbol = "x"
                               ),
                               right = Identifier(
                                   symbol = "x"
                               ),
                               operator = "*"
                           )
                       )
                   )
               )
           ),
            CallExpression(
                callee = Identifier(
                    symbol = "kvadrat"
                ),
                args = arrayListOf(
                    NumericLiteral(
                        value = 5.0
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }
}