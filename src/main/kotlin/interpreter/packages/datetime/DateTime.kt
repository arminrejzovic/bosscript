package interpreter.packages.datetime

import interpreter.Environment
import interpreter.values.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DateTime = Environment(
    variables = hashMapOf(
        "datumVrijemeSada" to NativeFunction("datumVrijemeSada"){
            return@NativeFunction DateTimeObjectFactory.construct(LocalDateTime.now())
        },
        "puniDatum" to NativeFunction("puniDatum"){args ->
            if(args.size != 7){
                throw Exception("Argument mismatch")
            }
            val year = (args[0] as Broj).value.toInt()
            val month = (args[1] as Broj).value.toInt()
            val day = (args[2] as Broj).value.toInt()
            val hours = (args[3] as Broj).value.toInt()
            val minutes = (args[4] as Broj).value.toInt()
            val seconds = (args[5] as Broj).value.toInt()
            val nanos = (args[6] as Broj).value.toInt()

            return@NativeFunction DateTimeObjectFactory.construct(LocalDateTime.of(year, month, day, hours, minutes, seconds, nanos))
        },
        "DatumVrijeme" to NativeFunction("DatumVrijeme"){args ->
            if(args.size != 5){
                throw Exception("Argument mismatch")
            }
            val year = (args[2] as Broj).value.toInt()
            val month = (args[1] as Broj).value.toInt()
            val day = (args[0] as Broj).value.toInt()
            val hours = (args[3] as Broj).value.toInt()
            val minutes = (args[4] as Broj).value.toInt()

            return@NativeFunction DateTimeObjectFactory.construct(LocalDateTime.of(year, month, day, hours, minutes))
        },
        "datumVrijemeIzTeksta" to NativeFunction("datumVrijemeIzTeksta"){args ->
            if(args.size != 1 || args[0] !is Tekst){
                throw Exception("Argument mismatch")
            }
            val dateString = (args[0] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            return@NativeFunction DateTimeObjectFactory.construct(LocalDateTime.parse(dateString, formatter))
        },
        "datumVrijemePoFormatu" to NativeFunction("datumVrijemePoFormatu"){args ->
            if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                throw Exception("Argument mismatch")
            }
            val dateString = (args[0] as Tekst).value
            val format = (args[1] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern(format)
            return@NativeFunction DateTimeObjectFactory.construct(LocalDateTime.parse(dateString, formatter))
        },
    )
)