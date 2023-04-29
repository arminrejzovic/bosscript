package interpreter.values.classes

import interpreter.Environment
import interpreter.values.RuntimeValue

class ModelDefinition(
    val className: String,
    val classEnv: Environment,
    val superclass: ModelDefinition?,
    val privateMembers: Set<String> = setOf(),
    override val value: Any?,
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
        return "ModelDefinition(className='$className', classEnv=$classEnv, superclass=$superclass, privateMembers=$privateMembers)"
    }
}