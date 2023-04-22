package interpreter.values

import isInt
import kotlin.math.roundToInt

data class Broj(
    override var value: Double,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "zaokruzi" to object : NativeFunkcija(name = "zaokruzi"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.isNotEmpty()){
                    throw Exception("Function 'zaokruzi' takes 0 arguments")
                }
                return Broj(
                    value = value.roundToInt().toDouble()
                )
            }
        },
        "tekst" to object : NativeFunkcija(name = "tekst"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                return Tekst(
                    value = value.toString()
                )
            }

        }
    ),
    override val typename: String = "broj"
) : RuntimeValue {

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
}