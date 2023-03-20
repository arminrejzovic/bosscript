import interpreter.Environment
import interpreter.Interpreter
import interpreter.Objekat
import interpreter.Tekst

fun main() {
    val src = """
            funkcija m(){
               var a = unos();
                ispis(a);
            }
            m();
            var x = 10;
            ako(x > 1 && x > 2){
                ispis("10!");
            }
        """.trimIndent()

    val interpreter = Interpreter()
    interpreter.evaluateProgram(src)

    val envTest = Environment(variables = hashMapOf(
        "x" to Objekat(
            properties = hashMapOf(
                "y" to Objekat(
                    properties = hashMapOf(
                        "z" to Tekst(
                            value = "Hello world"
                        )
                    )
                )
            )
        )
    ))

    val obj = envTest.getVariable("x")
    obj as Objekat
    val objY = obj.properties["y"]
    objY as Objekat
    objY.properties["z"] = Tekst("Привет мир!")

    println(envTest.getVariable("x"))
}