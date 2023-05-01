package interpreter.packages

import interpreter.Environment
import interpreter.values.*

val Converter = Environment(
    variables = hashMapOf(
        "broj" to object : NativeFunkcija("broj"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1){
                    throw Exception("Argument mismatch: Function 'broj' accepts 1 argument (obj: nepoznato)")
                }

                when(args[0]){
                    is Broj -> {
                        return args[0]
                    }
                    is Tekst -> {
                        val t = args[0] as Tekst
                        val numberValue = t.value.toDoubleOrNull() ?: throw Exception("Cannot parse ${t.value} to Broj.")
                        return Broj(
                            value = numberValue
                        )
                    }
                    is Logicki -> {
                        val l = args[0] as Logicki
                        return Broj(
                            value = if (l.value) 1.0 else 0.0
                        )
                    }

                    else -> {
                        throw Exception("Cannot convert ${args[0]} to Broj")
                    }
                }
            }
        },
        "logicki" to object : NativeFunkcija("logicki"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1){
                    throw Exception("Argument mismatch: Function 'logicki' accepts 1 argument (obj: nepoznato)")
                }

                when(args[0]){
                    is Logicki -> {
                        return args[0]
                    }
                    is Broj -> {
                        val b = args[0] as Broj
                        return Logicki(b.value.toInt() == 0)
                    }
                    is Tekst -> {
                        val t = args[0] as Tekst

                        val boolValue = when(t.value.lowercase()){
                            "tacno" -> true
                            "tačno" -> true
                            "netacno" -> false
                            "netačno" -> false
                            else -> throw Exception("$value is not a boolean")
                        }
                        return Logicki(
                            value = boolValue
                        )
                    }

                    else -> {
                        throw Exception("Cannot convert ${args[0]} to Logicki")
                    }
                }
            }
        },
        "nizOd" to object : NativeFunkcija("nizOd"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                val arr = arrayListOf<RuntimeValue>()
                args.forEach {
                    arr.add(it)
                }
                return Niz(value = arr)
            }
        }
    )
)