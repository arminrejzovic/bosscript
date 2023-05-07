import interpreter.Interpreter
import transpiler.Transpiler
import java.io.File

fun main(args: Array<String>) {
    if (args.size == 1) {
        // Default
        val filename = args[0]
        if(!filename.endsWith(".boss")){
            throw Exception("Expected .boss file")
        }
        val file = File(filename)

        if (file.exists()) {
            val interpreter = Interpreter()
            interpreter.evaluateProgram(file.readText())
        } else {
            throw Exception("No such file found: $filename")
        }
    }
    else if(args.size == 2){
        val bossName = args[0]
        val jsName = args[1]

        if(!bossName.endsWith(".boss")){
            throw Exception("Expected .boss file")
        }
        if(!jsName.endsWith(".js")){
            throw Exception("Expected JavaScript file as transpilation target")
        }

        val bossFile = File(bossName)
        val jsFile = File(jsName)

        if(!bossFile.exists()){
            throw Exception("No such file found: $bossName")
        }

        if(!jsFile.exists()){
            jsFile.createNewFile()
        }

        val transpiler = Transpiler()
        val js = transpiler.transpileProgram(bossFile.readText())

        jsFile.writeText(js)

        println("[INFO] Finished transpiling to Javascript")
        println("Check ${jsFile.absolutePath}")
    }
    else {
        throw Exception("No file to evaluate")
    }
}