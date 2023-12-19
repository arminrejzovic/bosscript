package interpreter.packages.regex

import interpreter.Environment
import interpreter.values.NativeFunction
import interpreter.values.Tekst

val regex = Environment(variables = hashMapOf(
    "Regex" to NativeFunction("Regex"){args ->
        if(args.size != 1 || args[0] !is Tekst){
            throw Exception("Funkcija 'Regex' prihvata 1 argument (šablon: tekst) (pronađeno ${args.size})")
        }
        val pattern = (args[0] as Tekst).value
        RegexObjectFactory.constructRegexObject(pattern)
    }
))