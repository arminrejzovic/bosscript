package interpreter.packages.datetime

import interpreter.values.*
import java.time.LocalDateTime

class DateTimeObject(
    private val dt: LocalDateTime,
) {
    fun construct() : Objekat{
        return Objekat(properties = hashMapOf(
            "godina" to Broj(value = dt.year.toDouble()),
            "mjesec" to Broj(value = dt.monthValue.toDouble()),
            "danGodine" to Broj(value = dt.dayOfYear.toDouble()),
            "danMjeseca" to Broj(value = dt.dayOfMonth.toDouble()),
            "danSedmice" to Broj(value = dt.dayOfWeek.value.toDouble()),
            "sati" to Broj(value = dt.hour.toDouble()),
            "minute" to Broj(value = dt.minute.toDouble()),
            "sekunde" to Broj(value = dt.second.toDouble()),
            "nanosekunde" to Broj(value = dt.nano.toDouble()),
            "minusDana" to object : NativeFunkcija("minusDana"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusDana accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusDays(nDays))
                    return obj.construct()
                }
            },
            "plusDana" to object : NativeFunkcija("plusDana"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusDana accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusDays(nDays))
                    return obj.construct()
                }
            },
            "plusSedmica" to object : NativeFunkcija("plusSedmica"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSedmica accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusWeeks(nDays))
                    return obj.construct()
                }
            },
            "minusSedmica" to object : NativeFunkcija("minusSedmica"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSedmica accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusWeeks(nDays))
                    return obj.construct()
                }
            },
            "plusMjeseci" to object : NativeFunkcija("plusMjeseci"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusMjeseci accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusMonths(nDays))
                    return obj.construct()
                }
            },
            "minusMjeseci" to object : NativeFunkcija("minusMjeseci"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusMjeseci accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusMonths(nDays))
                    return obj.construct()
                }
            },
            "minusGodina" to object : NativeFunkcija("minusGodina"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusGodina accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusYears(nDays))
                    return obj.construct()
                }
            },
            "plusGodina" to object : NativeFunkcija("plusGodina"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusGodina accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusYears(nDays))
                    return obj.construct()
                }
            },
            "plusSati" to object : NativeFunkcija("plusSati"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusHours(nDays))
                    return obj.construct()
                }
            },
            "minusSati" to object : NativeFunkcija("minusSati"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusHours(nDays))
                    return obj.construct()
                }
            },
            "plusMinuta" to object : NativeFunkcija("plusMinuta"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusMinutes(nDays))
                    return obj.construct()
                }
            },
            "minusMinuta" to object : NativeFunkcija("minusMinuta"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusMinutes(nDays))
                    return obj.construct()
                }
            },
            "plusSekundi" to object : NativeFunkcija("plusSekundi"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusSeconds(nDays))
                    return obj.construct()
                }
            },
            "minusSekundi" to object : NativeFunkcija("minusSekundi"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusSeconds(nDays))
                    return obj.construct()
                }
            },
            "plusNanosekundi" to object : NativeFunkcija("plusNanosekundi"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.plusNanos(nDays))
                    return obj.construct()
                }
            },
            "minusNanosekundi" to object : NativeFunkcija("minusNanosekundi"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    val obj = DateTimeObject(dt.minusNanos(nDays))
                    return obj.construct()
                }
            },
            "jePoslije" to object : NativeFunkcija("jePoslije"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jePoslije accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value
                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val date = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return Logicki(value = dt.isAfter(date))
                }
            },
            "jePrije" to object : NativeFunkcija("jePrije"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jePrije accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value
                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val date = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return Logicki(value = dt.isBefore(date))
                }
            },
            "jednako" to object : NativeFunkcija("jednako"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jednako accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value
                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val date = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )
                    return Logicki(value = dt.isEqual(date))
                }
            }
        ))
    }
}