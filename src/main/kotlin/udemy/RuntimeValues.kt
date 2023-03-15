package udemy

import BlockStatement
import Environment
import FunctionParameter
import TypeAnnotation
import isInteger

interface RuntimeValue {
    val value: Any?
    val builtIns: HashMap<String, RuntimeValue>
}

data class Number(
    override val value: Double,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        if(value.isInteger()){
            val regex = Regex("\\.0\\b")
            return value.toString().replace(regex, "")
        }
        return "$value"
    }
}

data class Text(
    override val value: String,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return value
    }
}

data class Bool(
    override val value: Boolean,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return if (value) "tacno" else "netacno"
    }
}

data class Null(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return "nedefinisano"
    }
}

data class VoidReturn(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return "void"
    }
}

data class Array(
    override val value: ArrayList<RuntimeValue>,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
) : RuntimeValue {
    override fun toString(): String {
        return value.toString()
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
}

// TODO Implement toString for objects, functions, native functions


