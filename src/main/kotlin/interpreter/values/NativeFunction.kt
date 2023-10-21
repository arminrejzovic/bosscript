package interpreter.values

class NativeFunction(
    val name: String,
    val call: (args: List<RuntimeValue>) -> RuntimeValue,
): RuntimeValue {
    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "funkcija"

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Native funkcije nemaju pripadajuće vrijednosti.")
    }

    override fun toString(): String {
        return "ƒ $name() {[native kod]}"
    }
}