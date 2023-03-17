package udemy

import BlockStatement
import Environment
import FunctionParameter
import TypeAnnotation
import errors.SyntaxError
import isInteger

interface RuntimeValue {
    val value: Any?
    val builtIns: HashMap<String, RuntimeValue>
    fun getProperty(prop: String): RuntimeValue
}

data class Number(
    override val value: Double,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Number")
    }

    override fun toString(): String {
        if (value.isInteger()) {
            val regex = Regex("\\.0\\b")
            return value.toString().replace(regex, "")
        }
        return "$value"
    }
}

data class Text(
    override val value: String,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "malaSlova" to object : NativeFunction(name = "malaSlova") {
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.isNotEmpty()) {
                    throw Exception("malaSlova accepts no arguments")
                }
                return Text(
                    value = value.lowercase()
                )
            }
        },
        "duzina" to Number(value = value.length.toDouble())
    )
) : RuntimeValue {
    override fun toString(): String {
        return value
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Text")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Text

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class Bool(
    override val value: Boolean,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return if (value) "tacno" else "netacno"
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Bool")
    }
}

data class Null(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return "nedefinisano"
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw NullPointerException("Null has no properties")
    }
}

data class ReturnValue(
    override val value: RuntimeValue,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
) : RuntimeValue {
    init {
        /*
         A ReturnValue is a wrapper that exists just so that return statements can be bubbled up to the top level
         Its value can't be another ReturnValue (since you cant say return return x)
         This should never happen in reality, but let's have code handling it just in case
         */
        if (value is ReturnValue) {
            throw SyntaxError("Something went wrong")
        }
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("ReturnValue members should never be accessed")
    }
}

data class Array(
    override val value: ArrayList<RuntimeValue>,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return value.toString()
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Array")
    }

    fun getElement(index: Int): RuntimeValue{
        return value[index]
    }
}

data class Object(
    val properties: HashMap<String, RuntimeValue>,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Object

        if (properties != other.properties) return false

        return true
    }

    override fun toString(): String {
        return properties.toString().replace("=", ": ")
    }

    override fun getProperty(prop: String): RuntimeValue {
        return (builtIns[prop] ?: properties[prop]) ?: throw Exception("Property $prop does not exist on object")
    }

    fun setProperty(prop: String, newValue: RuntimeValue): RuntimeValue{
        properties[prop] = newValue
        return properties[prop]!!
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }
}

data class Function(
    val name: String,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    val parentEnv: Environment?, // closure
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
) : RuntimeValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Function

        if (name != other.name) return false
        if (params != other.params) return false
        if (returnType != other.returnType) return false
        if (body != other.body) return false

        return true
    }

    override fun toString(): String {
        val paramString =
            params.map { "${it.identifier.symbol}:${it.type?.typeName ?: "nepoznato"}" }
                .toString()
                .replace("[", "")
                .replace("]", "")
        val italics = "\u001B[3m"
        val reset = "\u001B[0m"
        return "ƒ $name($paramString) → ${returnType?.typeName ?: "${italics}nepoznato${reset}"}"
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Function")
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + (returnType?.hashCode() ?: 0)
        result = 31 * result + body.hashCode()
        return result
    }
}

abstract class NativeFunction(
    val name: String,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    abstract fun call(vararg args: RuntimeValue): RuntimeValue
    override fun toString(): String {
        return "ƒ $name() {[native code]}"
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Native functions do not have properties")
    }
}


