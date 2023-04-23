package parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TipTest {
    @Test
    fun testModelDefinition(){
        val src = """
            tip Korisnik{
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
            TipDefinitionStatement(
                name = Identifier(
                    symbol = "Korisnik"
                ),
                properties = arrayListOf(
                    TypeProperty(
                        name = "ime",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    TypeProperty(
                        name = "prezime",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = false
                        )
                    ),
                    TypeProperty(
                        name = "godiste",
                        type = TypeAnnotation(
                            typeName = "broj",
                            isArrayType = false
                        )
                    ),
                    TypeProperty(
                        name = "lista",
                        type = TypeAnnotation(
                            typeName = "tekst",
                            isArrayType = true
                        )
                    ),
                    TypeProperty(
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
            tip Korisnik{}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            TipDefinitionStatement(
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
            tip Korisnik{
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
            tip Korisnik{
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