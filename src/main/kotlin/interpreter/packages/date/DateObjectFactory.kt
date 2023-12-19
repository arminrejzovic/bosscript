package interpreter.packages.date

import interpreter.values.*
import java.time.LocalDate
import java.time.Period

class DateObjectFactory {
    companion object {
        fun construct(ld: LocalDate): ReadonlyObject {
            return ReadonlyObject(properties = hashMapOf(
                "godina" to Broj(value = ld.year.toDouble()),
                "mjesec" to Broj(value = ld.monthValue.toDouble()),
                "danGodine" to Broj(value = ld.dayOfYear.toDouble()),
                "danMjeseca" to Broj(value = ld.dayOfMonth.toDouble()),
                "danSedmice" to Broj(value = ld.dayOfWeek.value.toDouble()),
                "minusDana" to NativeFunction("minusDana") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'minusDana' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.minusDays(nDays))
                },
                "plusDana" to NativeFunction("plusDana") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'plusDana' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.plusDays(nDays))
                },
                "plusSedmica" to NativeFunction("plusSedmica") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'plusSedmica' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.plusWeeks(nDays))
                },
                "minusSedmica" to NativeFunction("minusSedmica") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'minusSedmica' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.minusWeeks(nDays))
                },
                "plusMjeseci" to NativeFunction("plusMjeseci") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'plusMjeseci' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.plusMonths(nDays))
                },
                "minusMjeseci" to NativeFunction("minusMjeseci") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'minusMjeseci' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.minusMonths(nDays))
                },
                "minusGodina" to NativeFunction("minusGodina") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'minusGodina' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.minusYears(nDays))
                },
                "plusGodina" to NativeFunction("plusGodina") { args ->
                    if (args.size != 1 && args[0] !is Broj) {
                        throw Exception("Funkcija 'plusGodina' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
                    }
                    val nDays = (args[0] as Broj).value.toLong()
                    return@NativeFunction construct(ld.plusYears(nDays))
                },
                "jePoslije" to NativeFunction("jePoslije") { args ->
                    if (args.size != 1 && args[0] !is Objekat) {
                        throw Exception("Funkcija 'jePoslije' prihvata 1 argument (d: Datum) (pronađeno ${args.size})")
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

                    return@NativeFunction Logicki(value = ld.isAfter(date))
                },
                "periodIzmedju" to NativeFunction("periodIzmedju") { args ->
                    if (args.size != 1 && args[0] !is Objekat) {
                        throw Exception("Funkcija 'periodIzmedju' prihvata 1 argument (d: Datum) (pronađeno ${args.size})")
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

                    val diff = Period.between(ld, date)

                    return@NativeFunction Objekat(hashMapOf(
                        "godine" to Broj(diff.years.toDouble()),
                        "mjeseci" to Broj(diff.months.toDouble()),
                        "dani" to Broj(diff.days.toDouble())
                    ))
                },
                "periodIzmeđu" to NativeFunction("periodIzmeđu") { args ->
                    if (args.size != 1 && args[0] !is Objekat) {
                        throw Exception("Funkcija 'periodIzmeđu' prihvata 1 argument (d: Datum) (pronađeno ${args.size})")
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

                    val diff = Period.between(ld, date)

                    return@NativeFunction Objekat(hashMapOf(
                        "godine" to Broj(diff.years.toDouble()),
                        "mjeseci" to Broj(diff.months.toDouble()),
                        "dani" to Broj(diff.days.toDouble())
                    ))
                },
                "jePrije" to NativeFunction("jePrije") { args ->
                    if (args.size != 1 && args[0] !is Objekat) {
                        throw Exception("Funkcija 'jePrije' prihvata 1 argument (d: Datum) (pronađeno ${args.size})")
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

                    return@NativeFunction Logicki(value = ld.isBefore(date))
                },
                "jednako" to NativeFunction("jednako") { args ->
                    if (args.size != 1 && args[0] !is Objekat) {
                        throw Exception("Funkcija 'jednako' prihvata 1 argument (d: Datum) (pronađeno ${args.size})")
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
                    return@NativeFunction Logicki(value = ld.isEqual(date))
                }
            ))
        }
    }
}