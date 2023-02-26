package parser

import BlockStatement
import Identifier
import ModelDefinitionStatement
import ModelProperty
import TypeAnnotation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import udemy.Parser

class ModelTest {
    @Test
    fun testModelDefinition(){
        val src = """
            model Korisnik{
                ime: tekst,
                prezime: tekst,
                godiste: broj,
                lista: tekst[],
                ucenik: logicki
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ModelDefinitionStatement(
                name = Identifier(
                    symbol = "Korisnik"
                ),
                properties = arrayListOf(
                    ModelProperty(
                        name = "ime",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    ModelProperty(
                        name = "prezime",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    ModelProperty(
                        name = "godiste",
                        type = TypeAnnotation(
                            typeName = "broj",
                            isArrayType = false
                        )
                    ),
                    ModelProperty(
                        name = "lista",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = true
                        )
                    ),
                    ModelProperty(
                        name = "ucenik",
                        type = TypeAnnotation(
                            typeName = "logicki",
                            isArrayType = false
                        )
                    ),
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testEmptyModelDefinition(){
        val src = """
            model Korisnik{}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ModelDefinitionStatement(
                name = Identifier(
                    symbol = "Korisnik"
                ),
                properties = arrayListOf()
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testMissingCommaInModelDefinition(){
        val src = """
            model Korisnik{
                ime: tekst
                prezime: tekst
            }
        """.trimIndent()

        val parser = Parser()

        val exception = assertThrows<Exception> {
            parser.parseProgram(src)
        }

        val expectedError = "Expected , or }"
        val error = exception.message

        assert(error == expectedError)
    }

    @Test
    fun testMissingTypeInModelDefinition(){
        val src = """
            model Korisnik{
                ime: tekst,
                prezime
            }
        """.trimIndent()

        val parser = Parser()

        val exception = assertThrows<Exception> {
            parser.parseProgram(src)
        }

        val expectedError = "Missing :"
        val error = exception.message
        assert(error == expectedError)
    }
}