package lexer

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
            tokens.add(Token(src[cursor++].toString(), TokenType.OpenParen, line, col++))
        }
        else if(src[cursor] == ')'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseParen, line, col++))
        }
        else if(src[cursor] == '['){
            tokens.add(Token(src[cursor++].toString(), TokenType.OpenBracket, line, col++))
        }
        else if(src[cursor] == ']'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseBracket, line, col++))
        }
        else if(src[cursor] == '{'){
            tokens.add(Token(src[cursor++].toString(), TokenType.OpenBrace, line, col++))
        }
        else if(src[cursor] == '}'){
            tokens.add(Token(src[cursor++].toString(), TokenType.CloseBrace, line, col++))
        }
        else if(src[cursor] == '^'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Exponent, line, col++))
        }
        else if(src[cursor] == '&'){
            if(src.length > 1 && src[cursor + 1] == '&'){
                tokens.add(Token(value = "&&", TokenType.LogicalAnd, line, col))
                cursor += 2
                col += 2
            }
            else{
                throw Exception("Unexpected token found at[$line:$col]: '${src[cursor]}'")
            }
        }
        else if(src[cursor] == '|'){
            if(src.length > 1 && src[cursor + 1] == '|'){
                tokens.add(Token(value = "||", TokenType.LogicalOr, line, col))
                cursor += 2
                col += 2
            }
            else{
                throw Exception("Unexpected token found at[$line:$col]: '${src[cursor]}'")
            }
        }
        else if(src[cursor] == '+' || src[cursor] == '-' || src[cursor] == '*' || src[cursor] == '/' || src[cursor] == '%'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.ComplexAssign, line, col))
                col+=2
            }
            else if(src.length > 1 && src[cursor] == '+' && src[cursor + 1] == '+'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.UnaryIncrement, line, col))
                col+=2
            }
            else if(src.length > 1 && src[cursor] == '-' && src[cursor + 1] == '-'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.UnaryDecrement, line, col))
                col+=2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.BinaryOperator, line, col++))
            }
        }
        else if(src[cursor] == '='){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.EqualityOperator, line, col))
                col += 2
            }
            else if(src.length > 1 && src[cursor + 1] == '>'){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.Arrow, line, col))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.SimpleAssign, line, col++))
            }
        }
        else if(src[cursor] == '!'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.EqualityOperator, line, col))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.LogicalNot, line, col++))
            }
        }
        else if(src[cursor] == ':'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Colon, line, col++))
        }
        else if(src[cursor] == ';'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Semicolon, line, col++))
        }
        else if(src[cursor] == ','){
            tokens.add(Token(src[cursor++].toString(), TokenType.Comma, line, col++))
        }
        else if(src[cursor] == '.'){
            tokens.add(Token(src[cursor++].toString(), TokenType.Dot, line, col++))
        }
        else if(src[cursor] == '@'){
            tokens.add(Token(src[cursor++].toString(), TokenType.This, line, col++))
        }
        else if(src[cursor] == '<' || src[cursor] == '>'){
            if(src.length > 1 && src[cursor + 1] == '='){
                tokens.add(Token(value = "${src[cursor++]}${src[cursor++]}", TokenType.RelationalOperator, line, col))
                col += 2
            }
            else{
                tokens.add(Token(src[cursor++].toString(), TokenType.RelationalOperator, line, col++))
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
                tokens.add(Token(src[cursor++].toString(), TokenType.DoubleQuote, line, col++))
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
                tokens.add(Token(string, TokenType.String, line, col))
                col += string.length

                // If exists, add closing quotation mark
                if(src.isNotEmpty() && src[cursor] == '"'){
                    tokens.add(Token(src[cursor++].toString(), TokenType.DoubleQuote, line, col++))
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
                    number = number.replace("_", "")
                    tokens.add(Token(number, TokenType.Number, line, col))
                    col += number.length
                }
                else{
                    throw Exception("Unexpected token at [$line:$col]")
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
                    tokens.add(Token(identifier, reserved, line, col))
                }
                else {
                    tokens.add(Token(identifier, TokenType.Identifier, line, col))
                }
                col += identifier.length
            }

            else if(src[cursor] == '`'){
                if(!js){
                    throw Exception("Javascript snippets are not allowed here.")
                }
                val sb = StringBuilder("")
                src[cursor++].toString()
                while (src.isNotEmpty() && src[cursor] != '`'){
                    if(src[cursor] == '\n'){
                        line++
                        col = 1
                    }
                    sb.append(src[cursor++].toString())
                    col++
                }
                if(src[cursor] != '`'){
                    throw Exception("Missing closing backtick")
                }
                src[cursor++].toString()
                tokens.add(Token(sb.toString(), TokenType.Javascript, line, col))
            }

            else {
                // Something unexpected
                throw Exception("Unexpected token found at[$line:$col]: '${src[cursor]}'")
            }
        }
    }
    tokens.add(Token("EOF", TokenType.EOF, line, col))
    return tokens
}