package interpreter.values

import flatten
import interpreter.Environment
import isInt

data class Niz(
    override val value: ArrayList<RuntimeValue>,
) : RuntimeValue {
    override fun toString(): String {
        return value.toString()
    }

    private val duzina = NativeFunction("duzina"){
        Broj(value = value.size.toDouble())
    }

    private val dodaj = NativeFunction("dodaj"){ args ->
        if (args.size != 1) {
            throw Exception("Type Error: Function 'dodaj' accepts 1 argument (item: nepoznato)")
        }
        value.add(args[0])
        Null()
    }

    private val dodajNaPocetak = NativeFunction("dodajNaPocetak"){ args ->
        if (args.size != 1) {
            throw Exception("Type Error: Function 'dodajNaPocetak' accepts 1 argument (item: nepoznato)")
        }
        value.add(0, args[0])
        Broj(
            value = value.size.toDouble()
        )
    }

    private val spoji = NativeFunction("spoji"){ args ->
        val newArray = ArrayList<RuntimeValue>(value)

        args.forEach {
            if (it !is Niz) {
                throw Exception("Type Error: Function 'spoji' expects 1 or more Arrays as arguments")
            }
            newArray += it.value
        }

        Niz(
            value = newArray
        )
    }

    private val poravnaj = NativeFunction("poravnaj"){args ->
        if (args.isNotEmpty()) {
            throw Exception("Type Error: Function 'poravnaj' accepts 0 arguments")
        }
        Niz(
            value = value.flatten()
        )
    }

    private val izbaci = NativeFunction("izbaci"){args ->
        if (args.isNotEmpty()) {
            throw Exception("Type Error: Function 'izbaci' accepts 0 arguments")
        }
        if (value.isEmpty()) {
            throw Exception("Array is empty!")
        }
        value.removeLast()
    }

    private val izbaciPrvi = NativeFunction("izbaciPrvi"){ args ->
        if (args.isNotEmpty()) {
            throw Exception("Type Error: Function 'izbaciPrvi' accepts 0 arguments")
        }
        if (value.isEmpty()) {
            throw Exception("Array is empty!")
        }
        value.removeFirst()
    }

    private val sortiraj = NativeFunction("sortiraj"){
        value.sortBy { it.value as Comparable<Any> }
        Null()
    }

    private val sortirajSilazno = NativeFunction("sortirajSilazno"){
        value.sortByDescending { it.value as Comparable<Any> }
        Null()
    }

    private val sortirajPo = NativeFunction("sortirajPo"){ args ->
        if (args.size != 1 && args[0] !is Tekst) {
            throw Exception("Type Error: Function 'sortirajPo' accepts 1 argument (key: Tekst)")
        }
        if (value.any { it !is Objekat }) {
            throw Exception("Type Error: Cannot call 'sortirajPo' on primitive values")
        }

        value as ArrayList<Objekat>
        val prop = (args[0] as Tekst).value

        value.sortBy { it.getProperty(prop).value as Comparable<Any> }

        Null()
    }

    private val sortirajSilaznoPo = NativeFunction("sortirajSilaznoPo"){ args ->
        if (args.size != 1 && args[0] !is Tekst) {
            throw Exception("Type Error: Function 'sortirajSilaznoPo' accepts 1 argument (key: Tekst)")
        }
        if (value.any { it !is Objekat }) {
            throw Exception("Type Error: Cannot call 'sortirajSilaznoPo' on primitive values")
        }

        value as ArrayList<Objekat>
        val prop = (args[0] as Tekst).value

        value.sortByDescending { it.getProperty(prop).value as Comparable<Any> }

        Null()
    }

    private val isijeci = NativeFunction("isijeci"){ args ->
        if (args.size == 1) {
            if (args[0] !is Broj) {
                throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
            }
            val startIndex = (args[0] as Broj).value
            if (!startIndex.isInt()) {
                throw Exception("Error: Start Index must be an integer")
            }
            return@NativeFunction Niz(
                value = value.slice(startIndex.toInt() until value.size) as ArrayList<RuntimeValue>
            )
        }

        if (args.size == 2) {
            if (args[0] !is Broj || args[1] !is Broj) {
                throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
            }

            val startIndex = (args[0] as Broj).value
            val endIndex = (args[1] as Broj).value
            if (!startIndex.isInt()) {
                throw Exception("Error: Start Index must be an integer")
            }
            if (!endIndex.isInt()) {
                throw Exception("Error: End Index must be an integer")
            }
            return@NativeFunction Niz(
                value = value.slice(startIndex.toInt() until endIndex.toInt()) as ArrayList<RuntimeValue>
            )
        }

        throw Exception("Type Error: Function 'isijeci' accepts 2 arguments (start: Broj, end: Broj)")
    }

    private val primijeni = ContextualNativeFunction("primijeni"){ args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Function 'primijeni' accepts 1 argument (fun: Funkcija)")
            }
            val fn = args[0] as NativeFunction
            val newValues = arrayListOf<RuntimeValue>()
            value.forEach {
                newValues.add(fn.call(listOf(it)))
            }
            Niz(
                value = newValues
            )
        }
        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija
            val newArray = arrayListOf<RuntimeValue>()
            value.forEach {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                fn.params.forEach { param ->
                    activationRecord[param.identifier.symbol] = it
                }

                val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                val functionResult = interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)

                if (functionResult is ReturnValue) {
                    newArray.add(functionResult.value)
                } else {
                    newArray.add(functionResult)
                }
            }
            Niz(
                value = newArray
            )
        }
        else throw Exception("Type Error $args")
    }

    private val zaSvaki = ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Function 'zaSvaki' accepts 1 argument (fun: Funkcija)")
            }
            val fn = args[0] as NativeFunction
            value.forEach {
                fn.call(listOf(it))
            }
            return@ContextualNativeFunction Null()
        }
        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija
            value.forEach {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                fn.params.forEach { param ->
                    activationRecord[param.identifier.symbol] = it
                }

                val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
            }
            return@ContextualNativeFunction Null()
        }
        else throw Exception("Type Error")
    }

    private val zaSvakiUnazad = ContextualNativeFunction("zaSvakiUnazad"){ args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Function 'zaSvakiUnazad' accepts 1 argument (fun: Funkcija)")
            }
            val fn = args[0] as NativeFunction
            value.reversed().forEach {
                fn.call(listOf(it))
            }
            return@ContextualNativeFunction Null()
        }
        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija
            value.reversed().forEach {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                fn.params.forEach { param ->
                    activationRecord[param.identifier.symbol] = it
                }

                val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
            }
            return@ContextualNativeFunction Null()
        }
        else throw Exception("Type Error")
    }

    private val sortirajSa = ContextualNativeFunction("sortirajSa"){ args, interpreterInstance ->
        if (args.size != 1 || args[0] !is Funkcija) {
            throw Exception("Type Error: Function 'sortirajSa' accepts 1 argument (sortingFunction: Funkcija)")
        }
        val fn = args[0] as Funkcija
        if (fn.params.size != 2) {
            throw Exception("Type Error: Comparator function must accept 2 arguments (item1: nepoznato, item2: nepoznato)")
        }

        for (i in 0 until value.size - 1) {
            for (j in i + 1 until value.size) {
                val activationRecord = hashMapOf<String, RuntimeValue>()
                activationRecord[fn.params[0].identifier.symbol] = value[i]
                activationRecord[fn.params[1].identifier.symbol] = value[j]
                val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                val result =
                    (interpreterInstance.evaluateBlockStatement(fn.body, functionEnv) as ReturnValue).value
                if (result !is Broj) {
                    throw Exception("Type Error: Comparator function must return Broj, got $result")
                }
                if (result.value > 0) {
                    val temp = value[i]
                    value[i] = value[j]
                    value[j] = temp
                }
            }
        }
        return@ContextualNativeFunction Null()
    }

    private val kraj = NativeFunction("kraj"){
        return@NativeFunction Broj((value.size - 1).toDouble())
    }

    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf(
            "duzina" to duzina,
            "dodaj" to dodaj,
            "dodajNaPocetak" to dodajNaPocetak,
            "spoji" to spoji,
            "poravnaj" to poravnaj,
            "izbaci" to izbaci,
            "izbaciPrvi" to izbaciPrvi,
            "sortiraj" to sortiraj,
            "sortirajSilazno" to sortirajSilazno,
            "sortirajPo" to sortirajPo,
            "sortirajSilaznoPo" to sortirajSilaznoPo,
            "isijeci" to isijeci,
            "primijeni" to primijeni,
            "zaSvaki" to zaSvaki,
            "zaSvakiUnazad" to zaSvakiUnazad,
            "sortirajSa" to sortirajSa,
            "kraj" to kraj
        )

    override val typename: String
        get() = "niz"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("$prop does not exist on type Array")
    }

    fun getElement(index: Int): RuntimeValue {
        return value[index]
    }

    fun set(index: Int, newValue: RuntimeValue) {
        value[index] = newValue
    }
}