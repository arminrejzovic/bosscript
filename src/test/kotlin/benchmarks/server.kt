package benchmarks

import java.net.ServerSocket

fun main() {
    val server = ServerSocket(6969)

    while (true){
        val clientSocket = server.accept()
        println("Client connected: ${clientSocket.inetAddress.hostAddress}")

        val clientIn = clientSocket.getInputStream()
        val clientOut = clientSocket.getOutputStream()
        val writer = clientOut.bufferedWriter()

        val message = clientIn.bufferedReader().readLine()
        println(message)
        writer.write("ECHO: $message\n")
        writer.flush()
    }
}
