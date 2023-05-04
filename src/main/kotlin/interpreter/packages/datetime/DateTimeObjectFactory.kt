package interpreter.packages.datetime

import interpreter.values.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period

class DateTimeObjectFactory{
    companion object{
        fun construct(date: LocalDateTime): Objekat{
            return Objekat(properties = hashMapOf(
                "godina" to Broj(value = date.year.toDouble()),
                "mjesec" to Broj(value = date.monthValue.toDouble()),
                "danGodine" to Broj(value = date.dayOfYear.toDouble()),
                "danMjeseca" to Broj(value = date.dayOfMonth.toDouble()),
                "danSedmice" to Broj(value = date.dayOfWeek.value.toDouble()),
                "sati" to Broj(value = date.hour.toDouble()),
                "minute" to Broj(value = date.minute.toDouble()),
                "sekunde" to Broj(value = date.second.toDouble()),
                "nanosekunde" to Broj(value = date.nano.toDouble()),
                "minusDana" to NativeFunction("minusDana"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusDana accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusDays(nDays))
                },
                "plusDana" to NativeFunction("plusDana"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusDana accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusDays(nDays))
                },
                "plusSedmica" to NativeFunction("plusSedmica"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSedmica accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusWeeks(nDays))
                },
                "minusSedmica" to NativeFunction("minusSedmica"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSedmica accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusWeeks(nDays))
                },
                "plusMjeseci" to NativeFunction("plusMjeseci"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusMjeseci accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusMonths(nDays))
                },
                "minusMjeseci" to NativeFunction("minusMjeseci"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusMjeseci accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusMonths(nDays))
                },
                "minusGodina" to NativeFunction("minusGodina"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusGodina accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusYears(nDays))
                },
                "plusGodina" to NativeFunction("plusGodina"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusGodina accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusYears(nDays))
                },
                "plusSati" to NativeFunction("plusSati"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusHours(nDays))
                },
                "minusSati" to NativeFunction("minusSati"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusHours(nDays))
                },
                "plusMinuta" to NativeFunction("plusMinuta"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusMinutes(nDays))
                },
                "minusMinuta" to NativeFunction("minusMinuta"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusMinutes(nDays))
                },
                "plusSekundi" to NativeFunction("plusSekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusSeconds(nDays))
                },
                "minusSekundi" to NativeFunction("minusSekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusSeconds(nDays))
                },
                "plusNanosekundi" to NativeFunction("plusNanosekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.plusNanos(nDays))
                },
                "minusNanosekundi" to NativeFunction("minusNanosekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(date.minusNanos(nDays))
                },
                "jePoslije" to NativeFunction("jePoslije"){args ->
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

                    val newDate = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return@NativeFunction Logicki(value = date.isAfter(newDate))
                },
                "periodIzmedju" to NativeFunction("periodIzmedju"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("periodIzmedju accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value
                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val newDate = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    val period = Period.between(date.toLocalDate(), newDate.toLocalDate())
                    val duration = Duration.between(date.toLocalTime(), newDate.toLocalTime())

                    return@NativeFunction Objekat(hashMapOf(
                        "godine" to Broj(period.years.toDouble()),
                        "mjeseci" to Broj(period.months.toDouble()),
                        "dani" to Broj(period.days.toDouble()),
                        "sati" to Broj(duration.toHoursPart().toDouble()),
                        "minute" to Broj(duration.toMinutesPart().toDouble()),
                        "sekunde" to Broj(duration.toSecondsPart().toDouble()),
                    ))
                },

                "jePrije" to NativeFunction("jePrije"){args ->
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

                    val newDate = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return@NativeFunction Logicki(value = date.isBefore(newDate))
                },

                "jednako" to NativeFunction("jednako"){args ->
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

                    val newDate = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )
                    return@NativeFunction Logicki(value = date.isEqual(newDate))
                }
            ))
            
        }
    }
}