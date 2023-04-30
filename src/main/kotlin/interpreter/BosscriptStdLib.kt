package interpreter

import interpreter.packages.IO
import interpreter.packages.date.Date
import interpreter.packages.datetime.DateTime
import interpreter.packages.math
import interpreter.packages.time.Time

val stdlib = hashMapOf<String, Environment>(
    "matematika" to math,
    "IO" to IO,
    "DatumVrijeme" to DateTime,
    "Datum" to Date,
    "Vrijeme" to Time,
)