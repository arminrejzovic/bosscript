package interpreter.values

data class Tekst(
    override var value: String,
) : RuntimeValue {
    override fun toString(): String {
        return value
    }

    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf(
            "duzina" to duzina,
            "zavrsavaNa" to zavrsavaNa,
            "podtekst" to podtekst,
            "podtekstIndeks" to podtekstIndeks,
            "zamijeni" to zamijeni,
            "sadrzi" to sadrzi,
            "malimSlovima" to malimSlovima,
            "velikimSlovima" to velikimSlovima,
            "broj" to broj,
            "logicki" to logicki,
            "srezi" to srezi,
            "razdvoji" to razdvoji,
            "pocinjeNa" to pocinjeNa
        )
    override val typename: String
        get() = "tekst"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Text")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tekst

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    private val duzina = NativeFunction("duzina") {
        Broj(value.length.toDouble())
    }

    private val malimSlovima = NativeFunction("malimSlovima") { args ->
        if (args.isNotEmpty()) {
            throw Exception("malimSlovima accepts no arguments")
        }
        Tekst(
            value = value.lowercase()
        )
    }

    private val velikimSlovima = NativeFunction("velikimSlovima") { args ->
        if (args.isNotEmpty()) {
            throw Exception("velikimSlovima accepts no arguments")
        }
        Tekst(
            value = value.uppercase()
        )
    }

    private val broj = NativeFunction("broj") { args ->
        if (args.isNotEmpty()) {
            throw Exception("broj accepts no arguments")
        }
        Broj(
            value = value.toDouble()
        )
    }

    private val logicki = NativeFunction("logicki") { args ->
        if (args.isNotEmpty()) {
            throw Exception("logicki accepts no arguments")
        }

        val boolValue = when (value.lowercase()) {
            "tacno" -> true
            "tačno" -> true
            "netacno" -> false
            "netačno" -> false
            else -> throw Exception("$value is not a boolean")
        }
        Logicki(
            value = boolValue
        )
    }

    private val srezi = NativeFunction("srezi") { args ->
        if (args.isNotEmpty()) {
            throw Exception("logicki accepts no arguments")
        }
        Tekst(
            value = value.trim()
        )
    }

    private val razdvoji = NativeFunction("razdvoji") { args ->
        if (args.size != 1 && args[0] !is Tekst) {
            throw Exception("razdvoji accepts one argument: (delimiter: tekst)")
        }
        val delimiter = (args[0] as Tekst).value
        val strings = value.split(delimiter).map { Tekst(it) } as ArrayList<RuntimeValue>

        Niz(
            value = strings
        )
    }

    private val pocinjeNa = NativeFunction("pocinjeNa") { args ->
        if (args.size != 1 && args[0] !is Tekst) {
            throw Exception("pocinjeNa accepts one argument: (delimiter: tekst)")
        }
        val prefix = (args[0] as Tekst).value

        Logicki(
            value = value.startsWith(prefix)
        )
    }

    private val zavrsavaNa = NativeFunction("zavrsavaNa") { args ->
        if (args.size != 1 && args[0] !is Tekst) {
            throw Exception("zavrsavaNa accepts one argument: (delimiter: tekst)")
        }
        val prefix = (args[0] as Tekst).value

        Logicki(
            value = value.endsWith(prefix)
        )
    }

    private val podtekst = NativeFunction("podtekst") { args ->
        if (args.size == 1 && args[0] is Broj) {
            val startIndex = (args[0] as Broj).value.toInt()
            Tekst(
                value = value.substring(startIndex)
            )
        } else if (args.size == 2 && args[0] is Broj && args[1] is Broj) {
            val startIndex = (args[0] as Broj).value.toInt()
            val endIndex = (args[1] as Broj).value.toInt()
            Tekst(
                value = value.substring(startIndex, endIndex)
            )
        } else {
            throw Exception("podtekst accepts two arguments: (start: broj, end: broj)")
        }
    }

    private val podtekstIndeks = NativeFunction("podtekstIndeks") { args ->
        if (args.size == 1 && args[0] is Tekst) {
            val substring = (args[0] as Tekst).value
            Broj(
                value = value.indexOf(substring).toDouble()
            )
        } else {
            throw Exception("podtekstIndeks accepts one argument: (substring: Tekst)")
        }
    }

    private val zamijeni = NativeFunction("zamijeni") { args ->
        if (args.size == 2 && args[0] is Tekst && args[1] is Tekst) {
            val old = (args[0] as Tekst).value
            val new = (args[1] as Tekst).value
            Tekst(
                value = value.replace(old, new)
            )
        } else {
            throw Exception("zamijeni accepts two arguments: (old: Tekst, new: Tekst)")
        }
    }

    private val sadrzi = NativeFunction("sadrzi") { args ->
        if (args.size == 1 && args[0] is Tekst) {
            val substring = (args[0] as Tekst).value
            Logicki(
                value = value.contains(substring)
            )
        } else {
            throw Exception("sadrzi accepts one argument: (substring: Tekst)")
        }
    }

}