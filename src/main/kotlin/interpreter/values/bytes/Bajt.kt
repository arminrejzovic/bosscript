package interpreter.values.bytes

import interpreter.values.RuntimeValue

data class Bajt(
    override val value: Byte,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "bajt"
): RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Bajt has no property $prop")
    }
}