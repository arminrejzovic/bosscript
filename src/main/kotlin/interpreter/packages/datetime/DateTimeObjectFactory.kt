package interpreter.packages.datetime

import interpreter.values.*
import java.time.LocalDateTime

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
                "minusDana" to object : NativeFunkcija("minusDana"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusDana accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusDays(nDays))
                    }
                },
                "plusDana" to object : NativeFunkcija("plusDana"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusDana accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusDays(nDays))
                    }
                },
                "plusSedmica" to object : NativeFunkcija("plusSedmica"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusSedmica accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusWeeks(nDays))
                    }
                },
                "minusSedmica" to object : NativeFunkcija("minusSedmica"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSedmica accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusWeeks(nDays))
                    }
                },
                "plusMjeseci" to object : NativeFunkcija("plusMjeseci"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusMjeseci accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusMonths(nDays))
                    }
                },
                "minusMjeseci" to object : NativeFunkcija("minusMjeseci"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusMjeseci accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusMonths(nDays))
                    }
                },
                "minusGodina" to object : NativeFunkcija("minusGodina"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusGodina accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusYears(nDays))
                    }
                },
                "plusGodina" to object : NativeFunkcija("plusGodina"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusGodina accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusYears(nDays))
                    }
                },
                "plusSati" to object : NativeFunkcija("plusSati"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusSati accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusHours(nDays))
                    }
                },
                "minusSati" to object : NativeFunkcija("minusSati"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSati accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusHours(nDays))
                    }
                },
                "plusMinuta" to object : NativeFunkcija("plusMinuta"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusMinuta accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusMinutes(nDays))
                    }
                },
                "minusMinuta" to object : NativeFunkcija("minusMinuta"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusMinuta accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusMinutes(nDays))
                    }
                },
                "plusSekundi" to object : NativeFunkcija("plusSekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusSekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusSeconds(nDays))
                    }
                },
                "minusSekundi" to object : NativeFunkcija("minusSekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusSeconds(nDays))
                    }
                },
                "plusNanosekundi" to object : NativeFunkcija("plusNanosekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusNanosekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.plusNanos(nDays))
                    }
                },
                "minusNanosekundi" to object : NativeFunkcija("minusNanosekundi"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusNanosekundi accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(date.minusNanos(nDays))
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

                        val newDate = LocalDateTime.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                            hours.toInt(),
                            minutes.toInt(),
                            seconds.toInt(),
                            nanos.toInt()
                        )

                        return Logicki(value = date.isAfter(newDate))
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

                        val newDate = LocalDateTime.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                            hours.toInt(),
                            minutes.toInt(),
                            seconds.toInt(),
                            nanos.toInt()
                        )

                        return Logicki(value = date.isBefore(newDate))
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

                        val newDate = LocalDateTime.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                            hours.toInt(),
                            minutes.toInt(),
                            seconds.toInt(),
                            nanos.toInt()
                        )
                        return Logicki(value = date.isEqual(newDate))
                    }
                }
            ))
            
        }
    }
}