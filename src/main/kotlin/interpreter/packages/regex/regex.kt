package interpreter.packages.regex

import interpreter.Environment
import interpreter.values.*

val regex = Environment(variables = hashMapOf(
    "Regex" to NativeFunction("Regex"){args ->
        if(args.size != 1 || args[0] !is Tekst){
            throw Exception("Argument mismatch")
        }
        val pattern = (args[0] as Tekst).value
        RegexObjectFactory.constructRegexObject(pattern)
    }
))