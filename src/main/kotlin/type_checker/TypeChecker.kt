package type_checker

import interpreter.values.Niz
import interpreter.values.RuntimeValue

class TypeChecker {
    fun isExpectedPrimitiveType(expectedTypename: String, value: RuntimeValue): Boolean{
        return value.typename == expectedTypename
    }

    fun isExpectedArrayType(expectedTypename: String, array: RuntimeValue): Boolean{
        if(array !is Niz){
            throw Exception("Type Error")
        }
        else{
            return array.value.all {
                it.typename == expectedTypename
            }
        }
    }
}