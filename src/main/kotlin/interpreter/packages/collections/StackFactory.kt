package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.*
import java.util.Stack

class StackFactory {
    companion object{
        fun construct(): ReadonlyObject{
            val stack = Stack<RuntimeValue>()
            return ReadonlyObject(hashMapOf(
                "velicina" to NativeFunction("velicina"){
                    Broj(stack.size.toDouble())
                },
                "isprazni" to NativeFunction("isprazni"){
                    stack.clear()
                    Null()
                },
                "jePrazan" to NativeFunction("jePrazan"){
                    Logicki(stack.isEmpty())
                },
                "sadrzi" to NativeFunction("sadrzi"){ args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    Logicki(stack.contains(args[0]))
                },
                "sadrziSve" to NativeFunction("sadrziSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    Logicki(stack.containsAll(arr.value))
                },
                "dodaj" to NativeFunction("dodaj"){args ->
                    if(args.size != 1){
                        throw Exception("Argument mismatch")
                    }
                    stack.push(args[0])
                },
                "dodajSve" to NativeFunction("dodajSve"){args ->
                    if(args.size != 1 || args[0] !is Niz){
                        throw Exception("Argument mismatch")
                    }
                    val arr = args[0] as Niz
                    val ok = stack.addAll(arr.value)
                    Logicki(ok)
                },
                "izbaci" to NativeFunction("izbaci"){
                    stack.pop()
                },
                "kaoNiz" to NativeFunction("kaoNiz"){
                    Niz(ArrayList(stack.map{it}))
                },
                "proviri" to NativeFunction("proviri"){
                    stack.peek()
                },
                "zaSvaki" to ContextualNativeFunction("zaSvaki") { args, interpreterInstance ->
                    if (args[0] is NativeFunction) {
                        if (args.size != 1) {
                            throw Exception("Function 'zaSvaki' accepts 1 argument (fun: Funkcija)")
                        }
                        val fn = args[0] as NativeFunction
                        stack.forEach {
                            fn.call(listOf(it))
                        }
                        return@ContextualNativeFunction Null()
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        stack.forEach {
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
                        stack.forEach {
                            newValues.add(fn.call(listOf(it)))
                        }
                        Niz(
                            value = newValues
                        )
                    }
                    else if (args[0] is Funkcija) {
                        val fn = args[0] as Funkcija
                        val newArray = arrayListOf<RuntimeValue>()
                        stack.forEach {
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