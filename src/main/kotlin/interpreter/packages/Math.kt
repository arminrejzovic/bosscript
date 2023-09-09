package interpreter.packages

import interpreter.Environment
import interpreter.values.*
import kotlin.math.*

@OptIn(ExperimentalStdlibApi::class)
val math = Environment(
    variables = hashMapOf(
        "apsolutnaVrijednost" to NativeFunction(name = "apsolutnaVrijednost"){args ->
            if (args.size != 1){
                throw Exception("Function apsolutnaVrijednost accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function apsolutnaVrijednost. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = abs(num.value)
            )
        },

        "pi" to Broj(value = PI),

        "arccos" to NativeFunction(name = "arccos"){args ->
            if (args.size != 1){
                throw Exception("Function arccos accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function arccos. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = acos(num.value)
            )
        },

        "arcsin" to NativeFunction(name = "arcsin"){args ->
            if (args.size != 1){
                throw Exception("Function arcsin accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function arcsin. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = asin(num.value)
            )
        },

        "sin" to NativeFunction(name = "sin"){args ->
            if (args.size != 1){
                throw Exception("Function sin accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function sin. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = sin(Math.toRadians(num.value))
            )
        },

        "cos" to NativeFunction(name = "cos"){args ->
            if (args.size != 1){
                throw Exception("Function cos accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function cos. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = cos(Math.toRadians(num.value))
            )
        },

        "tg" to NativeFunction(name = "tg"){args ->
            if (args.size != 1){
                throw Exception("Function tg accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function tg. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = tan(Math.toRadians(num.value))
            )
        },

        "arctg" to NativeFunction(name = "arctg"){args ->
            if (args.size != 1){
                throw Exception("Function arctg accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function arctg. It accepts Broj")
            }

            val num = args[0] as Broj
            return@NativeFunction Broj(
                value = atan(num.value)
            )
        },

        "korijen" to NativeFunction(name = "korijen"){args ->
            if (args.size != 1){
                throw Exception("Function korijen accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function korijen. It accepts Broj")
            }

            val num = args[0] as Broj

            if(num.value < 0){
                throw Exception("Square root cannot be calculated for negative numbers. Value ${num.value} is invalid")
            }

            return@NativeFunction Broj(
                value = sqrt(num.value)
            )
        },

        "e" to Broj(value = E),

        "eNa" to NativeFunction(name = "eNa"){args ->
            if (args.size != 1){
                throw Exception("Function eNa accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function eNa. It accepts Broj")
            }

            val num = args[0] as Broj

            // e^num
            return@NativeFunction Broj(
                value = exp(num.value)
            )
        },

        "ln" to NativeFunction(name = "ln"){args ->
            if (args.size != 1){
                throw Exception("Function ln accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function ln. It accepts Broj")
            }

            val num = args[0] as Broj

            if(num.value < 0.0){
                throw Exception("Cannot calculate ln of ${num.value}: Value must be a positive number")
            }

            return@NativeFunction Broj(
                value = ln(num.value)
            )
        },

        "log" to NativeFunction(name = "log"){args ->
            if (args.size != 2){
                throw Exception("Function log accepts two parameters (x: Broj, base: Broj)")
            }
            if(args[0] !is Broj){
                throw Exception("Type Error: Value must be Broj")
            }

            if(args[1] !is Broj){
                throw Exception("Type Error: Base must be a number")
            }

            val num = args[0] as Broj
            val base = args[1] as Broj

            return@NativeFunction Broj(
                value = log(num.value, base.value)
            )
        },

        "log10" to NativeFunction(name = "log10"){args ->
            if (args.size != 1){
                throw Exception("Function log10 accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function log10. It accepts Broj")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = log10(num.value)
            )
        },

        "log2" to NativeFunction(name = "log2"){args ->
            if (args.size != 1){
                throw Exception("Function log2 accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function log2. It accepts Broj")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = log2(num.value)
            )
        },

        "max" to NativeFunction(name = "max"){args ->
            if (args.size != 2){
                throw Exception("Function max accepts two parameters (value1: Broj, value2: Broj)")
            }
            if(args[0] !is Broj){
                throw Exception("Type Error: Value1 must be Broj")
            }

            if(args[1] !is Broj){
                throw Exception("Type Error: Value2 must be a number")
            }

            val value1 = args[0] as Broj
            val value2 = args[1] as Broj

            return@NativeFunction Broj(
                value = max(value1.value, value2.value)
            )
        },

        "min" to NativeFunction(name = "min"){args ->
            if (args.size != 2){
                throw Exception("Function min accepts two parameters (value1: Broj, value2: Broj)")
            }
            if(args[0] !is Broj){
                throw Exception("Type Error: Value1 must be Broj")
            }

            if(args[1] !is Broj){
                throw Exception("Type Error: Value2 must be a number")
            }

            val value1 = args[0] as Broj
            val value2 = args[1] as Broj

            return@NativeFunction Broj(
                value = min(value1.value, value2.value)
            )
        },

        "zaokruzi" to NativeFunction(name = "zaokruzi"){args ->
            if (args.size != 1){
                throw Exception("Function zaokruzi accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function zaokruzi. It accepts Broj")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = round(num.value)
            )
        },

        "kubniKorijen" to NativeFunction(name = "kubniKorijen"){args ->
            if (args.size != 1){
                throw Exception("Function kubniKorijen accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function kubniKorijen. It accepts Broj")
            }

            val num = args[0] as Broj

            return@NativeFunction Broj(
                value = cbrt(num.value)
            )
        },

        "uStepenima" to NativeFunction(name = "uStepenima"){args ->
            if (args.size != 1){
                throw Exception("Function uStepenima accepts only one parameter")
            }
            if(args[0] !is Broj){
                throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function uStepenima. It accepts Broj")
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