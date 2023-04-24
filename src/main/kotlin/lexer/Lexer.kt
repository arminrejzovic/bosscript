package lexer

import isAlpha
import isIgnoredWhitespace
import isNumeric
import isValidVariableChar
import kotlin.Exception

val keywords = mapOf(
    "var" to TokenType.Var,
    "konst" to TokenType.Konst,
    "za" to TokenType.Za,
    "svako" to TokenType.Svako,
    "od" to TokenType.Od,
    "do" to TokenType.Do,
    "korak" to TokenType.Korak,
    "dok" to TokenType.Dok,
    "radi" to TokenType.Radi,
    "prekid" to TokenType.Break,
    "funkcija" to TokenType.Funkcija,
    "vrati" to TokenType.Vrati,
    "se" to TokenType.Se,
    "paket" to TokenType.Paket,
    "ako" to TokenType.Ako,
    "ili" to TokenType.Ili,
    "osim" to TokenType.Osim,
    "inace" to TokenType.Inace,
    "inače" to TokenType.Inace,
    "nedefinisano" to TokenType.Nedefinisano,
    "tacno" to TokenType.Tacno,
    "tačno" to TokenType.Tacno,
    "netacno" to TokenType.Netacno,
    "netačno" to TokenType.Netacno,
    "probaj" to TokenType.Try,
    "spasi" to TokenType.Catch,
    "konacno" to TokenType.Finally,
    "konačno" to TokenType.Finally,
    "tip" to TokenType.Tip,
    "model" to TokenType.Model,
    "privatna" to TokenType.Private
)

