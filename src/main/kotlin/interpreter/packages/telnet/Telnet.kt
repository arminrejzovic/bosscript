package interpreter.packages.telnet

import interpreter.Environment
import interpreter.values.*

val telnet = Environment(
    variables = hashMapOf(
        "TelnetKlijent" to NativeFunction("TelnetKlijent") {
            return@NativeFunction TelnetClientFactory.construct()
        }
    )
)