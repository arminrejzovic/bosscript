package interpreter.packages.io

import interpreter.Environment
import interpreter.values.NativeFunction
import interpreter.values.Tekst
import java.io.File

val IO = Environment(
    variables = hashMapOf(
        "Datoteka" to NativeFunction(name = "Datoteka"){args ->
            if(args.size != 1 || args[0] !is Tekst){
                throw Exception("Funkcija 'Datoteka' prihvata 1 argument (ime: tekst) (pronađeno ${args.size})")
            }

            val fileName = (args[0] as Tekst).value

            val file = File(fileName)

            return@NativeFunction FileObject(file).construct()
        }
    )
)