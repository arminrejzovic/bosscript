package interpreter.packages.regex

import interpreter.values.*

class RegexObjectFactory {
    companion object{
        fun constructRegexObject(regexp: String): ReadonlyObject{
            val regex = Regex(regexp)
            return ReadonlyObject(properties = hashMapOf(
                "sablon" to Tekst(regex.pattern),
                "šablon" to Tekst(regex.pattern),
                "pronadji" to NativeFunction("pronadji"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Funkcija 'pronadji' prihvata 1 argument (tekst: tekst) (pronađeno ${args.size})")
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
                        throw Exception("Funkcija 'pronadjiSve' prihvata 1 argument (tekst: tekst) (pronađeno ${args.size})")
                    }
                    val str = (args[0] as Tekst).value
                    val match = regex.findAll(str)
                    val results = arrayListOf<RuntimeValue>()
                    match.forEach {
                        results.add(constructMatchObject(it))
                    }
                    Niz(results)
                },
                "pronađi" to NativeFunction("pronađi"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Funkcija 'pronađi' prihvata 1 argument (tekst: tekst) (pronađeno ${args.size})")
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
                "pronađiSve" to NativeFunction("pronađiSve"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Funkcija 'pronađiSve' prihvata 1 argument (tekst: tekst) (pronađeno ${args.size})")
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
                        throw Exception("Funckija 'zamijeni' prihvata 2 argumenta (tekst: tekst, zamjena: tekst) (pronađeno ${args.size})")
                    }
                    val str = (args[0] as Tekst).value
                    val replacement = (args[1] as Tekst).value
                    val newStr = regex.replace(str, replacement)
                    Tekst(newStr)
                },

                "potpunoPoklapanje" to NativeFunction("potpunoPoklapanje"){args ->
                    if(args.size != 1 || args[0] !is Tekst){
                        throw Exception("Funkcija 'potpunoPoklapanje' prihvata 1 argument (tekst: tekst) (pronađeno ${args.size})")
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
                    "početak" to Broj(matchResult.range.first.toDouble()),
                    "kraj" to Broj(matchResult.range.last.toDouble())
                ))
            ))
        }
    }
}