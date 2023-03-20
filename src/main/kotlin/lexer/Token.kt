package lexer

data class Token(
    val value: String,
    val type: TokenType,
    val line: Int,
    val col: Int
){
    fun getLineCol() = "$line:$col"
}
