package interpreter.values

import flatten
import interpreter.Environment
import isInt

data class Niz(
    override val value: ArrayList<RuntimeValue>,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "duzina" to Broj(value = value.size.toDouble()),
        "dodaj" to object : NativeFunkcija(name = "dodaj"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1){
                    throw Exception("Type Error: Function 'dodaj' accepts 1 argument (item: nepoznato)")
                }
                value.add(args[0])
                return Null()
            }
        },
        "dodajNaPocetak" to object : NativeFunkcija(name = "dodajNaPocetak"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1){
                    throw Exception("Type Error: Function 'dodajNaPocetak' accepts 1 argument (item: nepoznato)")
                }
                value.add(0, args[0])
                return Broj(
                    value = value.size.toDouble()
                )
            }
        },
        "spoji" to object : NativeFunkcija(name = "spoji"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                val newArray = ArrayList<RuntimeValue>(value)

                args.forEach {
                    if(it !is Niz){
                        throw Exception("Type Error: Function 'spoji' expects 1 or more Arrays as arguments")
                    }
                    newArray += it.value
                }

                return Niz(
                    value = newArray
                )
            }
        },

        "poravnaj" to object : NativeFunkcija(name = "poravnaj"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.isNotEmpty()) {
                    throw Exception("Type Error: Function 'poravnaj' accepts 0 arguments")
                }
                return Niz(
                    value = value.flatten()
                )
            }
        },

        "izbaci" to object : NativeFunkcija(name = "izbaci"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.isNotEmpty()) {
                    throw Exception("Type Error: Function 'izbaci' accepts 0 arguments")
                }
                if (value.isEmpty()) {
                    throw Exception("Array is empty!")
                }
                return value.removeLast()
            }
        },

        "izbaciPrvi" to object : NativeFunkcija(name = "izbaciPrvi"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if (args.isNotEmpty()) {
                    throw Exception("Type Error: Function 'izbaciPrvi' accepts 0 arguments")
                }
                if (value.isEmpty()) {
                    throw Exception("Array is empty!")
                }
                return value.removeFirst()
            }
        },

        "sortiraj" to object : NativeFunkcija(name = "sortiraj"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                value.sortBy { it.value as Comparable<Any> }
                return Null()
            }
        },

        "sortirajSilazno" to object : NativeFunkcija(name = "sortirajSilazno"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                value.sortByDescending { it.value as Comparable<Any> }
                return Null()
            }
        },

        "sortirajPo" to object : NativeFunkcija(name = "sortirajPo"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 && args[0] !is Tekst){
                    throw Exception("Type Error: Function 'sortirajPo' accepts 1 argument (key: Tekst)")
                }
                if(value.any { it !is Objekat }){
                    throw Exception("Type Error: Cannot call 'sortirajPo' on primitive values")
                }

                value as ArrayList<Objekat>
                val prop = (args[0] as Tekst).value

                value.sortBy { it.getProperty(prop).value as Comparable<Any> }

                return Null()
            }
        },

        "sortirajSilaznoPo" to object : NativeFunkcija(name = "sortirajSilaznoPo"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 && args[0] !is Tekst){
                    throw Exception("Type Error: Function 'sortirajSilaznoPo' accepts 1 argument (key: Tekst)")
                }
                if(value.any { it !is Objekat }){
                    throw Exception("Type Error: Cannot call 'sortirajSilaznoPo' on primitive values")
                }

                value as ArrayList<Objekat>
                val prop = (args[0] as Tekst).value

                value.sortByDescending { it.getProperty(prop).value as Comparable<Any> }

                return Null()
            }
        },

        "isijeci" to object : NativeFunkcija(name = "isijeci"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size == 1){
                    if(args[0] !is Broj){
                        throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
                    }
                    val startIndex = (args[0] as Broj).value
                    if(!startIndex.isInt()){
                        throw Exception("Error: Start Index must be an integer")
                    }
                    return Niz(
                        value = value.slice(startIndex.toInt() until value.size) as ArrayList<RuntimeValue>
                    )
                }

                if(args.size == 2){
                    if(args[0] !is Broj || args[1] !is Broj){
                        throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
                    }

                    val startIndex = (args[0] as Broj).value
                    val endIndex = (args[1] as Broj).value
                    if(!startIndex.isInt()){
                        throw Exception("Error: Start Index must be an integer")
                    }
                    if(!endIndex.isInt()){
                        throw Exception("Error: End Index must be an integer")
                    }
                    return Niz(
                        value = value.slice(startIndex.toInt() until endIndex.toInt()) as ArrayList<RuntimeValue>
                    )
                }

                throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
            }
        },

        "primijeni" to ContextualNativeFunction(
            name = "primijeni",
            call = { args, interpreterInstance ->
                if(args[0] is NativeFunkcija){
                    if(args.size != 1){
                        throw Exception("Function 'primijeni' accepts 1 argument (fun: Funkcija)")
                    }
                    val fn = args[0] as NativeFunkcija
                    val newValues = arrayListOf<RuntimeValue>()
                    value.forEach {
                        newValues.add(fn.call(it))
                    }
                    return@ContextualNativeFunction Niz(
                        value = newValues
                    )
                }
                if(args[0] is Funkcija){
                    val fn = args[0] as Funkcija
                    val newArray = arrayListOf<RuntimeValue>()
                    value.forEach {
                        val activationRecord = hashMapOf<String, RuntimeValue>()
                        fn.params.forEach{param ->
                            activationRecord[param.identifier.symbol] = it
                        }

                        val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                        val functionResult = interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)

                        if(functionResult is ReturnValue) {
                            newArray.add(functionResult.value)
                        }
                        else {
                            newArray.add(functionResult)
                        }
                    }
                    return@ContextualNativeFunction Niz(
                        value = newArray
                    )
                }

                throw Exception("Type Error")
            },
            value = null
        ),

        "zaSvaki" to ContextualNativeFunction(
            name = "zaSvaki",
            call = { args, interpreterInstance ->
                if(args[0] is NativeFunkcija){
                    if(args.size != 1){
                        throw Exception("Function 'primijeni' accepts 1 argument (fun: Funkcija)")
                    }
                    val fn = args[0] as NativeFunkcija
                    value.forEach {
                       fn.call(it)
                    }
                    return@ContextualNativeFunction Null()
                }
                if(args[0] is Funkcija){
                    val fn = args[0] as Funkcija
                    value.forEach {
                        val activationRecord = hashMapOf<String, RuntimeValue>()
                        fn.params.forEach{param ->
                            activationRecord[param.identifier.symbol] = it
                        }

                        val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                        interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
                    }
                    return@ContextualNativeFunction Null()
                }

                throw Exception("Type Error")
            },
            value = null
        ),

        "sortirajSa" to ContextualNativeFunction(
            name = "sortirajSa",
            call = { args, interpreterInstance ->
                if(args.size != 1 || args[0] !is Funkcija){
                    throw Exception("Type Error: Function 'sortirajSa' accepts 1 argument (sortingFunction: Funkcija)")
                }
                val fn = args[0] as Funkcija
                if(fn.params.size != 2){
                    throw Exception("Type Error: Comparator function must accept 2 arguments (item1: nepoznato, item2: nepoznato)")
                }

                for (i in 0 until value.size - 1){
                    for (j in i + 1 until value.size){
                        val activationRecord = hashMapOf<String, RuntimeValue>()
                        activationRecord[fn.params[0].identifier.symbol] = value[i]
                        activationRecord[fn.params[1].identifier.symbol] = value[j]
                        val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                        val result = (interpreterInstance.evaluateBlockStatement(fn.body, functionEnv) as ReturnValue).value
                        if(result !is Broj){
                            throw Exception("Type Error: Comparator function must return Broj, got $result")
                        }
                        if(result.value > 0){
                            val temp = value[i]
                            value[i] = value[j]
                            value[j] = temp
                        }
                    }
                }
                Null()
            },
            value = null
        )
    ),
    override val typename: String = "niz"
) : RuntimeValue {
    override fun toString(): String {
        return value.toString()
    }

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Array")
    }

    fun getElement(index: Int): RuntimeValue {
        return value[index]
    }

    fun set(index: Int, newValue: RuntimeValue){
        value[index] = newValue
    }
}