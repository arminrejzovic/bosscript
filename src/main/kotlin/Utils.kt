fun Double.isInt(): Boolean {
    return this % 1 == 0.0
}

fun String.isAlpha(): Boolean{
    return (this.lowercase() != this.uppercase()) || this == "$" || this == "_"
}

fun String.isValidVariableChar(): Boolean{
    return this.isAlpha() || this.isNumeric() || this == "$" || this == "_"
}

fun String.isNumeric(): Boolean{
    return this.toDoubleOrNull() != null
}

fun String.isIgnoredWhitespace(): Boolean{
    return this == " " || this == "\r" || this == "\t"
}