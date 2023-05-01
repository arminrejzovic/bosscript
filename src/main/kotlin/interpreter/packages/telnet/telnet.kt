package interpreter.packages.telnet

import interpreter.Environment
import interpreter.values.*
import kotlin.math.*

val telnet = Environment(
    variables = hashMapOf(
        "TelnetKlijent" to object : NativeFunkcija("TelnetKlijent"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                return TelnetClientFactory.construct()
            }
        }
    )
)