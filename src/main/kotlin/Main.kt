import java.io.File

fun main(args: Array<String>) {
    val parser = Parser()
    val env = Environment()


    println("Bosscript 0.0.1")
    println("(c) eMatura doo. All rights reserved.")
    println()
    println()
    /*
    while (true){
        print("> ")
        val input = readLine()
        if(input == null || input.lowercase() == "exit"){
            return
        }
        val program = parser.generateAST(input)
        val result = interpret(program, env)
        println(result.toString())
    }
     */

    val testFile = File("C:\\Users\\armin\\IdeaProjects\\bosscript\\src\\test\\kotlin\\test.boss").readText()
    val program = parser.generateAST(testFile)
    val result = interpret(program, env)
    println(result.toString())
}