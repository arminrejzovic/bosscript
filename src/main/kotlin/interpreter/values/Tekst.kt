package interpreter.values
data class Tekst(
    override var value: String,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "malimSlovima" to object : NativeFunkcija(name = "malimSlovima") {
            override fun call(vararg args: RuntimeValue): Tekst {
                if (args.isNotEmpty()) {
                    throw Exception("malimSlovima accepts no arguments")
                }
                return Tekst(
                    value = value.lowercase()
                )
            }
        },

        "velikimSlovima" to object : NativeFunkcija(name = "velikimSlovima") {
            override fun call(vararg args: RuntimeValue): Tekst {
                if (args.isNotEmpty()) {
                    throw Exception("velikimSlovima accepts no arguments")
                }
                return Tekst(
                    value = value.uppercase()
                )
            }
        },

        "duzina" to Broj(value = value.length.toDouble()),

        "broj" to object : NativeFunkcija(name = "broj") {
            override fun call(vararg args: RuntimeValue): Broj {
                if (args.isNotEmpty()) {
                    throw Exception("broj accepts no arguments")
                }
                return Broj(
                    value = value.toDouble()
                )
            }
        },

        "logicki" to object : NativeFunkcija(name = "logicki") {
            override fun call(vararg args: RuntimeValue): Logicki {
                if (args.isNotEmpty()) {
                    throw Exception("logicki accepts no arguments")
                }
                return Logicki(
                    value = value.toBooleanStrict()
                )
            }
        },

        "srezi" to object : NativeFunkcija(name = "srezi") {
            override fun call(vararg args: RuntimeValue): Tekst {
                if (args.isNotEmpty()) {
                    throw Exception("logicki accepts no arguments")
                }
                return Tekst(
                    value = value.trim()
                )
            }
        },

        "razdvoji" to object : NativeFunkcija(name = "srezi") {
            override fun call(vararg args: RuntimeValue): Niz {
                if (args.size != 1 && args[0] !is Tekst) {
                    throw Exception("razdvoji accepts one argument: (delimiter: tekst)")
                }
                val delimiter = (args[0] as Tekst).value
                val strings = value.split(delimiter).map { Tekst(it) } as ArrayList<RuntimeValue>

                return Niz(
                    value = strings
                )
            }
        },

        "pocinjeNa" to object : NativeFunkcija(name = "pocinjeNa") {
            override fun call(vararg args: RuntimeValue): Logicki {
                if (args.size != 1 && args[0] !is Tekst) {
                    throw Exception("pocinjeNa accepts one argument: (delimiter: tekst)")
                }
                val prefix = (args[0] as Tekst).value

                return Logicki(
                    value = value.startsWith(prefix)
                )
            }
        },

        "zavrsavaNa" to object : NativeFunkcija(name = "zavrsavaNa") {
            override fun call(vararg args: RuntimeValue): Logicki {
                if (args.size != 1 && args[0] !is Tekst) {
                    throw Exception("zavrsavaNa accepts one argument: (delimiter: tekst)")
                }
                val prefix = (args[0] as Tekst).value

                return Logicki(
                    value = value.endsWith(prefix)
                )
            }
        },

        "podtekst" to object : NativeFunkcija(name = "podtekst") {
            override fun call(vararg args: RuntimeValue): Tekst {
                if(args.size == 1 && args[0] is Broj){
                    val startIndex = (args[0] as Broj).value.toInt()
                    return Tekst(
                        value = value.substring(startIndex)
                    )
                }

                if(args.size == 2 && args[0] is Broj && args[1] is Broj){
                    val startIndex = (args[0] as Broj).value.toInt()
                    val endIndex = (args[1] as Broj).value.toInt()
                    return Tekst(
                        value = value.substring(startIndex, endIndex)
                    )
                }

                throw Exception("podtekst accepts two arguments: (start: broj, end: broj)")
            }
        },

        "podtekstIndeks" to object : NativeFunkcija(name = "podtekstIndeks") {
            override fun call(vararg args: RuntimeValue): Broj {
                if(args.size == 1 && args[0] is Tekst){
                    val substring = (args[0] as Tekst).value
                    return Broj(
                        value = value.indexOf(substring).toDouble()
                    )
                }

                throw Exception("podtekstIndeks accepts one argument: (substring: Tekst)")
            }
        },

        "zamijeni" to object : NativeFunkcija(name = "zamijeni") {
            override fun call(vararg args: RuntimeValue): Tekst {
                if(args.size == 2 && args[0] is Tekst && args[1] is Tekst){
                    val old = (args[0] as Tekst).value
                    val new = (args[1] as Tekst).value
                    return Tekst(
                        value = value.replace(old, new)
                    )
                }

                throw Exception("zamijeni accepts two arguments: (old: Tekst, new: Tekst)")
            }
        },

        "sadrzi" to object : NativeFunkcija(name = "sadrzi") {
            override fun call(vararg args: RuntimeValue): Logicki {
                if(args.size == 1 && args[0] is Tekst){
                    val substring = (args[0] as Tekst).value
                    return Logicki(
                        value = value.contains(substring)
                    )
                }

                throw Exception("podtekstIndeks accepts one argument: (substring: Tekst)")
            }
        },
    )
) : RuntimeValue {
    override fun toString(): String {
        return value
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Text")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tekst

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}