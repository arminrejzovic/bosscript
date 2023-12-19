package interpreter.packages

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.NativeFunction
import kotlin.math.*

@OptIn(ExperimentalStdlibApi::class)
val math = Environment(
    variables = hashMapOf(
        "apsolutnaVrijednost" to NativeFunction(name = "apsolutnaVrijednost"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'apsolutnaVrijednost' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = abs(num.value)
            )
        },

        "pi" to Broj(value = PI),

        "arccos" to NativeFunction(name = "arccos"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'arccos' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = acos(num.value)
            )
        },

        "arcsin" to NativeFunction(name = "arcsin"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'arcsin' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = asin(num.value)
            )
        },

        "sin" to NativeFunction(name = "sin"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'sin' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = sin(Math.toRadians(num.value))
            )
        },

        "cos" to NativeFunction(name = "cos"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'cos' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = cos(Math.toRadians(num.value))
            )
        },

        "tg" to NativeFunction(name = "tg"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'tg' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = tan(Math.toRadians(num.value))
            )
        },

        "arctg" to NativeFunction(name = "arctg"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'arctg' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = atan(num.value)
            )
        },

        "korijen" to NativeFunction(name = "korijen"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'korijen' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            if(num.value < 0){
                throw Exception("Argument 'n' mora biti pozitivan broj. Nije moguće izračunati korijen negativnog broja.")
            }

            return@NativeFunction Broj(
                value = sqrt(num.value)
            )
        },

        "e" to Broj(value = E),

        "eNa" to NativeFunction(name = "eNa"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'eNa' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            // e^num
            return@NativeFunction Broj(
                value = exp(num.value)
            )
        },

        "ln" to NativeFunction(name = "ln"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'ln' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            if(num.value < 0.0){
                throw Exception("Argument 'n' mora biti pozitivan broj. Nije moguće izračunati prirodni logaritam negativnog broja.")
            }

            return@NativeFunction Broj(
                value = ln(num.value)
            )
        },

        "log" to NativeFunction(name = "log"){args ->
            if (args.size != 2 || args[0] !is Broj || args[1] !is Broj){
                throw Exception("Funkcija 'log' prihvata 2 argumenta (n: broj, baza: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj
            val base = args[1] as Broj

            return@NativeFunction Broj(
                value = log(num.value, base.value)
            )
        },

        "log10" to NativeFunction(name = "log10"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'log10' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = log10(num.value)
            )
        },

        "log2" to NativeFunction(name = "log2"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'log2' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = log2(num.value)
            )
        },

        "max" to NativeFunction(name = "max"){args ->
            if (args.size != 2 || args[0] !is Broj || args[1] !is Broj){
                throw Exception("Funkcija 'max' prihvata 2 argumenta (x: broj, y: broj) (pronađeno ${args.size})")
            }

            val value1 = args[0] as Broj
            val value2 = args[1] as Broj

            return@NativeFunction Broj(
                value = max(value1.value, value2.value)
            )
        },

        "min" to NativeFunction(name = "min"){args ->
            if (args.size != 2 || args[0] !is Broj || args[1] !is Broj){
                throw Exception("Funkcija 'min' prihvata 2 argumenta (x: broj, y: broj) (pronađeno ${args.size})")
            }

            val value1 = args[0] as Broj
            val value2 = args[1] as Broj

            return@NativeFunction Broj(
                value = min(value1.value, value2.value)
            )
        },

        "zaokruzi" to NativeFunction(name = "zaokruzi"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'zaokruzi' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = round(num.value)
            )
        },

        "zaokruži" to NativeFunction(name = "zaokruži"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'zaokruži' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = round(num.value)
            )
        },

        "kubniKorijen" to NativeFunction(name = "kubniKorijen"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'kubniKorijen' prihvata 1 argument (n: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = cbrt(num.value)
            )
        },

        "uStepenima" to NativeFunction(name = "uStepenima"){args ->
            if (args.size != 1 || args[0] !is Broj){
                throw Exception("Funkcija 'zaokruži' prihvata 1 argument (radijani: broj) (pronađeno ${args.size})")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = Math.toDegrees(num.value)
            )
        },

        "beskonacno" to Broj(value = Double.POSITIVE_INFINITY),

        "minusBeskonacno" to Broj(value = Double.NEGATIVE_INFINITY),
    )
)