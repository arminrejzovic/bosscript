package interpreter

import errors.BosscriptRuntimeException
import interpreter.values.*
import parser.TipDefinitionStatement
import parser.TypeAnnotation
import typechecker.TypeChecker
import kotlin.random.Random

class Environment(
    private val parent: Environment? = null,
    private val variables: HashMap<String, RuntimeValue> = HashMap(),
    private val variableTypes: HashMap<String, TypeAnnotation> = HashMap(),
    private val constants: MutableSet<String> = mutableSetOf(),
    private val typeDefinitions: HashMap<String, Tip> = HashMap(),
    private val traits: HashMap<String, Trait> = HashMap()
){

    init {
        if (parent == null){
            createGlobalEnvironment(this)
        }
    }

    private val typeChecker = TypeChecker(this)

    fun declareVariable(name: String, value: RuntimeValue, type: TypeAnnotation? = null, isConstant: Boolean = false): RuntimeValue {
        if(variables.containsKey(name)){
            throw Exception("Varijabla $name već postoji.")
        }

        if(type != null && value !is Null){
            typeChecker.expect(type, value)
            variableTypes[name] = type
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
            throw Exception("Konstante nije moguće mijenjati.")
        }
        val expectedType = env.variableTypes[name]
        if(expectedType != null && value !is Null){
            typeChecker.expect(expectedType, value)
        }
        env.variables[name] = value
        return value
    }

    private fun resolve(name: String): Environment {
        if(variables.containsKey(name)) {
            return this
        }
        if(parent == null) {
            throw Exception("$name ne postoji.")
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
        typeDefinitions.putAll(env.typeDefinitions)
        traits.putAll(env.traits)
    }

    fun addTypeDefinition(definition: TipDefinitionStatement){
        typeDefinitions[definition.name.symbol] = Tip(name = definition.name.symbol, properties = definition.properties)
    }

    fun importTypeDefinition(definition: Tip){
        typeDefinitions[definition.name] = definition
    }

    fun declareTrait(trait: Trait){
        traits[trait.name] = trait
    }

    fun resolveTrait(name: String): Trait {
        if(traits[name] != null) {
            return traits[name]!!
        }
        if(parent == null){
            throw Exception("TODO $name")
        }
        return parent.resolveTrait(name)
    }

    fun resolveTraitNullable(name: String): Trait? {
        if(traits[name] != null) {
            return traits[name]!!
        }
        if(parent == null){
            return null
        }
        return parent.resolveTrait(name)
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
            NativeFunction(name = "ispis"){args ->
                    args.forEach {
                        print(it)
                    }
                    println()
                    Null()
                },
            isConstant = true
        )

        env.declareVariable(
            "upozorenje",
            NativeFunction(name = "upozorenje"){ args ->
                
                    val reset = "\u001b[0m"
                    val yellow = "\u001b[33m"
                    args.forEach {
                        println(yellow + "⚠ " + it.value.toString() + reset)
                    }
                    Null()
            },
            isConstant = true
        )

        env.declareVariable(
            "greska",
            NativeFunction(name = "greska"){ args ->
                
                    val reset = "\u001b[0m"
                    val red = "\u001b[31m"

                    args.forEach {
                        println(red + "⚠ " + it.value.toString() + reset)
                    }
                    Null()

            },
            isConstant = true
        )

        env.declareVariable(
            "greška",
            NativeFunction(name = "greška"){ args ->

                val reset = "\u001b[0m"
                val red = "\u001b[31m"

                args.forEach {
                    println(red + "⚠ " + it.value.toString() + reset)
                }
                Null()

            },
            isConstant = true
        )

        env.declareVariable(
            "unos",
            NativeFunction(name = "unos"){args ->
                    if(args.size == 1){
                        val message = (args[0] as Tekst).value
                        println(message)
                    }
                    val str = readln()
                    Tekst(value = str)
            },
            isConstant = true
        )

        env.declareVariable(
            "postoji",
            NativeFunction(name = "postoji"){args ->
                val valueInQuestion = args[0]
                Logicki(value = valueInQuestion !is Null)
            },
            isConstant = true
        )

        env.declareVariable(
            "nasumicni",
            NativeFunction(name = "nasumicni"){args ->
                val until = if(args.size == 1) args[0] as Broj else Broj(value = 100.0)
                val untilVal = until.value
                Broj(
                    value = (Random.nextInt(until = untilVal.toInt())).toDouble()
                )
            },
            isConstant = true
        )

        env.declareVariable(
            "nasumični",
            NativeFunction(name = "nasumični"){args ->
                val until = if(args.size == 1) args[0] as Broj else Broj(value = 100.0)
                val untilVal = until.value
                Broj(
                    value = (Random.nextInt(until = untilVal.toInt())).toDouble()
                )
            },
            isConstant = true
        )

        env.declareVariable(
            "brojOd",
            NativeFunction("brojOd"){args ->
                if(args.size != 1){
                    throw Exception("Funkcija brojOd prihvata 1 argument (obj: objekat)")
                }

                when(args[0]){
                    is Broj -> {
                        return@NativeFunction Broj((args[0] as Broj).value)
                    }
                    is Tekst -> {
                        val t = args[0] as Tekst
                        val numberValue = t.value.toDoubleOrNull() ?: throw Exception("Nije moguće pretvoriti vrijednost ${t.value} u broj")
                        return@NativeFunction Broj(
                            value = numberValue
                        )
                    }
                    is Logicki -> {
                        val l = args[0] as Logicki
                        return@NativeFunction Broj(
                            value = if (l.value) 1.0 else 0.0
                        )
                    }

                    else -> {
                        throw Exception("Nije moguće pretvoriti vrijednost ${args[0]} u broj")
                    }
                }
            }
        )

        env.declareVariable(
            "logickiOd",
            NativeFunction("logickiOd"){args ->
                if(args.size != 1){
                    throw Exception("Funkcija 'logičkiOd' prihvata 1 argument (obj: objekat)")
                }

                when(args[0]){
                    is Logicki -> {
                        return@NativeFunction Logicki((args[0] as Logicki).value)
                    }
                    is Broj -> {
                        val b = args[0] as Broj
                        return@NativeFunction Logicki(b.value.toInt() != 0)
                    }
                    is Tekst -> {
                        val t = args[0] as Tekst

                        val boolValue = when(t.value.lowercase()){
                            "tacno" -> true
                            "tačno" -> true
                            "netacno" -> false
                            "netačno" -> false
                            else -> throw Exception("'${t.value}' nije logička vrijednost")
                        }
                        return@NativeFunction Logicki(
                            value = boolValue
                        )
                    }

                    else -> {
                        throw Exception("Nije moguće pretvoriti vrijednost ${args[0]} u logički")
                    }
                }
            }
        )

        env.declareVariable(
            "logičkiOd",
            NativeFunction("logičkiOd"){args ->
                if(args.size != 1){
                    throw Exception("Funkcija 'logičkiOd' prihvata 1 argument (obj: objekat)")
                }

                when(args[0]){
                    is Logicki -> {
                        return@NativeFunction Logicki((args[0] as Logicki).value)
                    }
                    is Broj -> {
                        val b = args[0] as Broj
                        return@NativeFunction Logicki(b.value.toInt() != 0)
                    }
                    is Tekst -> {
                        val t = args[0] as Tekst

                        val boolValue = when(t.value.lowercase()){
                            "tacno" -> true
                            "tačno" -> true
                            "netacno" -> false
                            "netačno" -> false
                            else -> throw Exception("'${t.value}' nije logička vrijednost")
                        }
                        return@NativeFunction Logicki(
                            value = boolValue
                        )
                    }

                    else -> {
                        throw Exception("Nije moguće pretvoriti vrijednost ${args[0]} u logički")
                    }
                }
            }
        )

        env.declareVariable(
            "nizOd",
            NativeFunction("nizOd"){args ->
                Niz(value = args.toCollection(ArrayList()))
            }
        )

        env.declareVariable(
            "tipOd",
            NativeFunction("tipOd"){args ->
                Tekst(args[0].typename)
            }
        )

        env.declareVariable(
            "izazoviGrešku",
            NativeFunction("izazoviGrešku"){ args ->
                if (args.size != 1 || args[0] !is Objekat){
                    throw RuntimeException("Funkcija 'izazoviGrešku' očekuje 1 argument (greška: objekat)")
                }
                val exception = args[0] as Objekat
                throw BosscriptRuntimeException(exception, args[0].toString(), Pair(0, 0))
            }
        )

        env.declareVariable(
            "izazoviGresku",
            NativeFunction("izazoviGresku"){ args ->
                if (args.size != 1 || args[0] !is Objekat){
                    throw RuntimeException("Funkcija 'izazoviGresku' očekuje 1 argument (greska: objekat)")
                }
                val exception = args[0] as Objekat
                throw BosscriptRuntimeException(exception, args[0].toString(), Pair(0, 0))
            }
        )
    }

    private fun checkIdentifierAlreadyInUse(identifier: String){
        return
    }

    override fun toString(): String {
        return "Environment(variables=$variables)"
    }

}