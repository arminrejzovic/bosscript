package interpreter.packages.date

import interpreter.values.*
import java.time.LocalDate

class DateObjectFactory{
    companion object{
        fun construct(ld: LocalDate) : Objekat{
            return Objekat(properties = hashMapOf(
                "godina" to Broj(value = ld.year.toDouble()),
                "mjesec" to Broj(value = ld.monthValue.toDouble()),
                "danGodine" to Broj(value = ld.dayOfYear.toDouble()),
                "danMjeseca" to Broj(value = ld.dayOfMonth.toDouble()),
                "danSedmice" to Broj(value = ld.dayOfWeek.value.toDouble()),
                "minusDana" to object : NativeFunkcija("minusDana"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusDana accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.minusDays(nDays))
                    }
                },
                "plusDana" to object : NativeFunkcija("plusDana"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusDana accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.plusDays(nDays))
                    }
                },
                "plusSedmica" to object : NativeFunkcija("plusSedmica"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusSedmica accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.plusWeeks(nDays))
                    }
                },
                "minusSedmica" to object : NativeFunkcija("minusSedmica"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusSedmica accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.minusWeeks(nDays))
                    }
                },
                "plusMjeseci" to object : NativeFunkcija("plusMjeseci"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusMjeseci accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.plusMonths(nDays))
                    }
                },
                "minusMjeseci" to object : NativeFunkcija("minusMjeseci"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusMjeseci accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.minusMonths(nDays))
                    }
                },
                "minusGodina" to object : NativeFunkcija("minusGodina"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("minusGodina accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.minusYears(nDays))
                    }
                },
                "plusGodina" to object : NativeFunkcija("plusGodina"){
                    override fun call(vararg args: RuntimeValue): RuntimeValue {
                        if(args.size != 1 && args[0] !is Broj){
                            throw Exception("plusGodina accepts 1 argument (n: broj)")
                        }
                        val nDays = (args[0] as Broj).value.toLong()
                        return construct(ld.plusYears(nDays))
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

                        val date = LocalDate.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                        )

                        return Logicki(value = ld.isAfter(date))
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

                        val date = LocalDate.of(
                            year.toInt(),
                            month.toInt(),
                            day.toInt(),
                        )

                        return Logicki(value = ld.isBefore(date))
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
                        return Logicki(value = ld.isEqual(date))
                    }
                }
            ))
        }
    }
}