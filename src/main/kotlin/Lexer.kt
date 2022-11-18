import java.lang.Exception

enum class TokenType{
    Number,
    String,
    Identifier,
    OpenParen,      // (
    CloseParen,     // )
    OpenBracket,    // [
    CloseBracket,   // ]
    OpenBrace,      // {
    CloseBrace,     // }
    Comma,          // ,
    Colon,          // :
    Equals,         // =
    Var,
    Konst,
    BinaryOperator,
    EOF,
    DoubleQuote,
    NOTIMPLEMENTED
}

data class Token(
    val value: String,
    val type: TokenType
){
}

fun String.isAlpha(): Boolean{
    return this.lowercase() != this.uppercase()
}

fun String.isNumeric(): Boolean{
    return this.toDoubleOrNull() != null
}

fun String.isIgnored(): Boolean{
    return this == " " || this == "\n" || this == "\r" || this == "\t" || this == ";"
}

val keywords = mapOf(
    "var" to TokenType.Var,
    "konst" to TokenType.Konst,
    "funkcija" to TokenType.NOTIMPLEMENTED,
    "vrati" to TokenType.NOTIMPLEMENTED,
    "ako" to TokenType.NOTIMPLEMENTED,
)

fun tokenize(src: String): ArrayList<Token>{
    val tokens = ArrayList<Token>()

    val sourceCode = src.split("").drop(1).dropLast(1).toMutableList()

    while (sourceCode.size > 0){
        if(sourceCode[0] == "("){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenParen))
        }
        else if(sourceCode[0] == ")"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseParen))
        }
        else if(sourceCode[0] == "["){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenBracket))
        }
        else if(sourceCode[0] == "]"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseBracket))
        }
        else if(sourceCode[0] == "{"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenBrace))
        }
        else if(sourceCode[0] == "}"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseBrace))
        }
        else if(sourceCode[0] == "+" || sourceCode[0] == "-" || sourceCode[0] == "*" || sourceCode[0] == "/" || sourceCode[0] == "%"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.BinaryOperator))
        }
        else if(sourceCode[0] == "="){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Equals))
        }
        else if(sourceCode[0] == ":"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Colon))
        }
        else if(sourceCode[0] == ","){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Comma))
        }
        else{
            // Multi-character tokens

            if(sourceCode[0] == "\""){
                //String literals
                // Add the opening quotation mark
                tokens.add(Token(sourceCode.removeAt(0), TokenType.DoubleQuote))
                var string = ""
                while (sourceCode.isNotEmpty() && sourceCode[0] != "\""){
                    string += sourceCode.removeAt(0)
                }
                // Add the string value
                tokens.add(Token(string, TokenType.String))

                // If exists, add closing quotation mark
                if(sourceCode.isNotEmpty() && sourceCode[0] == "\""){
                    tokens.add(Token(sourceCode.removeAt(0), TokenType.DoubleQuote))
                }
            }

            // Numbers
            else if(sourceCode[0].isNumeric()){
                var number = ""
                while (sourceCode.isNotEmpty() && sourceCode[0].isNumeric()){
                    number += sourceCode.removeAt(0)
                }
                tokens.add(Token(number, TokenType.Number))
            }

            // Whitespace, semicolons
            else if(sourceCode[0].isIgnored()){
                sourceCode.removeAt(0)
            }

            // Identifiers
            else if(sourceCode[0].isAlpha()){
                var identifier = ""
                while (sourceCode.isNotEmpty() && sourceCode[0].isAlpha()){
                    identifier += sourceCode.removeAt(0)
                }
                // Check for reserved keywords
                val reserved = keywords[identifier]
                if (reserved != null){
                    tokens.add(Token(identifier, reserved))
                }
                else {
                    tokens.add(Token(identifier, TokenType.Identifier))
                }
            }

            else {
                // Something unexpected
                throw Exception("Unexpected token found: '${sourceCode[0]}'")
            }
        }
    }
    tokens.add(Token("EOF", TokenType.EOF))
    return tokens
}