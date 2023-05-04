package benchmarks

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.Scanner


fun main() {
    val client = Socket("localhost", 6969)
    val out = client.getOutputStream()
    val writer = out.bufferedWriter()

    val input = client.getInputStream()
    val reader = input.bufferedReader()

    while (true){
        val rnd = Math.random()
        writer.write("$rnd\n")
        writer.flush()
        val response = reader.readLine()
        println(response)
    }
}

