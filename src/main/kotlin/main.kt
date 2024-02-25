import interpreter.Interpreter
import transpiler.Transpiler
import java.io.File

fun main(args: Array<String>) {

    Thread.setDefaultUncaughtExceptionHandler { _, x ->
        System.err.println("Došlo je do greške prilikom pokretanja programa.")
        System.err.println(x.message)
    }

    if (args.size == 1) {
        // Default
        val filename = args[0]
        if(!filename.endsWith(".boss")){
            throw Exception("Datoteka '$filename' je nepodržanog tipa. Očekuje se datoteka sa '.boss' ekstenzijom.")
        }
        val file = File(filename)

        if (file.exists()) {
            val interpreter = Interpreter()
            interpreter.evaluateProgram(file.readText())
        } else {
            throw Exception("Datoteka pod imenom '$filename' ne postoji.")
        }
    }
    else if(args.size == 2){
        val bossName = args[0]
        val jsName = args[1]

        if(!bossName.endsWith(".boss")){
            throw Exception("Datoteka '$bossName' je nepodržanog tipa. Očekuje se datoteka sa '.boss' ekstenzijom.")
        }
        if(!jsName.endsWith(".js")){
            throw Exception("Pronađena datoteka nepodržanog tipa. Destinacija transpilacije mora biti JavaScript datoteka.")
        }

        val bossFile = File(bossName)
        val jsFile = File(jsName)

        if(!bossFile.exists()){
            throw Exception("Datoteka pod imenom '$bossFile' ne postoji.")
        }

        if(!jsFile.exists()){
            jsFile.createNewFile()
        }

        val transpiler = Transpiler()
        val js = transpiler.transpileProgram(bossFile.readText())

        jsFile.writeText(js)

        println("[INFO] Transpilacija završena")
        println("Provjerite datoteku ${jsFile.absolutePath}")
    }
    else {
        throw Exception("Nije pronađen nijedan Bosscript fajl.")
    }
}