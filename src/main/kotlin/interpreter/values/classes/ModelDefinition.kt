package interpreter.values.classes

import interpreter.values.Funkcija
import interpreter.values.RuntimeValue
import interpreter.values.Trait

class ModelDefinition(
    val className: String,
    val superclass: ModelDefinition?,
    val constructor: Funkcija,
    val members: HashMap<String, RuntimeValue> = hashMapOf(),
    val privateMembers: Set<String> = setOf(),
    val traits: List<Trait> = listOf(),
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "model"
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        if(builtIns.containsKey(prop)){
            return builtIns[prop]!!
        }
        if(superclass == null){
            throw Exception("Vrijednost $prop ne postoji na modelu $className")
        }
        return superclass.getProperty(prop)
    }

    override fun toString(): String {
        return """
            model $className {
                roditelj: ${superclass?.className ?: "bosscript.objekat"}
                članovi: ${members.map { entry -> "${if (entry.value is Funkcija) "ƒ " else ""}${entry.key}${if (entry.value is Funkcija) "()" else ""}"}}
                privatni: $privateMembers
            }    
        """.trimIndent()
    }

    fun isPrivate(member: String): Boolean{
        if(privateMembers.contains(member)){
            return true
        }
        if(superclass == null) {
            return false
        }
        return superclass.isPrivate(member)
    }
}