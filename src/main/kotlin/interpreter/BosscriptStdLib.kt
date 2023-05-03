package interpreter

import interpreter.packages.IO
import interpreter.packages.JSON
import interpreter.packages.chrono
import interpreter.packages.date.Date
import interpreter.packages.datetime.DateTime
import interpreter.packages.math
import interpreter.packages.regex.regex
import interpreter.packages.telnet.telnet
import interpreter.packages.time.Time

val stdlib = hashMapOf<String, Environment>(
    "matematika" to math,
    "IO" to IO,
    "datumvrijeme" to DateTime,
    "datum" to Date,
    "vrijeme" to Time,
    "telnet" to telnet,
    "JSON" to JSON,
    "hrono" to chrono,
    "regex" to regex
)