package typechecker

import interpreter.Environment
import interpreter.values.*
import interpreter.values.classes.ModelObject
import parser.TypeAnnotation

class TypeChecker(private val env: Environment) {
    fun expect(expectedType: TypeAnnotation, providedValue: RuntimeValue) {
        when(providedValue){
            is Tekst, is Broj, is Funkcija, is Null -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}[], got ${providedValue.typename}")
                if(providedValue.typename != expectedType.typeName) throw Exception("Type error: Expected ${expectedType.typeName}, got ${providedValue.typename}")
            }
            is Logicki -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected logicki[], got logicki")
                if(providedValue.typename != "logicki" && providedValue.typename != "logički") throw Exception("Type error: Expected logicki, got ${providedValue.typename}")
            }
            is Niz -> {
                if(!expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}, got ${providedValue.typename}[]")
                if(expectedType.typeName != "niz") providedValue.value.forEach { expect(TypeAnnotation(expectedType.typeName), it) }
            }
            is Objekat -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}[], got ${providedValue.typename}")
                if(expectedType.typeName != "objekat"){
                    val typeDefinition = env.resolveTypeDefinition(expectedType.typeName) ?: throw Exception("Cannot resolve typename ${expectedType.typeName}")

                    val expectedKeys = typeDefinition.properties.mapTo(HashSet()) { it.name }

                    providedValue.properties.keys.forEach {
                        if(it !in expectedKeys) throw Exception("${typeDefinition.name} has no property $it")
                    }

                    typeDefinition.properties.forEach {
                        val prop = providedValue.properties[it.name] ?: throw Exception("Missing property ${it.name}")
                        expect(it.type, prop)
                    }
                }
            }
            is ModelObject -> {
                if(expectedType.isArrayType) throw Exception("Type error: Expected ${expectedType.typeName}[], got ${providedValue.typename}")
                if(providedValue.typename == expectedType.typeName) return
                if(!providedValue.isOfType(expectedType.typeName)){
                    throw Exception("Type error: Expected ${expectedType.typeName}, got ${providedValue.typename}")
                }
            }
        }
    }
}