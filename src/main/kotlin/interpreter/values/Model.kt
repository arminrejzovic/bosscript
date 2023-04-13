package interpreter.values

import interpreter.Environment
import parser.ModelProperty
import typechecker.TypeChecker

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

    fun constructor(args: ArrayList<RuntimeValue>, env: Environment): Objekat{
        if(args.size < properties.size){
            throw Exception("Type Error: Missing properties")
        }

        if(args.size > properties.size){
            throw Exception("Type Error:  properties")
        }

        val typeChecker = TypeChecker(env)

        for ((i, prop) in properties.withIndex()){
            typeChecker.expect(expectedType=prop.type, providedValue=args[i])
        }

        return Objekat(
            properties = properties.zip(args){prop, arg -> prop.name to arg}.toMap(HashMap())
        )
    }
}