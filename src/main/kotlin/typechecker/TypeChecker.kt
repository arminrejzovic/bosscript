package typechecker

import interpreter.Environment
import interpreter.values.*
import parser.TypeAnnotation

class TypeChecker(private val env: Environment) {
    private fun expectArrayType(expectedType: TypeAnnotation, providedValue: RuntimeValue) {
        TODO("Not yet implemented")
    }

    fun expect(expectedType: TypeAnnotation, providedValue: RuntimeValue) {
        when(providedValue){
            is Tekst, is Broj, is Funkcija, is Logicki, is Null -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}[], got ${providedValue.typename}")
                if(providedValue.typename != expectedType.typeName) throw Exception("Type error: Expected ${expectedType.typeName}, got ${providedValue.typename}")
            }
            is Niz -> {
                if(!expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}, got ${providedValue.typename}[]")
                if(expectedType.typeName != "niz") providedValue.value.forEach { expect(TypeAnnotation(expectedType.typeName), it) }
            }
            is Objekat -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}[], got ${providedValue.typename}")
                if(expectedType.typeName != "objekat"){
                    val modelDefinition = env.resolveModelDefinition(expectedType.typeName) ?: throw Exception("Cannot resolve typename ${expectedType.typeName}")

                    val sortedProps = providedValue.properties.toSortedMap()

                    val sortedExpectedProps = modelDefinition.properties.sortedWith(compareBy { it.name })

                    var i = 0
                    sortedProps.forEach { (name, value) ->
                        if(name != sortedExpectedProps[i].name) throw Exception("${modelDefinition.name} has no property $name")
                        expect(sortedExpectedProps[i].type, value)
                        i++
                    }
                }
            }
        }
    }
}