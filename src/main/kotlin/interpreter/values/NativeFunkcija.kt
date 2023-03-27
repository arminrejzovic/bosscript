package interpreter.values

// TODO try moving away from anonymous classes and objects to passing a function pointer
// Refer to: https://stackoverflow.com/a/68495651/13683311
abstract class NativeFunkcija(
    val name: String,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
) : RuntimeValue {
    abstract fun call(vararg args: RuntimeValue): RuntimeValue
    override fun toString(): String {
        return "ƒ $name() {[native code]}"
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Native functions do not have properties")
    }
}