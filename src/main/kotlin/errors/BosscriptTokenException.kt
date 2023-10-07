package errors


const val MESSAGE_TEMPLATE = "Pronađen neočekivani simbol: "

class BosscriptTokenException(token: Char, line: Int, col: Int)
    : Exception("[$line:$col] $MESSAGE_TEMPLATE $token")