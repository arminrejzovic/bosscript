package interpreter.values

import interpreter.packages.JSONStringify
import java.lang.StringBuilder

data class Objekat(
    val properties: HashMap<String, RuntimeValue>,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override var typename: String = "objekat"
) : RuntimeValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Objekat

        if (properties != other.properties) return false

        return true
    }

    override fun toString(): String {
        return properties.toString().replace("=", ": ")
    }

    override fun getProperty(prop: String): RuntimeValue {
        return (builtIns[prop] ?: properties[prop]) ?: throw Exception("Property $prop does not exist on object $this")
    }

    fun setProperty(prop: String, newValue: RuntimeValue): RuntimeValue {
        properties[prop] = newValue
        return properties[prop]!!
    }

    fun JSONString(): String{
        val sb = StringBuilder("{")
        properties.forEach {
            if(it.value !is Funkcija && it.value !is NativeFunkcija && it.value !is ContextualNativeFunction){
                sb.append("\"${it.key}\": ${JSONStringify(it.value)}")
                sb.append(", ")
            }
        }
        sb.append("\b\b}")
        return sb.toString()
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }
}