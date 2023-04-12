package interpreter.values

data class Logicki(
    override val value: Boolean,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "logicki"
) : RuntimeValue {
    override fun toString(): String {
        return if (value) "tacno" else "netacno"
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Bool")
    }
}