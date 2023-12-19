package interpreter.packages.io

import interpreter.packages.datetime.DateTimeObjectFactory
import interpreter.packages.telnet.BufferObjectFactory
import interpreter.values.*
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class FileObject(file: File) {
    private val apsolutnaAdresa = NativeFunction("apsolutnaAdresa") {
        return@NativeFunction Tekst(file.absolutePath)
    }
    private val relativnaAdresa =  NativeFunction("relativnaAdresa") {
        return@NativeFunction Tekst(file.path)
    }
    private val ime =  NativeFunction("ime") {
        return@NativeFunction Tekst(file.name)
    }

    private val roditelj = NativeFunction("roditelj") {
        if (file.parentFile == null) {
            return@NativeFunction Null()
        }
        FileObject(file.parentFile).construct()
    }

    private val jeDatoteka = NativeFunction("jeDatoteka"){
        Logicki(file.isFile)
    }
    private val jeFolder = NativeFunction("jeFolder"){
        Logicki(file.isDirectory)
    }
    private val jeSakriven = NativeFunction("jeSakriven"){
        Logicki(file.isHidden)
    }
    private val postoji =  NativeFunction("postoji") {
        Logicki(file.exists())
    }
    private val velicina = NativeFunction("veličina") {
        Broj(file.length().toDouble())
    }
    private val velicinaParticije = NativeFunction("veličinaParticije") {
        Broj(file.totalSpace.toDouble())
    }
    private val dozvoljenoPisati = NativeFunction("dozvoljenoPisati") {
        Logicki(file.canWrite())
    }
    private val dozvoljenoCitati = NativeFunction("dozvoljenoČitati") {
        Logicki(file.canRead())
    }
    private val dozvoljenoPokrenuti = NativeFunction("dozvoljenoPokrenuti") {
        Logicki(file.canExecute())
    }

    private val ponisti = NativeFunction("poništi") { args ->
        if (args.isNotEmpty()) {
            throw Exception("Funkcija 'poništi' ne prihvata argumente.")
        }
        val ok = file.delete()
        Logicki(ok)
    }

    private val preimenuj = NativeFunction("preimenuj") { args ->
        if (args.size != 1 || args[0] !is Tekst) {
            throw Exception("Argument mismatch")
        }
        val pathname = (args[0] as Tekst).value

        val ok = file.renameTo(File(pathname))
        Logicki(ok)
    }

    private val premjesti = NativeFunction("premjesti") { args ->
        if (args.size != 1 || args[0] !is Tekst) {
            throw Exception("Funkcija 'premjesti' prihvata 1 argument (putanja: Tekst) (pronađeno ${args.size})")
        }
        val pathname = (args[0] as Tekst).value

        val ok = file.renameTo(File(pathname))
        Logicki(ok)
    }

    private val pisac = NativeFunction("pisač") {
        val writer = file.bufferedWriter()
        BufferObjectFactory.constructWriter(writer)
    }

    private val citac = NativeFunction("čitač") {
        val reader = file.bufferedReader()
        BufferObjectFactory.constructReader(reader)
    }

    private val sadrzaj = NativeFunction("sadržaj") {
        if(file.exists()){
            Tekst(value = file.readText())
        }
        else Null()
    }

    private val postaviTekst = NativeFunction(name = "postaviTekst") { nestedArgs ->
        if (nestedArgs.size != 1 || nestedArgs[0] !is Tekst) {
            throw Exception("Funkcija 'postaviTekst' prihvata samo jedan argument (tekst: Tekst) (pronađeno ${nestedArgs.size})")
        }

        val text = (nestedArgs[0] as Tekst).value
        file.writeText(text)

        Null()
    }

    private val linije = NativeFunction(name = "linije") { _ ->
        val lines = file.readLines().map { Tekst(value = it) } as ArrayList<RuntimeValue>
        Niz(value = lines)
    }

    private val dopisi = NativeFunction(name = "dopiši") { nestedArgs ->
        if (nestedArgs.size != 1) {
            throw Exception("Funkcija 'dopiši' prihvata samo jedan argument (tekst: Tekst) (pronađeno ${nestedArgs.size})")
        }

        val text = nestedArgs[0].toString()
        file.appendText(text)

        Null()
    }

    private val vrijemeZadnjePromjene = NativeFunction(name = "vrijemeZadnjePromjene") { _ ->
        val millis = file.lastModified()
        val instant = Instant.ofEpochMilli(millis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        DateTimeObjectFactory.construct(dateTime)
    }

    private val listaDatoteka = NativeFunction("listaDatoteka") {
        val files = file.listFiles()
        if (files == null) {
            Null()
        } else {
            Niz(ArrayList(files.map { f -> FileObject(f).construct() }))
        }
    }

    private val kreiraj = NativeFunction("kreiraj") {
        if (file.isFile) {
            val ok = file.createNewFile()
            Logicki(ok)
        } else {
            val ok = file.mkdirs()
            Logicki(ok)
        }
    }

    private val kreirajDatoteku = NativeFunction("kreirajDatoteku") {
        val ok = file.createNewFile()
        Logicki(ok)
    }

    private val kreirajFolder = NativeFunction("kreirajFolder") {
        val ok = file.mkdir()
        Logicki(ok)
    }

    private val kreirajFoldere =  NativeFunction("kreirajFoldere") {
        val ok = file.mkdirs()
        Logicki(ok)
    }

    fun construct() : ReadonlyObject {
        return ReadonlyObject(hashMapOf(
            "apsolutnaAdresa" to apsolutnaAdresa,
            "relativnaAdresa" to relativnaAdresa,
            "ime" to ime,
            "roditelj" to roditelj,
            "jeDatoteka" to jeDatoteka,
            "jeFolder" to jeFolder,
            "jeSakriven" to jeSakriven,
            "postoji" to postoji,
            "velicina" to velicina,
            "veličina" to velicina,
            "velicinaParticije" to velicinaParticije,
            "veličinaParticije" to velicinaParticije,
            "dozvoljenoPisati" to dozvoljenoPisati,
            "dozvoljenoCitati" to dozvoljenoCitati,
            "dozvoljenoČitati" to dozvoljenoCitati,
            "dozvoljenoPokrenuti" to dozvoljenoPokrenuti,
            "ponisti" to ponisti,
            "poništi" to ponisti,
            "preimenuj" to preimenuj,
            "premjesti" to premjesti,
            "pisac" to pisac,
            "pisač" to pisac,
            "citac" to citac,
            "čitač" to citac,
            "sadrzaj" to sadrzaj,
            "sadržaj" to sadrzaj,
            "postaviTekst" to postaviTekst,
            "linije" to linije,
            "dopisi" to dopisi,
            "dopiši" to dopisi,
            "vrijemeZadnjePromjene" to vrijemeZadnjePromjene,
            "listaDatoteka" to listaDatoteka,
            "kreiraj" to kreiraj,
            "kreirajDatoteku" to kreirajDatoteku,
            "kreirajFolder" to kreirajFolder,
            "kreirajFoldere" to kreirajFoldere,
        ))
    }
}