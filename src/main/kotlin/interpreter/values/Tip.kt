package interpreter.values

import interpreter.Environment
import parser.TypeProperty
import typechecker.TypeChecker

data class Tip(
    val name: String,
    val properties: ArrayList<TypeProperty>,
) : RuntimeValue {
    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = "Tip"

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Definicija tipa nema pripadajuće vrijednosti.")
    }

    fun constructor(args: ArrayList<RuntimeValue>, env: Environment): Objekat{
        if(args.size < properties.size){
            throw Exception("Nedovoljno argumenata: Konstruktor tipa $name prihvata ${properties.size} argumenata.")
        }

        if(args.size > properties.size){
            throw Exception("Višak argumenata: Konstruktor tipa $name prihvata ${properties.size} argumenata.")
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