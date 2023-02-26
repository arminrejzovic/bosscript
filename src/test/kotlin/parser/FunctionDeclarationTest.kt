package parser

import BinaryExpression
import BlockStatement
import FunctionDeclaration
import FunctionParameter
import Identifier
import ReturnStatement
import TypeAnnotation
import org.junit.jupiter.api.Test
import udemy.Parser

class FunctionDeclarationTest {
    @Test
    fun testFunctionNoParams() {
        val src = """
            funkcija x(){
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            FunctionDeclaration(
                name = Identifier(
                    symbol = "x"
                ),
                params = arrayListOf(),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf()
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFunctionOneParam() {
        val src = """
            funkcija x(num:broj):broj{
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            FunctionDeclaration(
                name = Identifier(
                    symbol = "x"
                ),
                params = arrayListOf(
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "num"
                        ),
                        type = TypeAnnotation(
                            typeName = "broj",
                            isArrayType = false
                        )
                    ),
                ),
                returnType = TypeAnnotation(
                    typeName = "broj",
                    isArrayType = false
                ),
                body = BlockStatement(
                    body = arrayListOf()
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFunctionMultipleParams() {
        val src = """
            funkcija x(a:tekst,b:tekst,c:tekst):tekst{
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            FunctionDeclaration(
                name = Identifier(
                    symbol = "x"
                ),
                params = arrayListOf(
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "a"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "b"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "c"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    )
                ),
                returnType = TypeAnnotation(
                    typeName = "tekst",
                    isArrayType = false
                ),
                body = BlockStatement(
                    body = arrayListOf()
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFunctionReturnExpression() {
        val src = """
            funkcija x(a:broj,b:broj,c:broj):broj{
                vrati a+b;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            FunctionDeclaration(
                name = Identifier(
                    symbol = "x"
                ),
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
                            typeName = "broj",
                            isArrayType = false
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "c"
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
                                    symbol = "a"
                                ),
                                right = Identifier(
                                    symbol = "b"
                                ),
                                operator = "+"
                            )
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testFunctionVoidReturn() {
        val src = """
            funkcija x(a: tekst,b: tekst[],c: tekst){
                vrati se;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            FunctionDeclaration(
                name = Identifier(
                    symbol = "x"
                ),
                params = arrayListOf(
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "a"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "b"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = true
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(
                            symbol = "c"
                        ),
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    )
                ),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf(
                        ReturnStatement(
                            argument = null
                        )
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }
}