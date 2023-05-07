package interpreter.packages.regex

import interpreter.values.*

class RegexObjectFactory {
    companion object{
        fun constructRegexObject(regexp: String): ReadonlyObject{
            val regex = Regex(regexp)
            return ReadonlyObject(properties = hashMapOf(
                "sablon" to Tekst(regex.pattern),
                "pronadji" to NativeFunction("pronadji"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Argument mismatch")
                    }
                    val str = (args[0] as Tekst).value
                    val match = regex.find(str)
                    if(match != null){
                        return@NativeFunction constructMatchObject(match)
                    }
                    else {
                        return@NativeFunction Null()
                    }
                },
                "pronadjiSve" to NativeFunction("pronadjiSve"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Argument mismatch")
                    }
                    val str = (args[0] as Tekst).value
                    val match = regex.findAll(str)
                    val results = arrayListOf<RuntimeValue>()
                    match.forEach {
                        results.add(constructMatchObject(it))
                    }
                    Niz(results)
                },
                "zamijeni" to NativeFunction("zamijeni"){args ->
                    if(args.size != 2 || args[0] !is Tekst || args[1] !is Tekst){
                        throw Exception("Argument mismatch")
                    }
                    val str = (args[0] as Tekst).value
                    val replacement = (args[1] as Tekst).value
                    val newStr = regex.replace(str, replacement)
                    Tekst(newStr)
                },

                "potpunoPoklapanje" to NativeFunction("potpunoPoklapanje"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Argument mismatch")
                    }
                    val str = (args[0] as Tekst).value
                    val match = regex.matchEntire(str)

                    if(match != null){
                        return@NativeFunction constructMatchObject(match)
                    }

                    Null()
                },
            ))
        }

        private fun constructMatchObject(matchResult: MatchResult): Objekat{
            return Objekat(hashMapOf(
                "vrijednost" to Tekst(matchResult.value),
                "lokacija" to Objekat(hashMapOf(
                    "pocetak" to Broj(matchResult.range.first.toDouble()),
                    "kraj" to Broj(matchResult.range.last.toDouble())
                ))
            ))
        }
    }
}