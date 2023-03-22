package interpreter

import interpreter.packages.math

val stdlib = hashMapOf<String, Environment>(
    "matematika" to math
)