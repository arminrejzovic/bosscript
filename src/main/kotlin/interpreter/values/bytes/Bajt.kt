package interpreter.values.bytes

import interpreter.values.RuntimeValue

data class Bajt(
    override val value: Byte,
): RuntimeValue {
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "bajt"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Vrijednost '$prop' ne postoji na tipu 'bajt'")
    }
}