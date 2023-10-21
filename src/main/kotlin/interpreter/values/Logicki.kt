package interpreter.values

data class Logicki(
    override val value: Boolean,
) : RuntimeValue {
    override fun toString(): String {
        return if (value) "tačno" else "netačno"
    }

    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "logički"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Vrijednost '$prop' ne postoji na tipu 'logički'")
    }
}