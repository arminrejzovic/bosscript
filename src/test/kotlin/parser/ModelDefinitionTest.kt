package parser

import org.junit.jupiter.api.Test

class ModelDefinitionTest {
    @Test
    fun testEmptyModelDefinition() {
        val src = """
            model Korisnik{}
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult =
            ModelDefinitionStatement(
                className = Identifier(
                    symbol = "Korisnik"
                ),
                parentClassName = null,
                privateBlock = null,
                publicBlock = null
            )
        println(program.body.last())
        assert(program.body.last() == expectedResult)
    }

    @Test
    fun testModelDefinitionOnlyPublic() {
        val src = """
            model Korisnik{
                javno {
                    var x = 10;
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult =
            ModelDefinitionStatement(
                className = Identifier(
                    symbol = "Korisnik"
                ),
                parentClassName = null,
                privateBlock = null,
                publicBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "x",
                                    value = NumericLiteral(
                                        value = 10.0
                                    )
                                )
                            )
                        )
                    )
                )
            )
        println(program.body.last())
        assert(program.body.last() == expectedResult)
    }

    @Test
    fun testModelDefinitionOnlyPrivate() {
        val src = """
            model Korisnik{
                privatno {
                    var x = 10;
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult =
            ModelDefinitionStatement(
                className = Identifier(
                    symbol = "Korisnik"
                ),
                parentClassName = null,
                publicBlock = null,
                privateBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "x",
                                    value = NumericLiteral(
                                        value = 10.0
                                    )
                                )
                            )
                        )
                    )
                )
            )
        println(program.body.last())
        assert(program.body.last() == expectedResult)
    }

    @Test
    fun testModelDefinitionBothPrivateAndPublic() {
        val src = """
            model Korisnik{
                privatno {
                    var x = 10;
                }
                javno {
                   var z = "Z"; 
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult =
            ModelDefinitionStatement(
                className = Identifier(
                    symbol = "Korisnik"
                ),
                parentClassName = null,
                publicBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "z",
                                    value = StringLiteral(
                                        value = "Z"
                                    )
                                )
                            )
                        )
                    )
                ),
                privateBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "x",
                                    value = NumericLiteral(
                                        value = 10.0
                                    )
                                )
                            )
                        )
                    )
                )
            )
        println(program.body.last())
        assert(program.body.last() == expectedResult)
    }

    @Test
    fun testModelDefinitionWithParent() {
        val src = """
            model Nastavnik < Korisnik {
                privatno {
                    var x = 10;
                }
                javno {
                   var z = "Z"; 
                }
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult =
            ModelDefinitionStatement(
                className = Identifier(
                    symbol = "Nastavnik"
                ),
                parentClassName = Identifier(
                    symbol = "Korisnik"
                ),
                publicBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "z",
                                    value = StringLiteral(
                                        value = "Z"
                                    )
                                )
                            )
                        )
                    )
                ),
                privateBlock = ModelBlock(
                    body = arrayListOf(
                        VariableStatement(
                            declarations = arrayListOf(
                                VariableDeclaration(
                                    identifier = "x",
                                    value = NumericLiteral(
                                        value = 10.0
                                    )
                                )
                            )
                        )
                    )
                )
            )
        println(program.body.last())
        assert(program.body.last() == expectedResult)
    }

    @Test
    fun testModelDefinitionInvalidExpression() {
        val src = """
            model Nastavnik < Korisnik {
                privatno {
                    za svako (x od 0 do 10){
                        ispis(x);
                    }
                }
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Expected member declaration"
        var error = ""
        try {
            parser.parseProgram(src)
        }
        catch (e: Exception){
            error = "${e.message}"
        }
        assert(error == expectedError)
    }

    @Test
    fun testModelDefinitionInvalidExpressionII() {
        val src = """
            model Nastavnik < Korisnik {
                privatno{}
                za svako (x od 0 do 10){
                        ispis(x);
                }
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Expecting private or public block"
        var error = ""
        try {
            parser.parseProgram(src)
        }
        catch (e: Exception){
            error = "${e.message}"
        }
        assert(error == expectedError)
    }
}