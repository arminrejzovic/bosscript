package interpreter.values

class Null: RuntimeValue {
    override fun toString(): String {
        return "nedefinisano"
    }
    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "nedefinisano"

    override fun getProperty(prop: String): RuntimeValue {
        throw NullPointerException("Null has no properties")
    }
}