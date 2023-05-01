import interpreter.values.Niz
import interpreter.values.RuntimeValue

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
    val map = hashMapOf<String, String>(
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

    return functionName ?: throw Exception("Invalid operator $operator")
}