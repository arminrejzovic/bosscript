package errors

class BosscriptSyntaxError(message: String, location: Pair<Int, Int>) : Exception(
    "[${location.first}:${location.second}] $message") {}