package parser

import org.junit.jupiter.api.Test

class ImportTest {

    @Test
    fun testSimpleImport(){
        val src = """
            paket "matematika";
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ImportStatement(
                packageName = "matematika",
                imports = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testMultipleImports(){
        val src = """
            paket "matematika";
            paket "IO";
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ImportStatement(
                packageName = "matematika",
                imports = null
            ),
            ImportStatement(
                packageName = "IO",
                imports = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testSelectiveImport(){
        val src = """
            paket "matematika" {korijen, pi, e};
            paket "IO";
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ImportStatement(
                packageName = "matematika",
                imports = arrayListOf(
                    Identifier(symbol = "korijen"),
                    Identifier(symbol = "pi"),
                    Identifier(symbol = "e")
                )
            ),
            ImportStatement(
                packageName = "IO",
                imports = null
            )
        )
        assert(program.body == expectedResult)
    }
}