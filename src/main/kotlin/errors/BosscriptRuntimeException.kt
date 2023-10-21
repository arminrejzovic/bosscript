package errors

import interpreter.values.Objekat
import interpreter.values.Tekst

class BosscriptRuntimeException(
    val exceptionObject: Objekat = Objekat(hashMapOf()),
    var text: String,
    var location: Pair<Int, Int>
) : Exception("[${location.first}:${location.second}] $text\n") {
    init {
        if(!exceptionObject.properties.containsKey("poruka")){
            exceptionObject.properties["poruka"] = Tekst(text)
        }
    }
}