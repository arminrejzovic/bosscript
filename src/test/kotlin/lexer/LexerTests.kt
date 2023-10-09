package lexer

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
            Token(value = "paket", TokenType.Paket, Pair(0, 0), Pair(0, 0)),
            Token(value = "\"", TokenType.DoubleQuote, Pair(0, 0), Pair(0, 0)),
            Token(value = "matematika", TokenType.String, Pair(0, 0), Pair(0, 0)),
            Token(value = "\"", TokenType.DoubleQuote, Pair(0, 0), Pair(0, 0)),
            Token(value = ";", TokenType.Semicolon, Pair(0, 0), Pair(0, 0)),
            Token(value = "funkcija", TokenType.Funkcija, Pair(0, 0), Pair(0, 0)),
            Token(value = "main", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = "(", TokenType.OpenParen, Pair(0, 0), Pair(0, 0)),
            Token(value = ")", TokenType.CloseParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "{", TokenType.OpenBrace, Pair(0, 0), Pair(0, 0)),
            Token(value = "za", TokenType.Za, Pair(0, 0), Pair(0, 0)),
            Token(value = "svako", TokenType.Svako, Pair(0, 0), Pair(0, 0)),
            Token(value = "(", TokenType.OpenParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "x", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = "od", TokenType.Od, Pair(0, 0), Pair(0, 0)),
            Token(value = "0", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = "do", TokenType.Do, Pair(0, 0), Pair(0, 0)),
            Token(value = "1", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = "korak", TokenType.Korak, Pair(0, 0), Pair(0, 0)),
            Token(value = "0.01", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = ")", TokenType.CloseParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "{", TokenType.OpenBrace, Pair(0, 0), Pair(0, 0)),
            Token(value = "ispis", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = "(", TokenType.OpenParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "sin", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = "(", TokenType.OpenParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "x", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = ")", TokenType.CloseParen, Pair(0, 0), Pair(0, 0)),
            Token(value = ")", TokenType.CloseParen, Pair(0, 0), Pair(0, 0)),
            Token(value = ";", TokenType.Semicolon, Pair(0, 0), Pair(0, 0)),
            Token(value = "}", TokenType.CloseBrace, Pair(0, 0), Pair(0, 0)),
            Token(value = "}", TokenType.CloseBrace, Pair(0, 0), Pair(0, 0))
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
            Token(value = "dok", TokenType.Dok, Pair(0, 0), Pair(0, 0)),
            Token(value = "(", TokenType.OpenParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "x", TokenType.Identifier, Pair(0, 0), Pair(0, 0)),
            Token(value = "!=", TokenType.EqualityOperator, Pair(0, 0), Pair(0, 0)),
            Token(value = "10", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = ")", TokenType.CloseParen, Pair(0, 0), Pair(0, 0)),
            Token(value = "{", TokenType.OpenBrace, Pair(0, 0), Pair(0, 0)),
            Token(value = "}", TokenType.CloseBrace, Pair(0, 0), Pair(0, 0))
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
            Token(value = "10.0", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = ";", TokenType.Semicolon, Pair(0, 0), Pair(0, 0)),
            Token(value = "1000000", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = ";", TokenType.Semicolon, Pair(0, 0), Pair(0, 0)),
            Token(value = "6969", TokenType.Number, Pair(0, 0), Pair(0, 0)),
            Token(value = ";", TokenType.Semicolon, Pair(0, 0), Pair(0, 0))
        )
        expectedTokens.forEachIndexed{index, token ->
            println("Found ${tokens[index]}")
            println("Expected $token")
            assert(token == tokens[index])
        }
    }
}