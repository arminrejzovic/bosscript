package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*
import kotlin.collections.HashSet
import kotlin.collections.ArrayList

class SetFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val set = HashSet<RuntimeValue>()

            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(set.size.toDouble())
                },
                "isprazni" to NativeFunction("isprazni"){
                    set.clear()
                    Null()
                },
                "jePrazan" to NativeFunction("jePrazan"){
                    Logicki(set.isEmpty())
                },
                "sadrzi" to NativeFunction("sadrzi"){ args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    Logicki(set.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    Logicki(set.containsAll(arr.value))
                },
                "dodaj" to NativeFunction("dodaj"){args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    val ok = set.add(args[0])
                    Logicki(ok)
                },
                "dodajSve" to NativeFunction("dodajSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    val ok = set.addAll(arr.value)
                    Logicki(ok)
                },
                "izbaci" to NativeFunction("izbaci"){args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    val ok = set.remove(args[0])
                    Logicki(ok)
                },
                "izbaciSve" to NativeFunction("izbaciSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    val ok = set.removeAll(arr.value.toSet())
                    Logicki(ok)
                },
                "kaoNiz" to NativeFunction("kaoNiz"){
                    Niz(ArrayList(set.map{it}))
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'zaSvaki' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        set.forEach {
                            fn.call(listOf(it))
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        set.forEach {
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
                },
                "primijeni" to ContextualNativeFunction("primijeni"){ args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'primijeni' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        val newValues = arrayListOf<RuntimeValue>()
                        set.forEach {
                            newValues.add(fn.call(listOf(it)))
                        }
                        Niz(
                            value = newValues
                        )
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        val newArray = arrayListOf<RuntimeValue>()
                        set.forEach {
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
            ))
        }
    }
}