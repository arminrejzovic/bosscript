package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*

class SetFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val set = HashSet<RuntimeValue>()

            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(set.size.toDouble())
                },
                "veličina" to NativeFunction("veličina"){
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
                        throw Exception("Funkcija 'sadrzi' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(set.contains(args[0]))
                },
                "sadrži" to NativeFunction("sadrži"){ args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'sadrži' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(set.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadrziSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(set.containsAll(arr.value))
                },
                "sadržiSve" to NativeFunction("sadržiSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadržiSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(set.containsAll(arr.value))
                },
                "dodaj" to NativeFunction("dodaj"){args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'dodaj' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    val ok = set.add(args[0])
                    Logicki(ok)
                },
                "dodajSve" to NativeFunction("dodajSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'dodajSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    val ok = set.addAll(arr.value)
                    Logicki(ok)
                },
                "izbaci" to NativeFunction("izbaci"){args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'izbaci' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    val ok = set.remove(args[0])
                    Logicki(ok)
                },
                "izbaciSve" to NativeFunction("izbaciSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'izbaciSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
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
                            throw Exception("Funkcija 'zaSvaki' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
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
                    else throw Exception("Funkcija 'zaSvaki' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
                },
                "primijeni" to ContextualNativeFunction("primijeni"){ args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Funkcija 'primijeni' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
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
                    else throw Exception("Funkcija 'primijeni' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
                }
            ))
        }
    }
}