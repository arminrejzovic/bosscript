package parser

import BlockStatement
import Identifier
import ModelDefinitionStatement
import ModelProperty
import org.junit.jupiter.api.Test
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
                        type = Identifier(symbol = "tekst"),
                        isArrayType = false
                    ),
                    ModelProperty(
                        name = "prezime",
                        type = Identifier(symbol = "tekst"),
                        isArrayType = false
                    ),
                    ModelProperty(
                        name = "godiste",
                        type = Identifier(symbol = "broj"),
                        isArrayType = false
                    ),
                    ModelProperty(
                        name = "lista",
                        type = Identifier(symbol = "tekst"),
                        isArrayType = true
                    ),
                    ModelProperty(
                        name = "ucenik",
                        type = Identifier(symbol = "logicki"),
                        isArrayType = false
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
}