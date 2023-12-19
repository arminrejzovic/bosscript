package interpreter.packages.date

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunction
import interpreter.values.Tekst
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val Date = Environment(
    variables = hashMapOf(
        "datumSada" to NativeFunction("datumSada"){args ->
            return@NativeFunction DateObjectFactory.construct(LocalDate.now())
        },
        "Datum" to NativeFunction("Datum"){args ->
            if(args.size != 3){
                throw Exception("Funkcija 'Datum' prihvata 3 argumenta (dan: broj, mjesec: broj, godina: broj) (pronađeno ${args.size})")
            }
            val year = (args[2] as Broj).value.toInt()
            val month = (args[1] as Broj).value.toInt()
            val day = (args[0] as Broj).value.toInt()

            return@NativeFunction DateObjectFactory.construct(LocalDate.of(year, month, day))
        },
        "datumIzTeksta" to NativeFunction("datumIzTeksta"){args ->
            if(args.size != 1 || args[0] !is Tekst){
                throw Exception("Funkcija 'datumIzTeksta' prihvata 1 argument (datum: tekst) (pronađeno ${args.size})")
            }
            val dateString = (args[0] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            return@NativeFunction DateObjectFactory.construct(LocalDate.parse(dateString, formatter))
        },
        "datumPoFormatu" to NativeFunction("datumPoFormatu"){args ->
            if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                throw Exception("Funckija 'datumPoFormatu' prihvata 2 argumenta (datum: tekst, format: tekst) (pronađeno ${args.size})")
            }
            val dateString = (args[0] as Tekst).value
            val format = (args[1] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern(format)
            return@NativeFunction DateObjectFactory.construct(LocalDate.parse(dateString, formatter))
        },
    )
)