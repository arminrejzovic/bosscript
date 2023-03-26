package interpreter.packages

import interpreter.*
import interpreter.values.*
import java.io.File

val IO = Environment(
    variables = hashMapOf(
        "datoteka" to object : NativeFunkcija(name = "datoteka"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1){
                    throw Exception("Function 'datoteka' accepts only one argument (file: Tekst)")
                }
                if (args[0] !is Tekst){
                    throw Exception("Type Error: file must be Tekst, got ${args[0].javaClass.simpleName}")
                }

                val fileName = (args[0] as Tekst).value

                val file = File(fileName)

                return Objekat(
                    properties = hashMapOf(),
                    builtIns = hashMapOf(
                        "sadrzaj" to Tekst(value = file.readText()),

                        "postaviTekst" to object : NativeFunkcija(name = "postaviTekst"){
                            override fun call(vararg args: RuntimeValue): RuntimeValue {
                                if(args.size != 1){
                                    throw Exception("Function 'postaviTekst' accepts only one argument (tekst: Tekst)")
                                }
                                if (args[0] !is Tekst){
                                    throw Exception("Type Error: tekst must be Tekst, got ${args[0].javaClass.simpleName}")
                                }

                                val text = (args[0] as Tekst).value
                                file.writeText(text)

                                return Null()
                            }
                        },

                        "linije" to object : NativeFunkcija(name = "linije"){
                            override fun call(vararg args: RuntimeValue): RuntimeValue {
                                val lines = file.readLines().map {Tekst(value=it)} as ArrayList<RuntimeValue>
                                return Niz(value = lines)
                            }
                        },

                        "dopisi" to object : NativeFunkcija(name = "dopisi"){
                            override fun call(vararg args: RuntimeValue): RuntimeValue {
                                if(args.size != 1){
                                    throw Exception("Function 'dopisi' accepts only one argument (tekst: Tekst)")
                                }

                                val text = args[0].toString()
                                file.appendText(text)

                                return Null()
                            }
                        },
                    )
                )
            }

        }
    )
)