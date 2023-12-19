package interpreter.packages.telnet

import interpreter.values.*
import java.io.BufferedReader
import java.io.BufferedWriter

class BufferObjectFactory {
    companion object {
        fun constructReader(reader: BufferedReader): ReadonlyObject {
            return ReadonlyObject(hashMapOf(
                "zatvoriTok" to NativeFunction("zatvoriTok"){
                    try {
                        reader.close()
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom zatvaranja ulaznog toka")
                    }
                    Null()
                },
                "jeSpreman" to NativeFunction("jeSpreman"){
                    Logicki(reader.ready())
                },
                "ucitaj" to NativeFunction("ucitaj"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'ucitaj' prihvata 1 argument (n: broj)")
                    }
                    reader.read()
                    TODO()
                },
                "ucitajLiniju" to NativeFunction("ucitajLiniju"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'ucitajLiniju' ne prihvata argumente.")
                    }
                    try {
                        val msg = reader.readLine()
                        Tekst(msg)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja linije iz ulaznog toka")
                    }
                },

                "ucitajLinije" to NativeFunction("ucitajLinije"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'ucitajLinije' ne prihvata argumente.")
                    }
                    try {
                        val lines = reader.readLines()
                        val vals = ArrayList<RuntimeValue>(lines.map { Tekst(it) })
                        Niz(vals)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja linija iz ulaznog toka")
                    }
                },

                "preskoci" to NativeFunction("preskoci"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'preskoci' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = reader.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom preskakanja znakova iz ulaznog toka")
                    }
                },

                "preskoči" to NativeFunction("preskoči"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'preskoči' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = reader.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom preskakanja znakova iz ulaznog toka")
                    }
                }
            ))
        }

        fun constructWriter(writer: BufferedWriter): Objekat {
            return Objekat(
                hashMapOf(
                    "ispisi" to NativeFunction("ispisi") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Funkcija 'ispisi' prihvata 1 argument (str: tekst) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        writer.write(str)
                        Null()
                    },
                    "ispiši" to NativeFunction("ispiši") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Funkcija 'ispiši' prihvata 1 argument (str: tekst) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        writer.write(str)
                        Null()
                    },
                    "ispisiDio" to NativeFunction("ispisiDio") { args ->
                        if(args.size != 3 || args[0] !is Tekst || args[1] !is Broj || args[2] !is Broj){
                            throw Exception("Funkcija 'ispisiDio' prihvata 3 argumenta (str: tekst, offset: broj, dužina: broj) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        val offset = (args[1] as Broj).value.toInt()
                        val length = (args[2] as Broj).value.toInt()
                        writer.write(str, offset, length)
                        Null()
                    },
                    "ispišiDio" to NativeFunction("ispišiDio") { args ->
                        if(args.size != 3 || args[0] !is Tekst || args[1] !is Broj || args[2] !is Broj){
                            throw Exception("Funkcija 'ispišiDio' prihvata 3 argumenta (str: tekst, offset: broj, dužina: broj) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        val offset = (args[1] as Broj).value.toInt()
                        val length = (args[2] as Broj).value.toInt()
                        writer.write(str, offset, length)
                        Null()
                    },
                    "noviRed" to NativeFunction("noviRed") {
                        writer.newLine()
                        Null()
                    },
                    "isprazni" to NativeFunction("isprazni") {
                        writer.flush()
                        Null()
                    },
                    "zatvori" to NativeFunction("zatvori") {
                        writer.close()
                        Null()
                    },
                    "dopisiLiniju" to NativeFunction("dopisiLiniju") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Funkcija 'dopisiLiniju' prihvata 1 argument (str: tekst) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        writer.appendLine(str)
                        Null()
                    },
                    "dopišiLiniju" to NativeFunction("dopišiLiniju") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Funkcija 'dopišiLiniju' prihvata 1 argument (str: tekst) (pronađeno ${args.size})")
                        }
                        val str = (args[0] as Tekst).value
                        writer.appendLine(str)
                        Null()
                    },
                )
            )
        }
    }
}