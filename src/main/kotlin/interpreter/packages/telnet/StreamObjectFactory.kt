package interpreter.packages.telnet

import interpreter.values.*
import interpreter.values.bytes.BajtNiz
import java.io.InputStream
import java.io.OutputStream

class StreamObjectFactory {
    companion object{
        fun constructInputStream(stream: InputStream): ReadonlyObject{
            return ReadonlyObject(hashMapOf(
                "zatvoriTok" to NativeFunction("zatvoriTok"){
                    try {
                        stream.close()
                    }
                    catch (e: Exception){
                        throw Exception("Unable to close input stream...")
                    }
                    Null()
                },
                "ucitaj" to NativeFunction("ucitaj"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val bufferSize = (args[0] as Broj).value.toInt()
                        val buffer = ByteArray(bufferSize)
                        val bytesRead = stream.read(buffer)
                        val data = buffer.copyOf(bytesRead)
                        BajtNiz(data)
                    }
                    catch (e: Exception){
                        throw Exception("Unable to read line from input stream")
                    }
                },
                "ucitajPreostaleBajte" to NativeFunction("ucitajPreostaleBajte"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val bytes = stream.readAllBytes()
                        BajtNiz(bytes)
                    }
                    catch (e: Exception){
                        throw Exception("Unable to read line from input stream")
                    }
                },
                "dostupnihBajtova" to NativeFunction("dostupnihBajtova"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val n = stream.available()
                        Broj(n.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Unable to determine available bytes in input stream")
                    }
                },
                "preskoci" to NativeFunction("preskoci"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = stream.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Unable to skip ${args[0].value} bytes in input stream")
                    }
                },
                "citac" to NativeFunction("citac"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    BufferObjectFactory.constructReader(stream.bufferedReader())
                }
            ))
        }

        fun constructOutputStream(stream: OutputStream): Objekat{
            return Objekat(hashMapOf(
                "posaljiBajt" to NativeFunction("posaljiBajt"){args ->
                    if(args.size != 1 || args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    val arg = (args[0] as Broj).value.toInt()
                    stream.write(arg)
                    Null()
                },
                "posalji" to NativeFunction("posalji"){args ->
                    if(args.size != 1 || args[0] !is BajtNiz){
                        throw Exception("Argument mismatch")
                    }
                    val arg = (args[0] as BajtNiz).value
                    stream.write(arg)
                    Null()
                },
                "isprazni" to NativeFunction("isprazni"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    stream.flush()
                    Null()
                },
                "zatvori" to NativeFunction("zatvori"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    stream.close()
                    Null()
                },

                "pisac" to NativeFunction("pisac"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch")
                    }
                    BufferObjectFactory.constructWriter(stream.bufferedWriter())
                }
            ))
        }
    }
}