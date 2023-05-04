package interpreter.packages.time

import interpreter.packages.datetime.DateTimeObjectFactory
import interpreter.values.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class TimeObjectFactory{
    companion object{
        fun construct(time: LocalTime): Objekat{
            return Objekat(properties = hashMapOf(
                "sati" to Broj(value = time.hour.toDouble()),
                "minute" to Broj(value = time.minute.toDouble()),
                "sekunde" to Broj(value = time.second.toDouble()),
                "nanosekunde" to Broj(value = time.nano.toDouble()),
                "plusSati" to NativeFunction("plusSati"){args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("plusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.plusHours(nDays))
                },
                "minusSati" to NativeFunction("minusSati"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSati accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.minusHours(nDays))
                },
                "plusMinuta" to NativeFunction("plusMinuta"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.plusMinutes(nDays))
                },
                "minusMinuta" to NativeFunction("minusMinuta"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusMinuta accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.minusMinutes(nDays))
                },
                "plusSekundi" to NativeFunction("plusSekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.plusSeconds(nDays))
                },
                "minusSekundi" to NativeFunction("minusSekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusSekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.minusSeconds(nDays))
                },
                "plusNanosekundi" to NativeFunction("plusNanosekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("plusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.plusNanos(nDays))
                },
                "minusNanosekundi" to NativeFunction("minusNanosekundi"){args ->
                    if(args.size != 1 && args[0] !is Broj){
                        throw Exception("minusNanosekundi accepts 1 argument (n: broj)")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(time.minusNanos(nDays))
                },
                "periodIzmedju" to NativeFunction("periodIzmedju"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("periodIzmedju accepts 1 argument (v: Vrijeme)")
                    }
                    val providedTime = args[0] as Objekat
                    val hours = (providedTime.getProperty("sati") as Broj).value
                    val minutes = (providedTime.getProperty("minute") as Broj).value
                    val seconds = (providedTime.getProperty("sekunde") as Broj).value

                    val other = LocalTime.of(
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt()
                    )

                    val diff = Duration.between(time, other)

                    return@NativeFunction Objekat(hashMapOf(
                        "sati" to Broj(diff.toHoursPart().toDouble()),
                        "minute" to Broj(diff.toMinutesPart().toDouble()),
                        "sekunde" to Broj(diff.toSecondsPart().toDouble()),
                    ))
                },
                "jePoslije" to NativeFunction("jePoslije"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jePoslije accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val date = LocalTime.of(
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return@NativeFunction Logicki(value = time.isAfter(date))
                },
                "jePrije" to NativeFunction("jePrije"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jePrije accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat

                    val hours = (providedDate.getProperty("sati") as Broj).value
                    val minutes = (providedDate.getProperty("minute") as Broj).value
                    val seconds = (providedDate.getProperty("sekunde") as Broj).value
                    val nanos = (providedDate.getProperty("nanosekunde") as Broj).value

                    val date = LocalTime.of(
                        hours.toInt(),
                        minutes.toInt(),
                        seconds.toInt(),
                        nanos.toInt()
                    )

                    return@NativeFunction Logicki(value = time.isBefore(date))
                },
                "naDatum" to NativeFunction("naDatum"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("naDatum accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value

                    val date = LocalDate.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                    )
                    return@NativeFunction DateTimeObjectFactory.construct(time.atDate(date))
                },
                "jednako" to NativeFunction("jednako"){args ->
                    if(args.size != 1 && args[0] !is Objekat){
                        throw Exception("jednako accepts 1 argument (d: Datum)")
                    }
                    val providedDate = args[0] as Objekat
                    val year = (providedDate.getProperty("godina") as Broj).value
                    val month = (providedDate.getProperty("mjesec") as Broj).value
                    val day = (providedDate.getProperty("danMjeseca") as Broj).value

                    val date = LocalDate.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                    )
                    return@NativeFunction Logicki(value = time.equals(date))
                }
            ))
        }
    }
}