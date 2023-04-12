package interpreter.values

interface RuntimeValue {
    val value: Any?
    val builtIns: HashMap<String, RuntimeValue>
    val typename: String
    fun getProperty(prop: String): RuntimeValue
}