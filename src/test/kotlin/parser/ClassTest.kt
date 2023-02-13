package parser

import ClassDeclaration
import ClassFieldDeclaration
import Identifier
import org.junit.jupiter.api.Test
import udemy.Parser

class ClassTest {
    @Test
    fun testClassDeclarationNoFields(){
        val src = """
            klasa Primjer{
                privatna var x;
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ClassDeclaration(
                name = Identifier(
                    symbol = "Primjer"
                ),
                superclass = null,
                body = arrayListOf(
                    ClassFieldDeclaration(
                        visibility = "privatna",
                        isConstant = false,
                        identifier = "x",
                        value = null
                    )
                )
            )
        )
        assert(program.body == expectedResult)
    }
}