package interpreter.packages.telnet

import interpreter.values.*
import interpreter.values.bytes.Bajt
import interpreter.values.bytes.BajtNiz
import org.apache.commons.net.telnet.TelnetClient

class TelnetClientFactory {
    companion object{
        fun construct(): Objekat{
            val client = TelnetClient()

            return Objekat(properties = hashMapOf(
                "uspostaviKonekciju" to NativeFunction("uspostaviKonekciju"){ args ->
                    if(args.size != 2 || args[0] !is Tekst || args[1] !is Broj){
                        throw Exception("Argument mismatch: Function 'uspostaviKonekciju' accepts 2 arguments (host: Tekst, port: Broj)")
                    }
                    val hostname = (args[0] as Tekst).value
                    val port = (args[1] as Broj).value.toInt()
                    try{
                        client.connect(hostname, port)
                    }
                    catch (e: Exception){
                        throw Exception("Couldn't establish connection with $hostname:$port")
                    }

                    return@NativeFunction Null()
                },

                "zatvoriKonekciju" to NativeFunction("zatvoriKonekciju"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch: Function 'zatvoriKonekciju' accepts 0 arguments")
                    }

                    try{
                        client.disconnect()
                    }
                    catch (e: Exception){
                        throw Exception("Failed to disconnect...")
                    }

                    return@NativeFunction Null()
                },

                "izlazniTok" to NativeFunction("izlazniTok"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch: Function 'izlazniTok' accepts 0 arguments")
                    }

                    try{
                        val os = client.outputStream
                        Objekat(properties = hashMapOf(
                            "posalji" to NativeFunction("posalji"){ args ->
                                if(args.size != 1 || args[0] !is Tekst){
                                    throw Exception("Argument mismatch")
                                }
                                try{
                                    val msg = (args[0] as Tekst).value.toByteArray()
                                    os.write(msg)
                                }
                                catch (e: Exception){
                                    throw Exception("Unable to write to output stream")
                                }
                                Null()
                            },
                            "isprazni" to NativeFunction("isprazni"){ args ->
                                if(args.isNotEmpty()){
                                    throw Exception("Argument mismatch")
                                }
                                try{
                                    os.flush()
                                }
                                catch (e: Exception){
                                    throw Exception("Unable to flush output stream")
                                }
                                Null()
                            },
                            "zatvoriTok" to NativeFunction("zatvoriTok"){ args ->
                                if(args.isNotEmpty()){
                                    throw Exception("Argument mismatch")
                                }
                                try{
                                    os.close()
                                }
                                catch (e: Exception){
                                    throw Exception("Unable to close output stream")
                                }
                                Null()
                            }
                        ))
                    }
                    catch (e: Exception){
                        throw Exception("Cannot establish output stream...")
                    }
                },

                "ulazniTok" to NativeFunction("ulazniTok"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Argument mismatch: Function 'izlazniTok' accepts 0 arguments")
                    }

                    try {
                        val inputStream = client.inputStream
                        Objekat(properties = hashMapOf(
                            "zatvoriTok" to NativeFunction("zatvoriTok"){
                                try {
                                    inputStream.close()
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
                                    val bytesRead = inputStream.read(buffer)
                                    val data = buffer.copyOf(bytesRead)
                                    BajtNiz(data)
                                }
                                catch (e: Exception){
                                    throw Exception("Unable to read line from input stream")
                                }
                            },
                            "ucitajLiniju" to NativeFunction("ucitajLiniju"){ args ->
                                if(args.isNotEmpty()){
                                    throw Exception("Argument mismatch")
                                }
                                try {
                                    val msg = inputStream.bufferedReader().readLine()
                                    Tekst(msg)
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
                                    val bytes = inputStream.readAllBytes()
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
                                    val n = inputStream.available()
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
                                    val actuallySkipped = inputStream.skip(n)
                                    Broj(actuallySkipped.toDouble())
                                }
                                catch (e: Exception){
                                    throw Exception("Unable to skip ${args[0].value} bytes in input stream")
                                }
                            }
                        ))
                    }
                    catch (e: Exception){
                        throw Exception("Unable to establish input stream")
                    }
                }
            ))
        }
    }
}