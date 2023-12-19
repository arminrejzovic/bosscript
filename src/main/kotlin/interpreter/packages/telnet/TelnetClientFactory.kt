package interpreter.packages.telnet

import interpreter.values.*
import org.apache.commons.net.telnet.TelnetClient

class TelnetClientFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val client = TelnetClient()

            return ReadonlyObject(properties = hashMapOf(
                "uspostaviKonekciju" to NativeFunction("uspostaviKonekciju"){ args ->
                    if(args.size != 2 || args[0] !is Tekst || args[1] !is Broj){
                        throw Exception("Funkcija 'uspostaviKonekciju' prihvata 2 argumenta (hostname: tekst, port: broj) (pronađeno ${args.size})")
                    }
                    val hostname = (args[0] as Tekst).value
                    val port = (args[1] as Broj).value.toInt()
                    try{
                        client.connect(hostname, port)
                    }
                    catch (e: Exception){
                        throw Exception("Nije moguće uspostaviti konekciju sa $hostname:$port")
                    }

                    return@NativeFunction Null()
                },

                "zatvoriKonekciju" to NativeFunction("zatvoriKonekciju"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'zatvoriKonekciju' ne prihvata argumente.")
                    }

                    try{
                        client.disconnect()
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom zatvaranja konekcije")
                    }

                    return@NativeFunction Null()
                },

                "izlazniTok" to NativeFunction("izlazniTok"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'izlazniTok' ne prihvata argumente.")
                    }

                    try{
                        val os = client.outputStream
                        Objekat(properties = hashMapOf(
                            "posalji" to NativeFunction("posalji"){ args ->
                                if(args.size != 1 || args[0] !is Tekst){
                                    throw Exception("Funkcija 'posalji' prihvata 1 argument (poruka: tekst) (pronađeno ${args.size})")
                                }
                                try{
                                    val msg = (args[0] as Tekst).value.toByteArray()
                                    os.write(msg)
                                }
                                catch (e: Exception){
                                    throw Exception("Greška prilikom slanja poruke")
                                }
                                Null()
                            },
                            "pošalji" to NativeFunction("pošalji"){ args ->
                                if(args.size != 1 || args[0] !is Tekst){
                                    throw Exception("Funkcija 'pošalji' prihvata 1 argument (poruka: tekst) (pronađeno ${args.size})")
                                }
                                try{
                                    val msg = (args[0] as Tekst).value.toByteArray()
                                    os.write(msg)
                                }
                                catch (e: Exception){
                                    throw Exception("Greška prilikom slanja poruke")
                                }
                                Null()
                            },
                            "isprazni" to NativeFunction("isprazni"){ args ->
                                if(args.isNotEmpty()){
                                    throw Exception("Funkcija 'isprazni' ne prihvata argumente.")
                                }
                                try{
                                    os.flush()
                                }
                                catch (e: Exception){
                                    throw Exception("Greška prilikom pražnjenja izlaznog toka")
                                }
                                Null()
                            },
                            "zatvoriTok" to NativeFunction("zatvoriTok"){ args ->
                                if(args.isNotEmpty()){
                                    throw Exception("Funkcija 'zatvoriTok' ne prihvata argumente.")
                                }
                                try{
                                    os.close()
                                }
                                catch (e: Exception){
                                    throw Exception("Greška prilikom zatvaranja izlaznog toka")
                                }
                                Null()
                            }
                        ))
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom uspostavljanja izlaznog toka")
                    }
                },

                "ulazniTok" to NativeFunction("ulazniTok"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'ulazniTok' ne prihvata argumente.")
                    }

                    try {
                        StreamObjectFactory.constructInputStream(client.inputStream)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom uspostavljanja ulaznog toka")
                    }
                },

                "izlazniTok" to NativeFunction("izlazniTok"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'izlazniTok' ne prihvata argumente.")
                    }

                    try {
                        StreamObjectFactory.constructOutputStream(client.outputStream)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom uspostavljanja izlaznog toka")
                    }
                }
            ))
        }
    }
}