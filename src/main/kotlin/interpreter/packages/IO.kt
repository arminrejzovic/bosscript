package interpreter.packages

import interpreter.*
import interpreter.values.*
import java.io.File

val IO = Environment(
    variables = hashMapOf(
        "datoteka" to NativeFunction(name = "datoteka"){args ->
            if(args.size != 1){
                throw Exception("Function 'datoteka' accepts only one argument (file: Tekst)")
            }
            if (args[0] !is Tekst){
                throw Exception("Type Error: file must be Tekst, got ${args[0].javaClass.simpleName}")
            }

            val fileName = (args[0] as Tekst).value

            val file = File(fileName)

            return@NativeFunction Objekat(
                properties = hashMapOf(
                    "sadrzaj" to Tekst(value = file.readText()),

                    "postaviTekst" to NativeFunction(name = "postaviTekst"){nestedArgs ->
                        if(nestedArgs.size != 1){
                            throw Exception("Function 'postaviTekst' accepts only one argument (tekst: Tekst)")
                        }
                        if (nestedArgs[0] !is Tekst){
                            throw Exception("Type Error: tekst must be Tekst, got ${nestedArgs[0].javaClass.simpleName}")
                        }

                        val text = (nestedArgs[0] as Tekst).value
                        file.writeText(text)

                        Null()
                    },

                    "linije" to NativeFunction(name = "linije"){ _ ->
                        val lines = file.readLines().map {Tekst(value=it)} as ArrayList<RuntimeValue>
                        Niz(value = lines)
                    },

                    "dopisi" to NativeFunction(name = "dopisi"){ nestedArgs ->
                        if(nestedArgs.size != 1){
                            throw Exception("Function 'dopisi' accepts only one argument (tekst: Tekst)")
                        }

                        val text = nestedArgs[0].toString()
                        file.appendText(text)

                        Null()
                    },
                )
            )
        }
    )
)