package interpreter.values

import interpreter.Interpreter

data class ContextualNativeFunction(
    val name: String,
    override val value: Any?,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    val call: (args: ArrayList<RuntimeValue>, interpretationContext: Interpreter) -> RuntimeValue,
    override val typename: String = "funkcija"
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Native functions do not have properties")
    }

    override fun toString(): String {
        return "ƒ $name() {[native code]}"
    }
}