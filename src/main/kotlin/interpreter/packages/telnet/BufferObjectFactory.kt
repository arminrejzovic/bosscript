package interpreter.packages.telnet

import interpreter.values.*
import java.io.BufferedReader
import java.io.BufferedWriter

class BufferObjectFactory {
    companion object {
        fun constructReader(reader: BufferedReader): Objekat {
            return Objekat(hashMapOf(
                "zatvoriTok" to NativeFunction("zatvoriTok"){
                    try {
                        reader.close()
                    }
                    catch (e: Exception){
                        throw Exception("Unable to close input stream...")
                    }
                    Null()
                },
                "jeSpreman" to NativeFunction("jeSpreman"){
                    Logicki(reader.ready())
                },
                "ucitaj" to NativeFunction("ucitaj"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    reader.read()
                    TODO()
                },
                "ucitajLiniju" to NativeFunction("ucitajLiniju"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val msg = reader.readLine()
                        Tekst(msg)
                    }
                    catch (e: Exception){
                        throw Exception("Unable to read line from input stream")
                    }
                },

                "ucitajLinije" to NativeFunction("ucitajLinije"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val lines = reader.readLines()
                        val vals = ArrayList<RuntimeValue>(lines.map { Tekst(it) })
                        Niz(vals)
                    }
                    catch (e: Exception){
                        throw Exception("Unable to read lines from input stream")
                    }
                },

                "preskoci" to NativeFunction("preskoci"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = reader.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Unable to skip ${args[0].value} bytes in input stream")
                    }
                }
            ))
        }

        fun constructWriter(writer: BufferedWriter): Objekat {
            return Objekat(
                hashMapOf(
                    "ispisi" to NativeFunction("ispisi") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Argument mismatch")
                        }
                        val str = (args[0] as Tekst).value
                        writer.write(str)
                        Null()
                    },
                    "ispisiDio" to NativeFunction("ispisiDio") { args ->
                        if(args.size != 3 || args[0] !is Tekst || args[1] !is Broj || args[2] !is Broj){
                            throw Exception("Argument mismatch")
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
                    "dopisiLiniju" to NativeFunction("dopisi") { args ->
                        if(args.size != 1 || args[0] !is Tekst){
                            throw Exception("Argument mismatch")
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