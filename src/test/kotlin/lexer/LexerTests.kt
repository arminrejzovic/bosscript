package lexer

import interpreter.Interpreter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LexerTests {
    @Test
    fun testFibonacciFunction(){
        val src = """
            paket "matematika";

            funkcija main(){
                za svako (x od 0 do 1 korak 0.01){
                    ispis(sin(x));
                }
            }
        """.trimIndent()

        val tokens = tokenize(src, false)
        val expectedTokens = arrayListOf(
            Token(value = "paket", TokenType.Paket, 0, 0),
            Token(value = "\"", TokenType.DoubleQuote, 0, 0),
            Token(value = "matematika", TokenType.String, 0, 0),
            Token(value = "\"", TokenType.DoubleQuote, 0, 0),
            Token(value = ";", TokenType.Semicolon, 0, 0),
            Token(value = "funkcija", TokenType.Funkcija, 0, 0),
            Token(value = "main", TokenType.Identifier, 0, 0),
            Token(value = "(", TokenType.OpenParen, 0, 0),
            Token(value = ")", TokenType.CloseParen, 0, 0),
            Token(value = "{", TokenType.OpenBrace, 0, 0),
            Token(value = "za", TokenType.Za, 0, 0),
            Token(value = "svako", TokenType.Svako, 0, 0),
            Token(value = "(", TokenType.OpenParen, 0, 0),
            Token(value = "x", TokenType.Identifier, 0, 0),
            Token(value = "od", TokenType.Od, 0, 0),
            Token(value = "0", TokenType.Number, 0, 0),
            Token(value = "do", TokenType.Do, 0, 0),
            Token(value = "1", TokenType.Number, 0, 0),
            Token(value = "korak", TokenType.Korak, 0, 0),
            Token(value = "0.01", TokenType.Number, 0, 0),
            Token(value = ")", TokenType.CloseParen, 0, 0),
            Token(value = "{", TokenType.OpenBrace, 0, 0),
            Token(value = "ispis", TokenType.Identifier, 0, 0),
            Token(value = "(", TokenType.OpenParen, 0, 0),
            Token(value = "sin", TokenType.Identifier, 0, 0),
            Token(value = "(", TokenType.OpenParen, 0, 0),
            Token(value = "x", TokenType.Identifier, 0, 0),
            Token(value = ")", TokenType.CloseParen, 0, 0),
            Token(value = ")", TokenType.CloseParen, 0, 0),
            Token(value = ";", TokenType.Semicolon, 0, 0),
            Token(value = "}", TokenType.CloseBrace, 0, 0),
            Token(value = "}", TokenType.CloseBrace, 0, 0)
        )
        expectedTokens.forEachIndexed{index, token ->
            assert(token == tokens[index])
        }
    }

    @Test
    fun testWhileLoopTokens(){
        val src = """
            dok(x != 10){}
        """.trimIndent()

        val tokens = tokenize(src, false)
        val expectedTokens = arrayListOf(
            Token(value = "dok", TokenType.Dok, 0, 0),
            Token(value = "(", TokenType.OpenParen, 0, 0),
            Token(value = "x", TokenType.Identifier, 0, 0),
            Token(value = "!=", TokenType.EqualityOperator, 0, 0),
            Token(value = "10", TokenType.Number, 0, 0),
            Token(value = ")", TokenType.CloseParen, 0, 0),
            Token(value = "{", TokenType.OpenBrace, 0, 0),
            Token(value = "}", TokenType.CloseBrace, 0, 0)
        )
        expectedTokens.forEachIndexed{index, token ->
            println("Found ${tokens[index]}")
            println("Expected $token")
            assert(token == tokens[index])
        }
    }

    @Test
    fun testNumberTokens(){
        val src = """
            10.0;
            1_000_000;
            6969;
        """.trimIndent()

        val tokens = tokenize(src, false)
        val expectedTokens = arrayListOf(
            Token(value = "10.0", TokenType.Number, 0, 0),
            Token(value = ";", TokenType.Semicolon, 0, 0),
            Token(value = "1000000", TokenType.Number, 0, 0),
            Token(value = ";", TokenType.Semicolon, 0, 0),
            Token(value = "6969", TokenType.Number, 0, 0),
            Token(value = ";", TokenType.Semicolon, 0, 0)
        )
        expectedTokens.forEachIndexed{index, token ->
            println("Found ${tokens[index]}")
            println("Expected $token")
            assert(token == tokens[index])
        }
    }
}