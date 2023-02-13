package parser

import BinaryExpression
import BlockStatement
import ForStatement
import Identifier
import NumericLiteral
import org.junit.jupiter.api.Test
import udemy.Parser

class ForLoopTest {
    @Test
    fun testForLoopNoStep(){
        val src = """
            za svako(x od 0 do 10){
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ForStatement(
                counter = Identifier(
                    symbol = "x"
                ),
                startValue = NumericLiteral(
                    value = 0.0
                ),
                endValue = NumericLiteral(
                    10.0
                ),
                body = BlockStatement(
                    body = arrayListOf()
                ),
                step = null
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testForLoopWithStep(){
        val src = """
            za svako(x od 0 do 10 korak 2){
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ForStatement(
                counter = Identifier(
                    symbol = "x"
                ),
                startValue = NumericLiteral(
                    value = 0.0
                ),
                endValue = NumericLiteral(
                    10.0
                ),
                body = BlockStatement(
                    body = arrayListOf()
                ),
                step = NumericLiteral(
                    value = 2.0
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testForLoopWithExpressions(){
        val src = """
            za svako(x od 10-6 do 10+6 korak 10^0){
                
            }
        """.trimIndent()

        val parser = Parser()
        val program = parser.parseProgram(src)

        val expectedResult = arrayListOf(
            ForStatement(
                counter = Identifier(
                    symbol = "x"
                ),
                startValue = BinaryExpression(
                    left = NumericLiteral(
                        value = 10.0
                    ),
                    right = NumericLiteral(
                        value = 6.0
                    ),
                    operator = "-"
                ),
                endValue = BinaryExpression(
                    left = NumericLiteral(
                        value = 10.0
                    ),
                    right = NumericLiteral(
                        value = 6.0
                    ),
                    operator = "+"
                ),
                body = BlockStatement(
                    body = arrayListOf()
                ),
                step = BinaryExpression(
                    left = NumericLiteral(
                        value = 10.0
                    ),
                    right = NumericLiteral(
                        value = 0.0
                    ),
                    operator = "^"
                )
            )
        )
        assert(program.body == expectedResult)
    }

    @Test
    fun testForLoopMissingDo(){
        val src = """
            za svako(x od 0 dod 10){
                
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Expected ending condition for loop, missing keyword 'do'"
        var error = ""

        try {
            val program = parser.parseProgram(src)
        }catch (e: Exception){
            error = e.message.toString()
        }


        assert(error == expectedError)
    }

    @Test
    fun testForLoopMissingOd(){
        val src = """
            za svako(x odd 0 do 10){
                
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Expected starting condition for loop, missing keyword 'od'"
        var error = ""

        try {
            val program = parser.parseProgram(src)
        }catch (e: Exception){
            error = e.message.toString()
        }


        assert(error == expectedError)
    }

    @Test
    fun testForLoopMissingZa(){
        val src = """
            svako(x od 0 do 10){
                
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "You are probably missing 'za' before 'svako'"
        var error = ""

        try {
            val program = parser.parseProgram(src)
        }catch (e: Exception){
            error = e.message.toString()
        }


        assert(error == expectedError)
    }

    @Test
    fun testForLoopMissingSvako(){
        val src = """
            za (x od 0 do 10){
                
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Missing 'svako' following 'za'"
        var error = ""

        try {
            val program = parser.parseProgram(src)
        }catch (e: Exception){
            error = e.message.toString()
            println(error)
        }


        assert(error == expectedError)
    }

    @Test
    fun testForLoopComplex(){
        val src = """
            za svako(x od 10+3 do 10+6){
                
            }
        """.trimIndent()

        val parser = Parser()
        val expectedError = "Missing 'svako' following 'za'"
        var error = ""

        try {
            val program = parser.parseProgram(src)
        }catch (e: Exception){
            println(error)
            error = e.message.toString()
        }


        //assert(error == expectedError)
    }
}