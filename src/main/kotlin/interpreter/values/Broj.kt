package interpreter.values

import isInt
import kotlin.math.roundToInt

data class Broj(
    override var value: Double,
) : RuntimeValue {
    constructor(n: Int) : this(n.toDouble())
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf(
            "zaokruzi" to zaokruzi,
            "zaokruži" to zaokruzi,
            "tekst" to tekst
        )
    override val typename: String
        get() = "broj"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Vrijednost '$prop' ne postoji na tipu 'broj'")
    }

    override fun toString(): String {
        if (value.isInt()) {
            val regex = Regex("\\.0\\b")
            return value.toString().replace(regex, "")
        }
        return "$value"
    }

    private val zaokruzi = NativeFunction("zaokruži") { args ->
        if (args.isNotEmpty()) {
            throw Exception("Funkcija 'zaokruži' prihvata 0 argumenata.")
        }
        Broj(value = value.roundToInt().toDouble())
    }

    private val tekst = NativeFunction("tekst") { args ->
        Tekst(value = value.toString())
    }

}