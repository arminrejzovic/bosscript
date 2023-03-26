package interpreter.values

import isInt

data class Broj(
    override var value: Double,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
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