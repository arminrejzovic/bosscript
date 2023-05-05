package interpreter.packages.io

import interpreter.*
import interpreter.values.*
import java.io.File

val IO = Environment(
    variables = hashMapOf(
        "Datoteka" to NativeFunction(name = "Datoteka"){args ->
            if(args.size != 1){
                throw Exception("Function 'Datoteka' accepts only one argument (filename: Tekst)")
            }
            if (args[0] !is Tekst){
                throw Exception("Type Error: filename must be Tekst, got ${args[0].javaClass.simpleName}")
            }

            val fileName = (args[0] as Tekst).value

            val file = File(fileName)

            return@NativeFunction FileObject(file).construct()
        }
    )
)