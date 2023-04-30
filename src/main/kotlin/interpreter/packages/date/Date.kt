package interpreter.packages.date

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunkcija
import interpreter.values.RuntimeValue
import interpreter.values.Tekst
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val Date = Environment(
    variables = hashMapOf(
        "datumSada" to object : NativeFunkcija("datumSada"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                return DateObjectFactory.construct(LocalDate.now())
            }
        },
        "Datum" to object : NativeFunkcija("Datum"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 3){
                    throw Exception("Argument mismatch")
                }
                val year = (args[0] as Broj).value.toInt()
                val month = (args[1] as Broj).value.toInt()
                val day = (args[2] as Broj).value.toInt()

                return DateObjectFactory.construct(LocalDate.of(year, month, day))
            }
        },
        "datumIzTeksta" to object : NativeFunkcija("datumIzTeksta"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 || args[0] !is Tekst){
                    throw Exception("Argument mismatch")
                }
                val dateString = (args[0] as Tekst).value
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                return DateObjectFactory.construct(LocalDate.parse(dateString, formatter))
            }
        },
        "datumPoFormatu" to object : NativeFunkcija("datumPoFormatu"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                    throw Exception("Argument mismatch")
                }
                val dateString = (args[0] as Tekst).value
                val format = (args[1] as Tekst).value
                val formatter = DateTimeFormatter.ofPattern(format)
                return DateObjectFactory.construct(LocalDate.parse(dateString, formatter))
            }
        },
    )
)