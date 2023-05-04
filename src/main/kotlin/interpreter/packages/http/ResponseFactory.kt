package interpreter.packages.http

import interpreter.values.*

class ResponseFactory {
    companion object{
        fun construct(statusCode: Int, headers: Map<String, List<String>>?, body: String?): Objekat{
            val headersMap = hashMapOf<String, RuntimeValue>()
            headers?.forEach { (k, v) ->
                headersMap[k] = Niz(ArrayList(v.map { Tekst(it) }))
            }

            return Objekat(hashMapOf(
                "status" to Broj(statusCode.toDouble()),
                "headeri" to Objekat(headersMap),
                "tijelo" to if(body == null) Null() else Tekst(body)
            ))
        }
    }
}