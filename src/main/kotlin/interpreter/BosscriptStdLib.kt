package interpreter

import interpreter.packages.IO
import interpreter.packages.datetime.DateTime
import interpreter.packages.math

val stdlib = hashMapOf<String, Environment>(
    "matematika" to math,
    "IO" to IO,
    "vrijeme" to DateTime
)