package interpreter.packages.time

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunction
import interpreter.values.Tekst
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val Time = Environment(
    variables = hashMapOf(
        "vrijemeSada" to NativeFunction("vrijemeSada"){
            return@NativeFunction TimeObjectFactory.construct(LocalTime.now())
        },
        "Vrijeme" to NativeFunction("Vrijeme"){args ->
            if(args.size != 3){
                throw Exception("Funkcija 'Vrijeme' prihvata 3 argumenta (sati: broj, minute: broj, sekunde: broj) (pronađeno ${args.size})")
            }
            val hours = (args[0] as Broj).value.toInt()
            val minutes = (args[1] as Broj).value.toInt()
            val seconds = (args[2] as Broj).value.toInt()

            return@NativeFunction TimeObjectFactory.construct(LocalTime.of(hours, minutes, seconds))
        },
        "vrijemeIzTeksta" to NativeFunction("vrijemeIzTeksta"){args ->
            if(args.size != 1 || args[0] !is Tekst){
                throw Exception("Funkcija 'vrijemeIzTeksta' prihvata 1 argument (vrijeme: tekst) (pronađeno ${args.size})")
            }
            val dateString = (args[0] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            return@NativeFunction TimeObjectFactory.construct(LocalTime.parse(dateString, formatter))
        },
        "vrijemePoFormatu" to NativeFunction("vrijemePoFormatu"){args ->
            if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                throw Exception("Funkcija 'vrijemePoFormatu' prihvata 2 argumenta (vrijeme: tekst, format: tekst) (pronađeno ${args.size})")
            }
            val dateString = (args[0] as Tekst).value
            val format = (args[1] as Tekst).value
            val formatter = DateTimeFormatter.ofPattern(format)
            return@NativeFunction TimeObjectFactory.construct(LocalTime.parse(dateString, formatter))
        },
    )
)