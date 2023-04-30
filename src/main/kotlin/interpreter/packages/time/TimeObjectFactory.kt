package interpreter.packages.time

import interpreter.packages.datetime.DateTimeObjectFactory
import interpreter.values.*
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
                "plusSati" to object : NativeFunkcija("plusSati"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if (args.size != 1 && args[0] !is Broj) {
                            throw Exception("plusSati accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.plusHours(nDays))
                    }
                },
                "minusSati" to object : NativeFunkcija("minusSati"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSati accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.minusHours(nDays))
                    }
                },
                "plusMinuta" to object : NativeFunkcija("plusMinuta"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusMinuta accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.plusMinutes(nDays))
                    }
                },
                "minusMinuta" to object : NativeFunkcija("minusMinuta"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusMinuta accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.minusMinutes(nDays))
                    }
                },
                "plusSekundi" to object : NativeFunkcija("plusSekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusSekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.plusSeconds(nDays))
                    }
                },
                "minusSekundi" to object : NativeFunkcija("minusSekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.minusSeconds(nDays))
                    }
                },
                "plusNanosekundi" to object : NativeFunkcija("plusNanosekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusNanosekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.plusNanos(nDays))
                    }
                },
                "minusNanosekundi" to object : NativeFunkcija("minusNanosekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusNanosekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(time.minusNanos(nDays))
                    }
                },
                "jePoslije" to object : NativeFunkcija("jePoslije"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
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

                        return Logicki(value = time.isAfter(date))
                    }
                },
                "jePrije" to object : NativeFunkcija("jePrije"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
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

                        return Logicki(value = time.isBefore(date))
                    }
                },
                "naDatum" to object : NativeFunkcija("naDatum"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
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
                        return DateTimeObjectFactory.construct(time.atDate(date))
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

                        val date = LocalDate.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                        )
                        return Logicki(value = time.equals(date))
                    }
                }
            ))
        }
    }
}