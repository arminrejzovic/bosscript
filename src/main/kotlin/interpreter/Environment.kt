package interpreter

import java.util.Scanner

class Environment(
    private val parent: Environment? = null,
    private val variables: HashMap<String, RuntimeValue> = HashMap(),
    private val constants: MutableSet<String> = mutableSetOf()
){

    init {
        if (parent == null){
            createGlobalEnvironment(this)
        }
    }

    fun declareVariable(name: String, value: RuntimeValue, isConstant: Boolean = false): RuntimeValue {
        if(variables.containsKey(name)){
            throw Exception("Error: $name has already been defined")
        }

        variables[name] = value
        if(isConstant){
            constants.add(name)
        }
        return value
    }

    fun assignVariable(name: String, value: RuntimeValue): RuntimeValue {
        val env = resolve(name)
        if(env.constants.contains(name)){
            throw Exception("Constants cannot be reassigned")
        }
        env.variables[name] = value
        return value
    }

    private fun resolve(name: String): Environment {
        if(variables.containsKey(name)) {
            return this
        }
        if(parent == null) {
            throw Exception("$name does not exist")
        }

        return parent.resolve(name)
    }

    fun getVariable(name: String): RuntimeValue {
        val env = resolve(name)
        // We already know name is not null, so we can assert
        return env.variables[name]!!
    }

    private fun createGlobalEnvironment(env: Environment){
        env.declareVariable("pi", Broj(3.14159), isConstant = true)

        env.declareVariable(
            "ispis",
            object : NativeFunkcija(name = "ispis"){
                override fun call(vararg args: RuntimeValue): Null {
                    args.forEach {
                        println(it)
                    }
                    return Null()
                }
            },
            isConstant = true
        )

        env.declareVariable(
            "upozorenje",
            object : NativeFunkcija(name = "upozorenje"){
                override fun call(vararg args: RuntimeValue): Null {
                    val reset = "\u001b[0m"
                    val yellow = "\u001b[33m"
                    args.forEach {
                        println(yellow + "⚠ " + it.value.toString() + reset)
                    }
                    return Null()
                }
            },
            isConstant = true
        )

        env.declareVariable(
            "greska",
            object : NativeFunkcija(name = "greska"){
                override fun call(vararg args: RuntimeValue): Null {
                    val reset = "\u001b[0m"
                    val red = "\u001b[31m"

                    args.forEach {
                        println(red + "⚠ " + it.value.toString() + reset)
                    }
                    return Null()
                }
            },
            isConstant = true
        )

        env.declareVariable(
            "unos",
            object : NativeFunkcija(name = "unos"){
                override fun call(vararg args: RuntimeValue): Tekst {
                    val scanner = Scanner(System.`in`)
                    val str = scanner.nextLine()
                    return Tekst(value = str)
                }
            },
            isConstant = true
        )

        env.declareVariable(
            "postoji",
            object : NativeFunkcija(name = "postoji"){
                override fun call(vararg args: RuntimeValue): Logicki {
                    val valueInQuestion = args[0]
                    return Logicki(value = valueInQuestion !is Null)
                }
            },
            isConstant = true
        )
    }
}