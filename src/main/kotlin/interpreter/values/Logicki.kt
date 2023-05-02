package interpreter.values

data class Logicki(
    override val value: Boolean,
) : RuntimeValue {
    override fun toString(): String {
        return if (value) "tacno" else "netacno"
    }

    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "logicki"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Bool")
    }
}