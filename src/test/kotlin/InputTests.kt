import udemy.Interpreter
import udemy.Object
import udemy.Text

fun main() {
    val src = """
            funkcija m(){
               var a = unos();
                ispis(a);
            }
            m();
        """.trimIndent()

    val interpreter = Interpreter()
    //interpreter.evaluateProgram(src)

    val envTest = Environment(variables = hashMapOf(
        "x" to Object(
            properties = hashMapOf(
                "y" to Object(
                    properties = hashMapOf(
                        "z" to Text(
                            value = "Hello world"
                        )
                    )
                )
            )
        )
    ))

    val obj = envTest.getVariable("x")
    obj as Object
    val objY = obj.properties["y"]
    objY as Object
    objY.properties["z"] = Text("Привет мир!")

    println(envTest.getVariable("x"))
}