package interpreter.packages

import interpreter.Broj
import interpreter.Environment
import interpreter.NativeFunkcija
import interpreter.RuntimeValue
import kotlin.math.*

@OptIn(ExperimentalStdlibApi::class)
val math = Environment(
    variables = hashMapOf(
        "apsolutnaVrijednost" to object : NativeFunkcija(name = "apsolutnaVrijednost"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = abs(num.value)
                )
            }
        },

        "pi" to Broj(value = PI),

        "arccos" to object : NativeFunkcija(name = "arccos"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = acos(num.value)
                )
            }
        },

        "arcsin" to object : NativeFunkcija(name = "arcsin"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = asin(num.value)
                )
            }
        },

        "sin" to object : NativeFunkcija(name = "sin"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = sin(num.value)
                )
            }
        },

        "cos" to object : NativeFunkcija(name = "cos"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = cos(num.value)
                )
            }
        },

        "tg" to object : NativeFunkcija(name = "tg"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = tan(num.value)
                )
            }
        },

        "arctg" to object : NativeFunkcija(name = "arctg"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj
                return Broj(
                    value = atan(num.value)
                )
            }
        },

        "korijen" to object : NativeFunkcija(name = "korijen"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                if(num.value < 0){
                    throw Exception("Square root cannot be calculated for negative numbers. Value ${num.value} is invalid")
                }

                return Broj(
                    value = sqrt(num.value)
                )
            }
        },

        "e" to Broj(value = E),

        "exp" to object : NativeFunkcija(name = "exp"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                // e^num
                return Broj(
                    value = exp(num.value)
                )
            }
        },

        "ln" to object : NativeFunkcija(name = "ln"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                if(num.value < 0.0){
                    throw Exception("Cannot calculate ln of ${num.value}: Value must be a positive number")
                }

                return Broj(
                    value = ln(num.value)
                )
            }
        },

        "log" to object : NativeFunkcija(name = "log"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 2){
                    throw Exception("Function $name accepts two parameters (x: Broj, base: Broj)")
                }
                if(args[0] !is Broj){
                    throw Exception("Type Error: Value must be Broj")
                }

                if(args[1] !is Broj){
                    throw Exception("Type Error: Base must be a number")
                }

                val num = args[0] as Broj
                val base = args[1] as Broj

                return Broj(
                    value = log(num.value, base.value)
                )
            }
        },

        "log10" to object : NativeFunkcija(name = "log10"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                return Broj(
                    value = log10(num.value)
                )
            }
        },

        "log2" to object : NativeFunkcija(name = "log2"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                return Broj(
                    value = log2(num.value)
                )
            }
        },

        "max" to object : NativeFunkcija(name = "max"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 2){
                    throw Exception("Function $name accepts two parameters (value1: Broj, value2: Broj)")
                }
                if(args[0] !is Broj){
                    throw Exception("Type Error: Value1 must be Broj")
                }

                if(args[1] !is Broj){
                    throw Exception("Type Error: Value2 must be a number")
                }

                val value1 = args[0] as Broj
                val value2 = args[1] as Broj

                return Broj(
                    value = max(value1.value, value2.value)
                )
            }
        },

        "min" to object : NativeFunkcija(name = "min"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 2){
                    throw Exception("Function $name accepts two parameters (value1: Broj, value2: Broj)")
                }
                if(args[0] !is Broj){
                    throw Exception("Type Error: Value1 must be Broj")
                }

                if(args[1] !is Broj){
                    throw Exception("Type Error: Value2 must be a number")
                }

                val value1 = args[0] as Broj
                val value2 = args[1] as Broj

                return Broj(
                    value = min(value1.value, value2.value)
                )
            }
        },

        "zaokruzi" to object : NativeFunkcija(name = "zaokruzi"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                return Broj(
                    value = round(num.value)
                )
            }
        },

        "kubniKorijen" to object : NativeFunkcija(name = "kubniKorijen"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.size != 1){
                    throw Exception("Function $name accepts only one parameter")
                }
                if(args[0] !is Broj){
                    throw Exception("${args[0].javaClass.simpleName} is not a valid argument for function $name. It accepts Broj")
                }

                val num = args[0] as Broj

                return Broj(
                    value = cbrt(num.value)
                )
            }
        },

        "beskonacno" to Broj(value = Double.POSITIVE_INFINITY),

        "minusBeskonacno" to Broj(value = Double.NEGATIVE_INFINITY),


    )
)