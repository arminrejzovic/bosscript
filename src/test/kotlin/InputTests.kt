import udemy.Interpreter

fun main() {
    val src = """
            funkcija m(){
               var a = unos();
                ispis(a);
            }
            m();
        """.trimIndent()

    val interpreter = Interpreter()
    interpreter.evaluateProgram(src)

    val src2 = """
        ispis(nedefinisano);
        """.trimIndent()
    interpreter.evaluateProgram(src2)
}