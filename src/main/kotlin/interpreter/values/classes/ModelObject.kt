package interpreter.values.classes

import interpreter.values.Funkcija
import interpreter.values.Null
import interpreter.values.RuntimeValue

data class ModelObject(
    val prototype: ModelObject?,
    var instanceObject: HashMap<String, RuntimeValue> = hashMapOf(),
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "__proto__" to (prototype ?: Null())
    ),
    override val typename: String = "model"
): RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        if(instanceObject.containsKey(prop)){
            return instanceObject[prop]!!
        }
        if(builtIns.containsKey(prop)){
            return builtIns[prop]!!
        }
        if(prototype == null){
            throw Exception("Model $typename has no property $prop")
        }
        return prototype.getProperty(prop)
    }

    fun setInstanceMember(name: String, value: RuntimeValue){
        instanceObject[name] = value
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("{\n")
        instanceObject.forEach{ (k, v) ->
            if(v !is Funkcija){
                sb.append("\t$k: $v\n")
            }
        }
        var obj = prototype
        while (obj != null){
            obj.instanceObject.forEach{ (k, v) ->
                if(v !is Funkcija && v !is Null){
                    sb.append("\t$k: $v\n")
                }
            }
            obj = obj.prototype
        }
        sb.append("}")
        return sb.toString()
    }

    fun isOfType(typeName: String): Boolean {
        if(this.typename == typeName){
            return true
        }
        if(prototype == null){
            return false
        }
        return prototype.isOfType(typeName)
    }
}