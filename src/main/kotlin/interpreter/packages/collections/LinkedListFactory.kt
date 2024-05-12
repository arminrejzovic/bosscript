package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*
import java.util.*

class LinkedListFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val list = LinkedList<RuntimeValue>()
            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(list.size.toDouble())
                },
                "veličina" to NativeFunction("veličina"){
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
                        throw Exception("Funkcija 'dodaj' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    val ok = list.add(args[0])
                    Logicki(ok)
                },
                "dobavi" to NativeFunction("dobavi"){ args ->
                    if(args.size != 1 || args[0] !is Broj){
                        throw Exception("Funkcija 'dobavi' prihvata 1 argument (indeks: broj) (pronađeno ${args.size})")
                    }
                    val index = (args[0] as Broj).value.toInt()
                    list[index]
                },
                "dodajSve" to NativeFunction("dodaj"){ args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'dodajSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
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
                "proviriNaPočetak" to NativeFunction("proviriNaPočetak"){
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
                        throw Exception("Funkcija 'sadrzi' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(list.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadrziSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(list.containsAll(arr.value))
                },
                "sadrži" to NativeFunction("sadrži"){ args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'sadrži' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(list.contains(args[0]))
                },
                "sadržiSve" to NativeFunction("sadržiSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadržiSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(list.containsAll(arr.value))
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Funkcija 'zaSvaki' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
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
                                activationRecord[param.identifier] = it
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
                                activationRecord[param.identifier] = it
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