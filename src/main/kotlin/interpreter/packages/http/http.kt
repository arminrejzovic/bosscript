package interpreter.packages.http

import interpreter.Environment
import interpreter.values.*
import java.net.HttpURLConnection
import java.net.URL

fun fetch(url: String, method: String = "GET", headers: Map<String, String>? = null, body: String? = null): Objekat {
    val connection = URL(url).openConnection() as HttpURLConnection

    with(connection) {
        requestMethod = method
        doOutput = body != null
        headers?.forEach(this::setRequestProperty)
    }

    if (body != null) {
        connection.outputStream.use {
            it.write(body.toByteArray())
        }
    }

    val responseBody = connection.inputStream.use { it.readBytes() }.toString(Charsets.UTF_8)

    return ResponseFactory.construct(connection.responseCode, connection.headerFields, responseBody)
}

val http = Environment(variables = hashMapOf(
    "zahtjev" to NativeFunction("zahtjev"){ args ->
        if(args.isEmpty() || args[0] !is Tekst){
            throw Exception("Argument mismatch")
        }

        val url = (args[0] as Tekst).value
        val method = if(args.size < 2) "GET" else {
            val providedMethod = args[1] as Tekst
            providedMethod.value
        }
        val headers = if(args.size < 3) null else {
            val map = HashMap<String, String>()
            val providedHeaders = args[2] as Objekat
            providedHeaders.properties.forEach{ (k, v) ->
                map[k] = (v as Tekst).value
            }
            map
        }
        val body = if(args.size < 4) null else {
            val providedBody = args[3] as Tekst
            providedBody.value
        }

        return@NativeFunction fetch(url, method, headers, body)
    }
))