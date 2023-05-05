package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*

class MapFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val map = HashMap<RuntimeValue, RuntimeValue>()
            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(map.size)
                },
                "kljucevi" to NativeFunction("kljucevi"){
                    Niz(ArrayList(map.keys))
                },
                "vrijednosti" to NativeFunction("vrijednosti"){
                    Niz(ArrayList(map.values))
                },
                "dobavi" to NativeFunction("dobavi"){args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    val key = args[0]
                    map[key] ?: Null()
                },
                "postavi" to NativeFunction("postavi"){args ->
                    if(args.size != 2){
                        throw Exception("Argument mismatch")
                    }
                    val key = args[0]
                    val value = args[1]
                    map[key] = value
                    Null()
                },
                "postaviAkoNePostoji" to NativeFunction("postaviAkoNePostoji"){args ->
                    if(args.size != 2){
                        throw Exception("Argument mismatch")
                    }
                    val key = args[0]
                    val value = args[1]
                    map.putIfAbsent(key, value) ?: Null()
                },
                "sadrziKljuc" to NativeFunction("sadrziKljuc"){args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    val key = args[0]
                    Logicki(map.containsKey(key))
                },
                "parovi" to NativeFunction("parovi"){
                    val entries = map.entries.map { e -> ReadonlyObject(hashMapOf(
                        "kljuc" to e.key,
                        "vrijednost" to e.value
                    )) }
                    Niz(ArrayList(entries))
                },
                "isprazni" to NativeFunction("isprazni"){
                    map.clear()
                    Null()
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'zaSvaki' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        map.forEach {
                            fn.call(listOf(it.key, it.value))
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        map.forEach {
                            val activationRecord = hashMapOf<String, RuntimeValue>()
                            activationRecord[fn.params[0].identifier.symbol] = it.key
                            activationRecord[fn.params[1].identifier.symbol] = it.value

                            val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                            interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else throw Exception("Type Error")
                },
                "primijeni" to ContextualNativeFunction("primijeni"){ args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'primijeni' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        val newValues = arrayListOf<RuntimeValue>()
                        map.forEach {
                            newValues.add(fn.call(listOf(it.key, it.value)))
                        }
                        Niz(
                            value = newValues
                        )
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        val newArray = arrayListOf<RuntimeValue>()
                        map.forEach {
                            val activationRecord = hashMapOf<String, RuntimeValue>()
                            activationRecord[fn.params[0].identifier.symbol] = it.key
                            activationRecord[fn.params[1].identifier.symbol] = it.value

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
            ))
        }
    }
}