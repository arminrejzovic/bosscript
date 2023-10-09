package lexer

import errors.BosscriptSyntaxError
import errors.BosscriptTokenException
import isAlpha
import isIgnoredWhitespace
import isNumeric
import isValidVariableChar

val keywords = mutableMapOf(
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
    "privatno" to TokenType.Private,
    "javno" to TokenType.Public,
    "opsta" to TokenType.Static,
    "opšta" to TokenType.Static,
    "konstruktor" to TokenType.Constructor,
)

fun tokenize(src: String, js: Boolean): ArrayDeque<Token>{
    val tokens = ArrayDeque<Token>()
    var line = 1
    var col = 1
    var cursor = 0

    while (cursor < src.length){
        if(src[cursor] == '('){
            tokens.add(
                Token(
                    value = src[cursor++].toString(),
                    type = TokenType.OpenParen,
                    start = Pair(line, col++),
                    end = Pair(line, col))
            )
        }
        else if(src[cursor] == ')'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseParen, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '['){
            tokens.add(Token(src[cursor++].toString(), TokenType.OpenBracket, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == ']'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseBracket, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '{'){
            tokens.add(Token(src[cursor++].toString(), TokenType.OpenBrace, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '}'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseBrace, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '^'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Exponent, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '&'){
            if(src.length > 1 && src[cursor + 1] == '&'){
                tokens.add(Token(value = "&&", TokenType.LogicalAnd, start = Pair(line, col), end = Pair(line, col + 2)))
                cursor += 2
                col += 2
            }
            else{
                throw BosscriptTokenException(src[cursor], line, col)
            }
        }
        else if(src[cursor] == '|'){
            if(src.length > 1 && src[cursor + 1] == '|'){
                tokens.add(Token(value = "||", TokenType.LogicalOr, start = Pair(line, col), end = Pair(line, col + 2)))
                cursor += 2
                col += 2
            }
            else{
                throw BosscriptTokenException(src[cursor], line, col)
            }
        }
        else if(src[cursor] == '+' || src[cursor] == '-' || src[cursor] == '*' || src[cursor] == '/' || src[cursor] == '%'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.ComplexAssign, start = Pair(line, col), end = Pair(line, col + 2)))
                col+=2
            }
            else if(src.length > 1 && src[cursor] == '+' && src[cursor + 1] == '+'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.UnaryIncrement, start = Pair(line, col), end = Pair(line, col + 2)))
                col+=2
            }
            else if(src.length > 1 && src[cursor] == '-' && src[cursor + 1] == '-'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.UnaryDecrement, start = Pair(line, col), end = Pair(line, col + 2)))
                col+=2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.BinaryOperator, start = Pair(line, col++), end = Pair(line, col)))
            }
        }
        else if(src[cursor] == '='){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.EqualityOperator, start = Pair(line, col), end = Pair(line, col + 2)))
                col += 2
            }
            else if(src.length > 1 && src[cursor + 1] == '>'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.Arrow, start = Pair(line, col), end = Pair(line, col + 2)))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.SimpleAssign, start = Pair(line, col++), end = Pair(line, col)))
            }
        }
        else if(src[cursor] == '!'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.EqualityOperator, start = Pair(line, col), end = Pair(line, col + 2)))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.LogicalNot, start = Pair(line, col++), end = Pair(line, col)))
            }
        }
        else if(src[cursor] == '?'){
            if(src[cursor + 1] == ':'){
               tokens.add(Token("${src[cursor++]}${src[cursor++]}", TokenType.BinaryOperator, start = Pair(line, col), end = Pair(line, col + 2)))
               col += 2
            }
        }
        else if(src[cursor] == ':'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Colon, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == ';'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Semicolon, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == ','){
            tokens.add(Token(src[cursor++].toString(), TokenType.Comma, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '.'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Dot, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '@'){
            tokens.add(Token(src[cursor++].toString(), TokenType.This, start = Pair(line, col++), end = Pair(line, col)))
        }
        else if(src[cursor] == '<' || src[cursor] == '>'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.RelationalOperator, start = Pair(line, col), end = Pair(line, col + 2)))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.RelationalOperator, start = Pair(line, col++), end = Pair(line, col)))
            }
        }
        else if(src[cursor] == '\n'){
            line++
            col = 1
            cursor++
        }
        else if(src[cursor].isIgnoredWhitespace()){
            cursor++
            col += 1
        }
        else{
            // Multi-character tokens
            //String literals
            if(src[cursor] == '"'){
                // Add the opening quotation mark
                tokens.add(Token(src[cursor++].toString(), TokenType.DoubleQuote, start = Pair(line, col++), end = Pair(line, col)))
                var string = ""
                while (cursor < src.length && src[cursor] != '"'){
                    if(src[cursor] == '\\' && src.length > 1){
                        when(src[cursor + 1]){
                            'n' -> string += "\n"
                            't'-> string += "\t"
                            'r' -> string += "\r"
                            '\\' -> string += "\\"
                            '"' -> string += "\""
                        }
                        cursor += 2
                    }
                    else{
                        string += src[cursor++]
                    }
                }
                // Add the string value
                tokens.add(Token(string, TokenType.String, start = Pair(line, col), end = Pair(line, col + string.length)))
                col += string.length

                // If exists, add closing quotation mark
                if(src.isNotEmpty() && src[cursor] == '"'){
                    tokens.add(Token(src[cursor++].toString(), TokenType.DoubleQuote, start = Pair(line, col++), end = Pair(line, col)))
                }
            }

            // Numbers
            else if(src[cursor].isNumeric()){
                var number = ""
                while (src.isNotEmpty() && src[cursor].isNumeric() || src[cursor] == '_' || src[cursor] == '.'){
                    number += src[cursor++]
                }
                val validNumberPattern = Regex("^-?(0|[1-9](_?[0-9])*)(\\.[0-9](_?[0-9])*)?([eE][-+]?[0-9]+)?")
                if(validNumberPattern.matches(number)){
                    val validatedNumber = number.replace("_", "")
                    tokens.add(Token(validatedNumber, TokenType.Number, start = Pair(line, col), end = Pair(line, col + validatedNumber.length)))
                    col += validatedNumber.length
                }
                else{
                    throw BosscriptTokenException(src[cursor], line, col)
                }
            }

            // Identifiers
            else if(src[cursor].isAlpha()){
                var identifier = src[cursor++].toString()
                while (cursor < src.length && src[cursor].isValidVariableChar()){
                    identifier += src[cursor++]
                }
                // Check for reserved lexer.getKeywords
                val reserved = keywords[identifier]
                if (reserved != null){
                    tokens.add(Token(identifier, reserved, start = Pair(line, col), end = Pair(line, col + identifier.length)))
                }
                else {
                    tokens.add(Token(identifier, TokenType.Identifier, start = Pair(line, col), end = Pair(line, col + identifier.length)))
                }
                col += identifier.length
            }

            // JavaScript snippets
            else if(src[cursor] == '`'){
                if(!js){
                    throw BosscriptSyntaxError("Neočekivan JavaScript kod. Transpilacija nije uključena.")
                }
                val sb = StringBuilder("")
                src[cursor++].toString() // this is to remove the backtick
                val start = Pair(line, col)
                while (src.isNotEmpty() && src[cursor] != '`'){
                    if(src[cursor] == '\n'){
                        line++
                        col = 1
                    }
                    sb.append(src[cursor++].toString())
                    col++
                }
                if(src[cursor] != '`'){
                    throw BosscriptSyntaxError("Nedostaje ` na kraju JavaScript koda.")
                }
                src[cursor++].toString() // this is to remove the backtick
                tokens.add(Token(sb.toString(), TokenType.Javascript, start, Pair(line, col)))
            }

            // Comments
            else if (src[cursor] == '#'){
                while (src[cursor] != '\n'){
                    cursor++
                }
                line++
                col = 1
            }

            else {
                // Something unexpected
                throw BosscriptTokenException(src[cursor], line, col)
            }
        }
    }
    tokens.add(Token("EOF", TokenType.EOF, Pair(Int.MAX_VALUE, Int.MAX_VALUE), Pair(Int.MAX_VALUE, Int.MAX_VALUE)))
    return tokens
}