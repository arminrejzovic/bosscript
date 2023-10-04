package errors

import interpreter.values.Objekat

class BosscriptRuntimeException(
    val exceptionObject: Objekat
) : Exception() {
}