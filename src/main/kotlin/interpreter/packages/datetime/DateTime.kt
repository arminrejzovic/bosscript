package interpreter.packages.datetime

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunkcija
import interpreter.values.RuntimeValue
import interpreter.values.Tekst
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DateTime = Environment(
    variables = hashMapOf(
        "sada" to object : NativeFunkcija("sada"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                val dt = DateTimeObject(LocalDateTime.now())
                return dt.construct()
            }
        },
        "puniDatum" to object : NativeFunkcija("puniDatum"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
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

                val dt = DateTimeObject(LocalDateTime.of(year, month, day, hours, minutes, seconds, nanos))
                return dt.construct()
            }
        },
        "datum" to object : NativeFunkcija("datum"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 5){
                    throw Exception("Argument mismatch")
                }
                val year = (args[0] as Broj).value.toInt()
                val month = (args[1] as Broj).value.toInt()
                val day = (args[2] as Broj).value.toInt()
                val hours = (args[3] as Broj).value.toInt()
                val minutes = (args[4] as Broj).value.toInt()

                val dt = DateTimeObject(LocalDateTime.of(year, month, day, hours, minutes))
                return dt.construct()
            }
        },
        "datumIzTeksta" to object : NativeFunkcija("datumIzTeksta"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 || args[0] !is Tekst){
                    throw Exception("Argument mismatch")
                }
                val dateString = (args[0] as Tekst).value
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                val dt = DateTimeObject(LocalDateTime.parse(dateString, formatter))
                return dt.construct()
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
                val dt = DateTimeObject(LocalDateTime.parse(dateString, formatter))
                return dt.construct()
            }
        },
    )
)