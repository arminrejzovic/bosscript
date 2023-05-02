package interpreter.packages

import interpreter.Environment
import interpreter.values.*
import interpreter.values.classes.ModelObject
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

val JSON = Environment(variables = hashMapOf(
    "objekatIzJSON" to NativeFunction("objekatIzJSON"){ args ->
        if(args.size != 1 && args[0] !is Tekst){
            throw Exception("Argument mismatch")
        }
        val jsonString = (args[0] as Tekst).value

        val jsonObj = JSONObject(jsonString)
        return@NativeFunction parseJSONObject(jsonObj)
    },
    "JSONTekst" to NativeFunction("JSONTekst"){ args ->
        if(args.size != 1){
            throw Exception("Argument mismatch")
        }
        return@NativeFunction Tekst(value = JSONStringify(args[0]))
    }
))


private fun parseJSONObject(jsonObj: JSONObject): Objekat{
    val values = hashMapOf<String, RuntimeValue>()

    jsonObj.keys().forEach {
        val attr = jsonObj.get(it)
        values[it] = parseJSONAttribute(attr)
    }

    return Objekat(values)
}

private fun parseJSONAttribute(attr: Any?): RuntimeValue{
    return when(attr){
        is Int, is Double, is Long, is Float -> Broj(attr.toString().toDouble())
        is String -> Tekst(attr)
        is Boolean -> Logicki(attr)
        is JSONObject -> parseJSONObject(attr)
        is JSONArray -> Niz(ArrayList(attr.map { parseJSONAttribute(it) }))
        JSONObject.NULL -> Null()
        else -> throw Exception("Unsupported JSON attribute encountered: $attr")
    }
}

fun JSONStringify(rv: RuntimeValue): String{
    return when(rv){
        is Broj, is Logicki -> rv.value.toString()
        is Tekst -> "\"${rv.value}\""
        is Null -> "null"
        is Niz -> {
            val sb = StringBuilder("[")
            rv.value.forEach {
                sb.append(JSONStringify(it))
                sb.append(", ")
            }
            sb.append("\b\b]")
            return sb.toString()
        }
        is Objekat -> rv.JSONString()
        is ModelObject -> rv.JSONString()
        else -> throw Exception("Cannot serialize $rv to JSON")
    }
}