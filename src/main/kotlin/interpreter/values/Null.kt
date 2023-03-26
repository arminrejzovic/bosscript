package interpreter.values

data class Null(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return "nedefinisano"
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw NullPointerException("Null has no properties")
    }
}