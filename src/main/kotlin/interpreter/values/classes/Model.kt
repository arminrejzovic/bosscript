package interpreter.values.classes

import interpreter.Environment
import interpreter.values.RuntimeValue

data class Model(
    val instanceEnv: Environment,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "model",
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        return instanceEnv.getVariable(prop)
    }
}