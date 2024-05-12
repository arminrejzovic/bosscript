package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*
import java.util.*

class QueueFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val queue = ArrayDeque<RuntimeValue>()

            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(queue.size.toDouble())
                },
                "veličina" to NativeFunction("veličina"){
                    Broj(queue.size.toDouble())
                },
                "jePrazan" to NativeFunction("jePrazan"){
                    Logicki(queue.isEmpty())
                },
                "prvi" to NativeFunction("prvi"){
                    queue.first
                },
                "zadnji" to NativeFunction("zadnji"){
                    queue.last
                },
                "indeksi" to NativeFunction("indeksi"){
                    val indices = queue.indices
                    val arr = ArrayList<RuntimeValue>(indices.map { i -> Broj(i.toDouble()) })
                    Niz(arr)
                },
                "dodaj" to NativeFunction("dodaj"){ args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'dodaj' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    val ok = queue.add(args[0])
                    Logicki(ok)
                },
                "dodajSve" to NativeFunction("dodaj"){ args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'dodajSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    val ok = queue.addAll(arr.value)
                    Logicki(ok)
                },
                "izbaci" to NativeFunction("izbaci"){
                    queue.remove()
                },
                "izbaciZadnji" to NativeFunction("izbaciZadnji"){
                    queue.removeLast()
                },
                "izbaciPrvi" to NativeFunction("izbaciPrvi"){
                    queue.removeFirst()
                },
                "isprazni" to NativeFunction("isprazni"){
                    queue.clear()
                    Null()
                },
                "proviri" to NativeFunction("proviri"){
                    queue.peek()
                },
                "proviriNaPocetak" to NativeFunction("proviriNaPocetak"){
                    queue.peekFirst()
                },
                "proviriNaKraj" to NativeFunction("proviriNaKraj"){
                    queue.peekLast()
                },
                "kaoNiz" to NativeFunction("kaoNiz"){
                    Niz(ArrayList(queue.map{it}))
                },
                "sadrzi" to NativeFunction("sadrzi"){ args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'sadrzi' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(queue.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadrziSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(queue.containsAll(arr.value))
                },
                "sadrži" to NativeFunction("sadrži"){ args ->
                    if(args.size != 1){
                        throw Exception("Funkcija 'sadrži' prihvata 1 argument (vrijednost: objekat) (pronađeno ${args.size})")
                    }
                    Logicki(queue.contains(args[0]))
                },
                "sadržiSve" to NativeFunction("sadržiSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Funkcija 'sadržiSve' prihvata 1 argument (vrijednosti: objekat[]) (pronađeno ${args.size})")
                    }
                    val arr = args[0] as Niz
                    Logicki(queue.containsAll(arr.value))
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Funkcija 'zaSvaki' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
                        }
                        val fn = args[0] as NativeFunction
                        queue.forEach {
                            fn.call(listOf(it))
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        queue.forEach {
                            val activationRecord = hashMapOf<String, RuntimeValue>()
                            fn.params.forEach { param ->
                                activationRecord[param.identifier] = it
                            }

                            val functionEnv = Environment(parent = fn.parentEnv, variables = activationRecord)
                            interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else throw Exception("Funkcija 'zaSvaki' prihvata 1 argument (fun: Funkcija)")
                },
                "primijeni" to ContextualNativeFunction("primijeni"){ args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Funkcija 'primijeni' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
                        }
                        val fn = args[0] as NativeFunction
                        val newValues = arrayListOf<RuntimeValue>()
                        queue.forEach {
                            newValues.add(fn.call(listOf(it)))
                        }
                        Niz(
                            value = newValues
                        )
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        val newArray = arrayListOf<RuntimeValue>()
                        queue.forEach {
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