fun tokenize(src: String): ArrayList<Token>{
    val tokens = ArrayList<Token>()
    var line = 1
    var col = 1

    val sourceCode = src.split("").drop(1).dropLast(1).toMutableList()

    while (sourceCode.size > 0){
        if(sourceCode[0] == "("){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenParen, line, col++))
        }
        else if(sourceCode[0] == ")"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseParen, line, col++))
        }
        else if(sourceCode[0] == "["){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenBracket, line, col++))
        }
        else if(sourceCode[0] == "]"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseBracket, line, col++))
        }
        else if(sourceCode[0] == "{"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.OpenBrace, line, col++))
        }
        else if(sourceCode[0] == "}"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.CloseBrace, line, col++))
        }
        else if(sourceCode[0] == "^"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Exponent, line, col++))
        }
        else if(sourceCode[0] == "&"){
            if(sourceCode.size > 1 && sourceCode[1] == "&"){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.LogicalAnd, line, col))
                col+=2
            }
            else{
                throw Exception("Unexpected token found at[$line:$col]: '${sourceCode[0]}'")
            }
        }
        else if(sourceCode[0] == "|"){
            if(sourceCode.size > 1 && sourceCode[1] == "|"){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.LogicalAnd, line, col))
                col+=2
            }
            else{
                throw Exception("Unexpected token found at[$line:$col]: '${sourceCode[0]}'")
            }
        }
        else if(sourceCode[0] == "+" || sourceCode[0] == "-" || sourceCode[0] == "*" || sourceCode[0] == "/" || sourceCode[0] == "%"){
            if(sourceCode.size > 1 && sourceCode[1] == "="){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.ComplexAssign, line, col))
                col+=2
            }
            else if(sourceCode.size > 1 && sourceCode[0] == "+" && sourceCode[1] == "+"){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.UnaryIncrement, line, col))
                col+=2
            }
            else if(sourceCode.size > 1 && sourceCode[0] == "-" && sourceCode[1] == "-"){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.UnaryDecrement, line, col))
                col+=2
            }
            else{
                tokens.add(Token(sourceCode.removeAt(0), TokenType.BinaryOperator, line, col++))
            }
        }
        else if(sourceCode[0] == "="){
            if(sourceCode.size > 1 && sourceCode[1] == "="){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.EqualityOperator, line, col))
                col += 2
            }
            else if(sourceCode.size > 1 && sourceCode[1] == ">"){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.Arrow, line, col))
                col += 2
            }
            else{
                tokens.add(Token(sourceCode.removeAt(0), TokenType.SimpleAssign, line, col++))
            }
        }
        else if(sourceCode[0] == "!"){
            if(sourceCode.size > 1 && sourceCode[1] == "="){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.EqualityOperator, line, col))
                col += 2
            }
            else{
                tokens.add(Token(sourceCode.removeAt(0), TokenType.LogicalNot, line, col++))
            }
        }
        else if(sourceCode[0] == ":"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Colon, line, col++))
        }
        else if(sourceCode[0] == ";"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Semicolon, line, col++))
        }
        else if(sourceCode[0] == ","){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Comma, line, col++))
        }
        else if(sourceCode[0] == "."){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.Dot, line, col++))
        }
        else if(sourceCode[0] == "@"){
            tokens.add(Token(sourceCode.removeAt(0), TokenType.This, line, col++))
        }
        else if(sourceCode[0] == "<" || sourceCode[0] == ">"){
            if(sourceCode.size > 1 && sourceCode[1] == "="){
                tokens.add(Token(value = "${sourceCode.removeAt(0)}${sourceCode.removeAt(0)}", TokenType.RelationalOperator, line, col))
                col += 2
            }
            else{
                tokens.add(Token(sourceCode.removeAt(0), TokenType.RelationalOperator, line, col++))
            }
        }
        else if(sourceCode[0] == "\n"){
            line++
            col = 1
            sourceCode.removeAt(0)
        }
        else if(sourceCode[0].isIgnoredWhitespace()){
            sourceCode.removeAt(0)
            col += 1
        }
        else{
            // Multi-character tokens
            if(sourceCode[0] == "\""){
                //String literals
                // Add the opening quotation mark
                tokens.add(Token(sourceCode.removeAt(0), TokenType.DoubleQuote, line, col++))
                var string = ""
                while (sourceCode.isNotEmpty() && sourceCode[0] != "\""){
                    if(sourceCode[0] == "\\" && sourceCode.size > 1){
                        when(sourceCode[1]){
                            "n" -> string += "\n"
                            "t"-> string += "\t"
                            "r" -> string += "\r"
                            "\\" -> string += "\\"
                            "\"" -> string += "\""
                        }
                        sourceCode.removeAt(0)
                        sourceCode.removeAt(0)
                    }
                    else{
                        string += sourceCode.removeAt(0)
                    }
                }
                // Add the string value
                tokens.add(Token(string, TokenType.String, line, col))
                col += string.length

                // If exists, add closing quotation mark
                if(sourceCode.isNotEmpty() && sourceCode[0] == "\""){
                    tokens.add(Token(sourceCode.removeAt(0), TokenType.DoubleQuote, line, col++))
                }
            }

            // Numbers
            else if(sourceCode[0].isNumeric()){
                var number = ""
                while (sourceCode.isNotEmpty() && sourceCode[0].isNumeric() || sourceCode[0] == "_" || sourceCode[0] == "."){
                    number += sourceCode.removeAt(0)
                }
                val validNumberPattern = Regex("^-?(0|[1-9](_?[0-9])*)(\\.[0-9](_?[0-9])*)?([eE][-+]?[0-9]+)?")
                if(validNumberPattern.matches(number)){
                    number = number.replace("_", "")
                    tokens.add(Token(number, TokenType.Number, line, col))
                    col += number.length
                }
                else{
                    throw Exception("Unexpected token at [$line:$col]")
                }
            }

            // Identifiers
            else if(sourceCode[0].isAlpha()){
                var identifier = sourceCode.removeAt(0)
                while (sourceCode.isNotEmpty() && sourceCode[0].isValidVariableChar()){
                    identifier += sourceCode.removeAt(0)
                }
                // Check for reserved lexer.getKeywords
                val reserved = keywords[identifier]
                if (reserved != null){
                    tokens.add(Token(identifier, reserved, line, col))
                }
                else {
                    tokens.add(Token(identifier, TokenType.Identifier, line, col))
                }
                col += identifier.length
            }

            else {
                // Something unexpected
                throw Exception("Unexpected token found at[$line:$col]: '${sourceCode[0]}'")
            }
        }
    }
    tokens.add(Token("EOF", TokenType.EOF, line, col))
    return tokens
}