import udemy.Bool
import udemy.Function
import udemy.Null
import udemy.Number
import udemy.RuntimeValue
import java.util.Scanner

class Environment(
    private val parent: Environment? = null,
    private val variables: HashMap<String, RuntimeValue> = HashMap(),
    private val constants: MutableSet<String> = mutableSetOf()
){

    init {
        if (parent == null){
            createScope(this)
        }
    }

    fun declareVariable(name: String, value: RuntimeValue, isConstant: Boolean = false): RuntimeValue{
        if(variables.containsKey(name)){
            throw Exception("Error: $name has already been defined")
        }

        variables[name] = value
        if(isConstant){
            constants.add(name)
        }
        return value
    }

    fun assignVariable(name: String, value: RuntimeValue): RuntimeValue{
        val env = resolve(name)
        if(env.constants.contains(name)){
            throw Exception("Constants cannot be reassigned")
        }
        env.variables[name] = value
        return value
    }

    private fun resolve(name: String): Environment{
        if(variables.containsKey(name)) {
            return this
        }
        if(parent == null) {
            throw Exception("$name does not exist")
        }

        return parent.resolve(name)
    }

    fun getVariable(name: String): RuntimeValue{
        val env = resolve(name)
        // We already know name is not null, so we can assert
        return env.variables[name]!!
    }

    fun createScope(env: Environment){
        env.declareVariable("pi", Number(3.14159), isConstant = true)
        env.declareVariable("tacno", Bool(true), isConstant = true)
        env.declareVariable("netacno", Bool(false), isConstant = true)
        env.declareVariable("nista", Null(), isConstant = true)
    }
}