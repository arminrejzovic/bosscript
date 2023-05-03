package interpreter.packages

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.ContextualNativeFunction
import interpreter.values.Funkcija
import interpreter.values.NativeFunction
import kotlin.system.measureTimeMillis

val chrono = Environment(variables = hashMapOf(
    "tempiraj" to ContextualNativeFunction("tempiraj"){ args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Function 'tempiraj' accepts 1 argument (fun: Funkcija)")
            }
            val fn = args[0] as NativeFunction

            val executionTime = measureTimeMillis {
                fn.call(listOf())
            }

            return@ContextualNativeFunction Broj(executionTime.toDouble())
        }
        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija
            val executionTime = measureTimeMillis {
                val functionEnv = Environment(parent = fn.parentEnv, variables = hashMapOf())
                interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
            }
            return@ContextualNativeFunction Broj(executionTime.toDouble())
        }
        else throw Exception("Type Error")
    }
))