package interpreter.packages.telnet

import interpreter.values.*
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
                        StreamObjectFactory.constructInputStream(client.inputStream)
                    }
                    catch (e: Exception){
                        throw Exception("Unable to establish input stream")
                    }
                }
            ))
        }
    }
}