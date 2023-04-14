package typechecker

import interpreter.Environment
import interpreter.values.*
import parser.TypeAnnotation

class TypeChecker(private val env: Environment) {
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

                    val expectedKeys = modelDefinition.properties.mapTo(HashSet()) { it.name }

                    providedValue.properties.keys.forEach {
                        if(it !in expectedKeys) throw Exception("${modelDefinition.name} has no property $it")
                    }

                    modelDefinition.properties.forEach {
                        val prop = providedValue.properties[it.name] ?: throw Exception("Missing property ${it.name}")
                        expect(it.type, prop)
                    }
                }
            }
        }
    }
}