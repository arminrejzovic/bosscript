package interpreter.values

import parser.ModelProperty
import type_checker.TypeChecker

data class Model(
    val name: String,
    val properties: ArrayList<ModelProperty>,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
    override val typename: String = "Model"
) : RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Model has no properties of itself")
    }

    fun constructor(args: ArrayList<RuntimeValue>): Objekat{
        if(args.size != properties.size){
            throw Exception("Type Error: Missing properties")
        }

        val typeChecker = TypeChecker()

        for ((i, prop) in properties.withIndex()){
            if(prop.type.isArrayType){
                if(!typeChecker.isExpectedArrayType(prop.type.typeName, args[i])){
                    throw Exception("Type error")
                }
            }
            if(!typeChecker.isExpectedPrimitiveType(prop.type.typeName, args[i])){
                throw Exception("Type error")
            }
        }

        return Objekat(
            properties = properties.zip(args){prop, arg -> prop.name to arg}.toMap(HashMap())
        )
    }
}