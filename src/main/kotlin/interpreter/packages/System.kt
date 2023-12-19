package interpreter.packages

import interpreter.Environment
import interpreter.values.Broj
import interpreter.values.ContextualNativeFunction
import interpreter.values.Funkcija
import interpreter.values.NativeFunction
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

val system = Environment(variables = hashMapOf(
    "tempiraj" to ContextualNativeFunction("tempiraj"){ args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Funkcija 'tempiraj' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
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
        else throw Exception("Funkcija 'tempiraj' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
    },

    "tempirajNano" to ContextualNativeFunction("tempirajNano"){ args, interpreterInstance ->
        if (args[0] is NativeFunction) {
            if (args.size != 1) {
                throw Exception("Funkcija 'tempirajNano' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
            }
            val fn = args[0] as NativeFunction

            val executionTime = measureNanoTime {
                fn.call(listOf())
            }

            return@ContextualNativeFunction Broj(executionTime.toDouble())
        }
        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija
            val executionTime = measureNanoTime {
                val functionEnv = Environment(parent = fn.parentEnv, variables = hashMapOf())
                interpreterInstance.evaluateBlockStatement(fn.body, functionEnv)
            }
            return@ContextualNativeFunction Broj(executionTime.toDouble())
        }
        else throw Exception("Funkcija 'tempirajNano' prihvata 1 argument (fun: Funkcija) (pronađeno ${args.size})")
    },

    "zatvoriProces" to NativeFunction("zatvoriProces"){args ->
        if (args.size != 1 || args[0] !is Broj){
            throw Exception("Funkcija 'zatvoriProces' prihvata 1 argument (status: broj) (pronađeno ${args.size})")
        }
        val status = (args[0] as Broj).value.toInt()
        exitProcess(status)
    },
))