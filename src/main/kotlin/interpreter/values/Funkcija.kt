package interpreter.values

import interpreter.Environment
import parser.BlockStatement
import parser.FunctionParameter
import parser.TypeAnnotation

data class Funkcija(
    val name: String,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    val parentEnv: Environment?, // closure
) : RuntimeValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Funkcija

        if (name != other.name) return false
        if (params != other.params) return false
        if (returnType != other.returnType) return false
        if (body != other.body) return false

        return true
    }

    override fun toString(): String {
        val paramString =
            params.map { "${it.identifier}:${it.type?.typeName ?: "nepoznato"}" }
                .toString()
                .replace("[", "")
                .replace("]", "")
        val italics = "\u001B[3m"
        val reset = "\u001B[0m"
        return "ƒ $name($paramString) → ${returnType?.typeName ?: "${italics}nepoznato${reset}"}"
    }

    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "funkcija"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Vrijednost '$prop' ne postoji na tipu 'funkcija'")
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + (returnType?.hashCode() ?: 0)
        result = 31 * result + body.hashCode()
        return result
    }
}