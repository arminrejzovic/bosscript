import interpreter.Interpreter
import interpreter.values.*
import interpreter.values.classes.ModelObject
import lexer.Token
import lexer.tokenize
import parser.Parser
import java.io.File
import java.lang.StringBuilder
import kotlin.system.measureTimeMillis

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

fun main(){
    val filename = "C:\\Users\\Armin\\CLionProjects\\bosscript\\boss\\test.boss"

    val file = File(filename)

    if (file.exists()) {
        val src = file.readText()
        val executionTime = measureTimeMillis {
            val parser = Parser(false)
            parser.parseProgram(src)
        }
        println("Program parsed $executionTime ms")
    } else {
        throw Exception("No such file found: $filename")
    }
}