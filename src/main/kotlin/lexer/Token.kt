package lexer

data class Token(
    val value: String,
    val type: TokenType,
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>
){
    fun getLineCol() = "${start.first}:${start.second}"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (value != other.value) return false
        return type == other.type
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
