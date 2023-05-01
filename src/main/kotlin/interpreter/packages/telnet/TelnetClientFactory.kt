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
                "uspostaviKonekciju" to object : NativeFunkcija("uspostaviKonekciju"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
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

                        return Null()
                    }
                },

                "zatvoriKonekciju" to object : NativeFunkcija("zatvoriKonekciju"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.isNotEmpty()){
                            throw Exception("Argument mismatch: Function 'zatvoriKonekciju' accepts 0 arguments")
                        }

                        try{
                            client.disconnect()
                        }
                        catch (e: Exception){
                            throw Exception("Failed to disconnect...")
                        }

                        return Null()
                    }
                },

                "izlazniTok" to object : NativeFunkcija("izlazniTok"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.isNotEmpty()){
                            throw Exception("Argument mismatch: Function 'izlazniTok' accepts 0 arguments")
                        }

                        try{
                            val os = client.outputStream
                            return Objekat(properties = hashMapOf(
                                "posalji" to object : NativeFunkcija("posalji"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
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
                                        return Null()
                                    }
                                },
                                "isprazni" to object : NativeFunkcija("isprazni"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.isNotEmpty()){
                                            throw Exception("Argument mismatch")
                                        }
                                        try{
                                            os.flush()
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to flush output stream")
                                        }
                                        return Null()
                                    }
                                },
                                "zatvoriTok" to object : NativeFunkcija("zatvoriTok"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.isNotEmpty()){
                                            throw Exception("Argument mismatch")
                                        }
                                        try{
                                            os.close()
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to close output stream")
                                        }
                                        return Null()
                                    }
                                }
                            ))
                        }
                        catch (e: Exception){
                            throw Exception("Cannot establish output stream...")
                        }
                    }
                },

                "ulazniTok" to object : NativeFunkcija("ulazniTok"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.isNotEmpty()){
                            throw Exception("Argument mismatch: Function 'izlazniTok' accepts 0 arguments")
                        }

                        try {
                            val inputStream = client.inputStream
                            return Objekat(properties = hashMapOf(
                                "zatvoriTok" to object : NativeFunkcija("zatvoriTok"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        try {
                                            inputStream.close()
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to close input stream...")
                                        }
                                        return Null()
                                    }
                                },
                                "ucitaj" to object : NativeFunkcija("ucitaj"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.size != 1 && args[0] !is Broj){
                                            throw Exception("Argument mismatch")
                                        }
                                        try {
                                            val bufferSize = (args[0] as Broj).value.toInt()
                                            val buffer = ByteArray(bufferSize)
                                            val bytesRead = inputStream.read(buffer)
                                            val data = buffer.copyOf(bytesRead)
                                            return BajtNiz(data)
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to read line from input stream")
                                        }
                                    }
                                },
                                "ucitajLiniju" to object : NativeFunkcija("ucitajLiniju"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.isNotEmpty()){
                                            throw Exception("Argument mismatch")
                                        }
                                        try {
                                            val msg = inputStream.bufferedReader().readLine()
                                            return Tekst(msg)
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to read line from input stream")
                                        }
                                    }
                                },
                                "ucitajPreostaleBajte" to object : NativeFunkcija("ucitajPreostaleBajte"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.isNotEmpty()){
                                            throw Exception("Argument mismatch")
                                        }
                                        try {
                                            val bytes = inputStream.readAllBytes()
                                            return BajtNiz(bytes)
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to read line from input stream")
                                        }
                                    }
                                },
                                "dostupnihBajtova" to object : NativeFunkcija("dostupnihBajtova"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.isNotEmpty()){
                                            throw Exception("Argument mismatch")
                                        }
                                        try {
                                            val n = inputStream.available()
                                            return Broj(n.toDouble())
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to determine available bytes in input stream")
                                        }
                                    }
                                },
                                "preskoci" to object : NativeFunkcija("preskoci"){
                                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                                        if(args.size != 1 && args[0] !is Broj){
                                            throw Exception("Argument mismatch")
                                        }
                                        try {
                                            val n = (args[0] as Broj).value.toLong()
                                            val actuallySkipped = inputStream.skip(n)
                                            return Broj(actuallySkipped.toDouble())
                                        }
                                        catch (e: Exception){
                                            throw Exception("Unable to skip ${args[0].value} bytes in input stream")
                                        }
                                    }
                                }
                            ))
                        }
                        catch (e: Exception){
                            throw Exception("Unable to establish input stream")
                        }
                    }
                }
            ))
        }
    }
}