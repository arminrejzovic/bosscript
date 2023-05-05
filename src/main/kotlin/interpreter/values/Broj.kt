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
            "tekst" to tekst
        )
    override val typename: String
        get() = "broj"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Number")
    }

    override fun toString(): String {
        if (value.isInt()) {
            val regex = Regex("\\.0\\b")
            return value.toString().replace(regex, "")
        }
        return "$value"
    }

    private val zaokruzi = NativeFunction("zaokruzi") { args ->
        if (args.isNotEmpty()) {
            throw Exception("Function 'zaokruzi' takes 0 arguments")
        }
        Broj(value = value.roundToInt().toDouble())
    }

    private val tekst = NativeFunction("tekst") { args ->
        Tekst(value = value.toString())
    }

}