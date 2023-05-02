package interpreter

import interpreter.values.*
import parser.TipDefinitionStatement
import java.util.Scanner
import kotlin.random.Random

class Environment(
    private val parent: Environment? = null,
    private val variables: HashMap<String, RuntimeValue> = HashMap(),
    private val constants: MutableSet<String> = mutableSetOf(),
    private val typeDefinitions: HashMap<String, Tip> = HashMap()
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

    private fun resolveOrNull(name: String): Environment? {
        if(variables.containsKey(name)) {
            return this
        }
        if(parent == null) {
            return null
        }

        return parent.resolve(name)
    }

    fun getVariableOrNull(name: String): RuntimeValue?{
        val env = resolveOrNull(name) ?: return null
        return env.variables[name]
    }

    fun importEnv(env: Environment){
        variables.putAll(env.variables)
        constants.addAll(env.constants)
    }

    fun addModelDefinition(modelDefinition: TipDefinitionStatement){
        typeDefinitions[modelDefinition.name.symbol] = Tip(name = modelDefinition.name.symbol, properties = modelDefinition.properties)
    }

    private fun resolveTypeDefinitionEnv(name: String): Environment?{
        if(typeDefinitions[name] != null) {
            return this
        }
        if(parent == null) {
            return null
        }

        return parent.resolveTypeDefinitionEnv(name)
    }

    fun getParent(): Environment? {
        return parent
    }

    fun resolveTypeDefinition(name: String): Tip?{
        val env = resolveTypeDefinitionEnv(name) ?: return null
        return env.typeDefinitions[name]
    }

    private fun createGlobalEnvironment(env: Environment){
        env.declareVariable(
            "ispis",
            object : NativeFunkcija(name = "ispis"){
                override fun call(vararg args: RuntimeValue): Null {
                    args.forEach {
                        print(it)
                    }
                    println()
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
                    if(args.size == 1){
                        val message = (args[0] as Tekst).value
                        println(message)
                    }
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

        env.declareVariable(
            "nasumicni",
            object : NativeFunkcija(name = "nasumicni"){
                override fun call(vararg args: RuntimeValue): Broj {
                    val until = if(args.size == 1) args[0] as Broj else Broj(value = 100.0)
                    val untilVal = until.value
                    return Broj(
                        value = (Random.nextInt(until = untilVal.toInt())).toDouble()
                    )
                }
            },
            isConstant = true
        )

        env.declareVariable(
            "brojOd",
            object : NativeFunkcija("broj"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1){
                        throw Exception("Argument mismatch: Function 'broj' accepts 1 argument (obj: nepoznato)")
                    }

                    when(args[0]){
                        is Broj -> {
                            return args[0]
                        }
                        is Tekst -> {
                            val t = args[0] as Tekst
                            val numberValue = t.value.toDoubleOrNull() ?: throw Exception("Cannot parse ${t.value} to Broj.")
                            return Broj(
                                value = numberValue
                            )
                        }
                        is Logicki -> {
                            val l = args[0] as Logicki
                            return Broj(
                                value = if (l.value) 1.0 else 0.0
                            )
                        }

                        else -> {
                            throw Exception("Cannot convert ${args[0]} to Broj")
                        }
                    }
                }
            }
        )

        env.declareVariable(
            "logickiOd",
            object : NativeFunkcija("logicki"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    if(args.size != 1){
                        throw Exception("Argument mismatch: Function 'logicki' accepts 1 argument (obj: nepoznato)")
                    }

                    when(args[0]){
                        is Logicki -> {
                            return args[0]
                        }
                        is Broj -> {
                            val b = args[0] as Broj
                            return Logicki(b.value.toInt() == 0)
                        }
                        is Tekst -> {
                            val t = args[0] as Tekst

                            val boolValue = when(t.value.lowercase()){
                                "tacno" -> true
                                "tačno" -> true
                                "netacno" -> false
                                "netačno" -> false
                                else -> throw Exception("$value is not a boolean")
                            }
                            return Logicki(
                                value = boolValue
                            )
                        }

                        else -> {
                            throw Exception("Cannot convert ${args[0]} to Logicki")
                        }
                    }
                }
            }
        )

        env.declareVariable(
            "nizOd",
            object : NativeFunkcija("nizOd"){
                override fun call(vararg args: RuntimeValue): RuntimeValue {
                    return Niz(value = args.toCollection(ArrayList()))
                }
            }
        )
    }

    override fun toString(): String {
        return "Environment(variables=$variables)"
    }


}