fun main(args: Array<String>) {
    val parser = Parser()
    val env = Environment()

    env.declareVariable("pi", NumberValue(3.14159), isConstant = true)
    env.declareVariable("tacno", BoolValue(true), isConstant = true)
    env.declareVariable("netacno", BoolValue(false), isConstant = true)
    env.declareVariable("nista", NullValue(), isConstant = true)

    println("Bosscript 0.0.1")
    println("(c) eMatura doo. All rights reserved.")
    while (true){
        print("> ")
        val input = readLine()
        if(input == null || input.lowercase() == "exit"){
            return
        }
        val program = parser.generateAST(input)
        val result = interpret(program, env)
        println(result.toString().toIndentString())
    }
}