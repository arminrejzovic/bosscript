package errors


class BosscriptParsingError(message: String, location: Pair<Int, Int>) : Exception(
    "[${location.first}:${location.second}] $message\n",
)