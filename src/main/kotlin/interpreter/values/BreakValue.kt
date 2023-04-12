package interpreter.values

data class BreakValue(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "prekid"
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Break has no properties")
    }
}