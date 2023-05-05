package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*
import java.util.ArrayList
import java.util.LinkedList

class LinkedListFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val list = LinkedList<RuntimeValue>()
            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(list.size.toDouble())
                },
                "jePrazan" to NativeFunction("jePrazan"){
                    Logicki(list.isEmpty())
                },
                "prvi" to NativeFunction("prvi"){
                    list.first
                },
                "zadnji" to NativeFunction("zadnji"){
                    list.last
                },
                "indeksi" to NativeFunction("indeksi"){
                    val indices = list.indices
                    val arr = ArrayList<RuntimeValue>(indices.map { i -> Broj(i.toDouble()) })
                    Niz(arr)
                },
                "dodaj" to NativeFunction("dodaj"){ args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    val ok = list.add(args[0])
                    Logicki(ok)
                },
                "dobavi" to NativeFunction("dobavi"){ args ->
                    if(args.size != 1 || args[0] !is Broj){
                        throw Exception("Argument mismatch")
                    }
                    val index = (args[0] as Broj).value.toInt()
                    list[index]
                },
                "dodajSve" to NativeFunction("dodaj"){ args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    val ok = list.addAll(arr.value)
                    Logicki(ok)
                },
                "izbaci" to NativeFunction("izbaci"){
                    list.remove()
                },
                "izbaciZadnji" to NativeFunction("izbaciZadnji"){
                    list.removeLast()
                },
                "izbaciPrvi" to NativeFunction("izbaciPrvi"){
                    list.removeFirst()
                },
                "isprazni" to NativeFunction("isprazni"){
                    list.clear()
                    Null()
                },
                "proviri" to NativeFunction("proviri"){
                    list.peek()
                },
                "proviriNaPocetak" to NativeFunction("proviriNaPocetak"){
                    list.peekFirst()
                },
                "proviriNaKraj" to NativeFunction("proviriNaKraj"){
                    list.peekLast()
                },
                "kaoNiz" to NativeFunction("kaoNiz"){
                    Niz(ArrayList(list.map{it}))
                },
                "sadrzi" to NativeFunction("sadrzi"){ args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    Logicki(list.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    Logicki(list.containsAll(arr.value))
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'zaSvaki' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        list.forEach {
                            fn.call(listOf(it))
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        list.forEach {
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
                        list.forEach {
                            newValues.add(fn.call(listOf(it)))
                        }
                        Niz(
                            value = newValues
                        )
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        val newArray = arrayListOf<RuntimeValue>()
                        list.forEach {
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