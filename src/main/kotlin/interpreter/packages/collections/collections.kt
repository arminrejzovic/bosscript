package interpreter.packages.collections

import interpreter.Environment
import interpreter.values.NativeFunction

val collections = Environment(variables = hashMapOf(
    "Set" to NativeFunction("Set"){
        SetFactory.construct()
    }
))