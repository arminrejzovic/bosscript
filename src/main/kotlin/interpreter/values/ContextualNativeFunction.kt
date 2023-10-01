package interpreter.values

import interpreter.Interpreter

class ContextualNativeFunction(
    val name: String,
    val call: (args: ArrayList<RuntimeValue>, interpretationContext: Interpreter) -> RuntimeValue,
) : RuntimeValue {
    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "funkcija"

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Native functions do not have properties")
    }

    override fun toString(): String {
        return "ƒ $name() {[native code]}"
    }
}