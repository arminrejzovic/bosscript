package interpreter.values

data class Niz(
    override val value: ArrayList<RuntimeValue>,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "duzina" to Broj(value = value.size.toDouble())
    ),
    override val typename: String = "niz"
) : RuntimeValue {
    override fun toString(): String {
        return value.toString()
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Array")
    }

    fun getElement(index: Int): RuntimeValue {
        return value[index]
    }

    fun set(index: Int, newValue: RuntimeValue){
        value[index] = newValue
    }
}