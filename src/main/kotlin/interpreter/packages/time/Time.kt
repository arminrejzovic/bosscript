package interpreter.packages.time

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunkcija
import interpreter.values.RuntimeValue
import interpreter.values.Tekst
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val Time = Environment(
    variables = hashMapOf(
        "vrijemeSada" to object : NativeFunkcija("vrijemeSada"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                return TimeObjectFactory.construct(LocalTime.now())
            }
        },
        "Vrijeme" to object : NativeFunkcija("Vrijeme"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 5){
                    throw Exception("Argument mismatch")
                }
                val hours = (args[0] as Broj).value.toInt()
                val minutes = (args[1] as Broj).value.toInt()
                val seconds = (args[2] as Broj).value.toInt()

                return TimeObjectFactory.construct(LocalTime.of(hours, minutes, seconds))
            }
        },
        "vrijemeIzTeksta" to object : NativeFunkcija("vrijemeIzTeksta"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 || args[0] !is Tekst){
                    throw Exception("Argument mismatch")
                }
                val dateString = (args[0] as Tekst).value
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                return TimeObjectFactory.construct(LocalTime.parse(dateString, formatter))
            }
        },
        "vrijemePoFormatu" to object : NativeFunkcija("vrijemePoFormatu"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                    throw Exception("Argument mismatch")
                }
                val dateString = (args[0] as Tekst).value
                val format = (args[1] as Tekst).value
                val formatter = DateTimeFormatter.ofPattern(format)
                return TimeObjectFactory.construct(LocalTime.parse(dateString, formatter))
            }
        },
    )
)