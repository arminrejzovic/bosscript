
import interpreter.values.Niz
import interpreter.values.RuntimeValue

fun Double.isInt(): Boolean {
    return this % 1 == 0.0
}

fun Char.isAlpha(): Boolean{
    return this.isLetter() || this == '$' || this == '_'
}

fun Char.isValidVariableChar(): Boolean{
    return this.isAlpha() || this.isNumeric() || this == '$' || this == '_'
}

fun Char.isNumeric(): Boolean{
    this.isLetter()
    return this.isDigit()
}

fun Char.isIgnoredWhitespace(): Boolean{
    return this == ' ' || this == '\r' || this == '\t'
}

fun ArrayList<RuntimeValue>.flatten(): ArrayList<RuntimeValue> {
    val flattenedList = arrayListOf<RuntimeValue>()
    for (element in this) {
        when (element) {
            is Niz -> flattenedList.addAll(element.value.flatten())
            else -> flattenedList.add(element)
        }
    }
    return flattenedList
}

fun operatorToFunctionName(operator: String): String{
    val map = hashMapOf(
        "+" to "plus",
        "-" to "minus",
        "*" to "puta",
        "/" to "podijeljeno",
        "<" to "manjeOd",
        ">" to "veceOd",
        "==" to "jednako",
        "!=" to "nijeJednako"
    )

    val functionName = map[operator]

    return functionName ?: throw Exception("Pronađen nepodržani operator $operator")
}