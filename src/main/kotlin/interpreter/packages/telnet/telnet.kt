package interpreter.packages.telnet

import interpreter.Environment
import interpreter.values.*
import kotlin.math.*

val telnet = Environment(
    variables = hashMapOf(
        "TelnetKlijent" to NativeFunction("TelnetKlijent") {
            return@NativeFunction TelnetClientFactory.construct()
        }
    )
)