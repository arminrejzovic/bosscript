fun String.toIndentString(): String = buildString(length) {
    var indent = 0

    fun line() {
        appendLine()
        repeat(2 * indent) { append(' ') }
    }

    this@toIndentString.filter { it != ' ' }.forEach { char ->
        when (char) {
            ')', ']', '}' -> {
                indent--
                line()
                append(char)
            }
            '=' -> append(" = ")
            '(', '[', '{', ',' -> {
                append(char)
                if (char != ',') indent++
                line()
            }
            else -> append(char)
        }
    }
}