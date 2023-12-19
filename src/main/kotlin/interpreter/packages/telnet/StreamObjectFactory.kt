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
                        throw Exception("Greška prilikom zatvaranja ulaznog toka")
                    }
                    Null()
                },
                "ucitaj" to NativeFunction("ucitaj"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'ucitaj' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val bufferSize = (args[0] as Broj).value.toInt()
                        val buffer = ByteArray(bufferSize)
                        val bytesRead = stream.read(buffer)
                        val data = buffer.copyOf(bytesRead)
                        BajtNiz(data)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja iz ulaznog toka")
                    }
                },
                "učitaj" to NativeFunction("učitaj"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'učitaj' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val bufferSize = (args[0] as Broj).value.toInt()
                        val buffer = ByteArray(bufferSize)
                        val bytesRead = stream.read(buffer)
                        val data = buffer.copyOf(bytesRead)
                        BajtNiz(data)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja iz ulaznog toka")
                    }
                },
                "ucitajPreostaleBajte" to NativeFunction("ucitajPreostaleBajte"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'ucitajPreostaleBajte' ne prihvata argumente.")
                    }
                    try {
                        val bytes = stream.readAllBytes()
                        BajtNiz(bytes)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja iz ulaznog toka")
                    }
                },
                "učitajPreostaleBajte" to NativeFunction("učitajPreostaleBajte"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'učitajPreostaleBajte' ne prihvata argumente.")
                    }
                    try {
                        val bytes = stream.readAllBytes()
                        BajtNiz(bytes)
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom čitanja iz ulaznog toka")
                    }
                },
                "dostupnihBajtova" to NativeFunction("dostupnihBajtova"){ args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'dostupnihBajtova' ne prihvata argumente.")
                    }
                    try {
                        val n = stream.available()
                        Broj(n.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Nije moguće odrediti broj dostupnih bajtova u ulaznom toku.")
                    }
                },
                "preskoci" to NativeFunction("preskoci"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'preskoci' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = stream.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom preskakanja bajtova iz ulaznog toka")
                    }
                },
                "preskoči" to NativeFunction("preskoči"){ args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("Funkcija 'preskoči' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    try {
                        val n = (args[0] as Broj).value.toLong()
                        val actuallySkipped = stream.skip(n)
                        Broj(actuallySkipped.toDouble())
                    }
                    catch (e: Exception){
                        throw Exception("Greška prilikom preskakanja bajtova iz ulaznog toka")
                    }
                },
                "citac" to NativeFunction("citac"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'citac' ne prihvata argumente.")
                    }
                    BufferObjectFactory.constructReader(stream.bufferedReader())
                },
                "čitač" to NativeFunction("čitač"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funkcija 'čitač' ne prihvata argumente.")
                    }
                    BufferObjectFactory.constructReader(stream.bufferedReader())
                }
            ))
        }

        fun constructOutputStream(stream: OutputStream): Objekat{
            return Objekat(hashMapOf(
                "posaljiBajt" to NativeFunction("posaljiBajt"){args ->
                    if(args.size != 1 || args[0] !is Broj){
                        throw Exception("Funkcija 'posaljiBajt' prihvata 1 argument (bajt: broj) (pronađeno ${args.size})")
                    }
                    val arg = (args[0] as Broj).value.toInt()
                    stream.write(arg)
                    Null()
                },
                "pošaljiBajt" to NativeFunction("pošaljiBajt"){args ->
                    if(args.size != 1 || args[0] !is Broj){
                        throw Exception("Funkcija 'pošaljiBajt' prihvata 1 argument (bajt: broj) (pronađeno ${args.size})")
                    }
                    val arg = (args[0] as Broj).value.toInt()
                    stream.write(arg)
                    Null()
                },
                "posalji" to NativeFunction("posalji"){args ->
                    if(args.size != 1 || args[0] !is BajtNiz){
                        throw Exception("Funkcija 'posalji' prihvata 1 argument (bajtovi: bajtNiz) (pronađeno ${args.size})")
                    }
                    val arg = (args[0] as BajtNiz).value
                    stream.write(arg)
                    Null()
                },
                "pošalji" to NativeFunction("pošalji"){args ->
                    if(args.size != 1 || args[0] !is BajtNiz){
                        throw Exception("Funkcija 'pošalji' prihvata 1 argument (bajtovi: bajtNiz) (pronađeno ${args.size})")
                    }
                    val arg = (args[0] as BajtNiz).value
                    stream.write(arg)
                    Null()
                },
                "isprazni" to NativeFunction("isprazni"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funckija 'isprazni' ne prihvata argumente.")
                    }
                    stream.flush()
                    Null()
                },
                "zatvori" to NativeFunction("zatvori"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funckija 'zatvori' ne prihvata argumente.")
                    }
                    stream.close()
                    Null()
                },
                "pisac" to NativeFunction("pisac"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funckija 'pisac' ne prihvata argumente.")
                    }
                    BufferObjectFactory.constructWriter(stream.bufferedWriter())
                },
                "pisač" to NativeFunction("pisač"){args ->
                    if(args.isNotEmpty()){
                        throw Exception("Funckija 'pisač' ne prihvata argumente.")
                    }
                    BufferObjectFactory.constructWriter(stream.bufferedWriter())
                }
            ))
        }
    }
}