import udemy.Interpreter

fun main() {
    val src = """
            var a = unos();
            ispis(a);
        """.trimIndent()

    val interpreter = Interpreter()
    val result = interpreter.evaluateProgram(src)
}