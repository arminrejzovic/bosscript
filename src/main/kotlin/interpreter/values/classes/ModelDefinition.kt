package interpreter.values.classes

import interpreter.Environment
import interpreter.values.Funkcija
import interpreter.values.RuntimeValue
import parser.FunctionDeclaration

class ModelDefinition(
    val className: String,
    val superclass: ModelDefinition?,
    val constructor: Funkcija,
    val members: HashMap<String, RuntimeValue> = hashMapOf(),
    val privateMembers: Set<String> = setOf(),
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "model"
) : RuntimeValue{
    override fun getProperty(prop: String): RuntimeValue {
        if(builtIns.containsKey(prop)){
            return builtIns[prop]!!
        }
        if(superclass == null){
            throw Exception("Property $prop does not exist on model $className")
        }
        return superclass.getProperty(prop)
    }

    override fun toString(): String {
        return "ModelDefinition(className='$className', superclass=${superclass?.className}, constructor=$constructor, members=$members, privateMembers=$privateMembers)"
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