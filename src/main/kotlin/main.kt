import interpreter.Interpreter
import java.io.File

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val filename = args[0]
        val file = File(filename)

        if (file.exists()) {
            val interpreter = Interpreter()
            interpreter.evaluateProgram(file.readText())
        } else {
            throw Exception("No such file found: $filename")
        }
    }
    else {
        throw Exception("No file to evaluate")
    }
}