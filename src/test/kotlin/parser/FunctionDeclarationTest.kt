package parser

import BinaryExpression
import BlockStatement
import FunctionDeclaration
import Identifier
import ReturnStatement
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
            funkcija x(num){
                
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
                    Identifier(
                        symbol = "num"
                    )
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
            funkcija x(a,b,c){
                
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
                    Identifier(
                        symbol = "a"
                    ),
                    Identifier(
                        symbol = "b"
                    ),
                    Identifier(
                        symbol = "c"
                    )
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
            funkcija x(a,b,c){
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
                    Identifier(
                        symbol = "a"
                    ),
                    Identifier(
                        symbol = "b"
                    ),
                    Identifier(
                        symbol = "c"
                    )
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
            funkcija x(a,b,c){
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
                    Identifier(
                        symbol = "a"
                    ),
                    Identifier(
                        symbol = "b"
                    ),
                    Identifier(
                        symbol = "c"
                    )
                ),
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