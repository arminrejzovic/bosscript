package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.NativeFunction

val collections = Environment(variables = hashMapOf(
    "Skup" to NativeFunction("Skup"){
        SetFactory.construct()
    },
    "Red" to NativeFunction("Red"){
        QueueFactory.construct()
    },
    "Stog" to NativeFunction("Stog"){
        StackFactory.construct()
    },
    "VezanaLista" to NativeFunction("VezanaLista"){
        LinkedListFactory.construct()
    },
    "Mapa" to NativeFunction("Mapa"){
        MapFactory.construct()
    }
))