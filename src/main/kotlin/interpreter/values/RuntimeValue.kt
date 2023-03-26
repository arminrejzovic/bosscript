package interpreter.values

interface RuntimeValue {
    val value: Any?
    val builtIns: HashMap<String, RuntimeValue>
    fun getProperty(prop: String): RuntimeValue
}