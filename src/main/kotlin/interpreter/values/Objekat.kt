package interpreter.values

data class Objekat(
    val properties: HashMap<String, RuntimeValue>,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
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
        return (builtIns[prop] ?: properties[prop]) ?: throw Exception("Property $prop does not exist on object")
    }

    fun setProperty(prop: String, newValue: RuntimeValue): RuntimeValue {
        properties[prop] = newValue
        return properties[prop]!!
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }
}