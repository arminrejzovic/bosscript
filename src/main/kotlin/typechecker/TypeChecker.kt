package typechecker

import errors.BosscriptRuntimeException
import interpreter.Environment
import interpreter.values.*
import interpreter.values.classes.ModelObject
import parser.TypeAnnotation

class TypeChecker(private val env: Environment) {
    fun expect(expectedType: TypeAnnotation, providedValue: RuntimeValue, location: Pair<Int, Int> = Pair(-1, -1)) {
        when(providedValue){
            is Tekst, is Broj, is Funkcija, is Null -> {
                if(expectedType.isArrayType) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName}[] \n\tStvarni tip - ${providedValue.typename}",
                        location = location
                    )
                }
                if(providedValue.typename != expectedType.typeName) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName} \n\tStvarni tip - ${providedValue.typename}",
                        location = location
                    )
                }
            }
            is Logicki -> {
                if(expectedType.isArrayType) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName}[] \n\tStvarni tip - logički",
                        location = location
                    )
                }

                if(expectedType.typeName != "logicki" && expectedType.typeName != "logički") {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName} \n\tStvarni tip - logički",
                        location = location
                    )
                }
            }
            is Niz -> {
                if(!expectedType.isArrayType) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName} \n\tStvarni tip - ${providedValue.typename}[]",
                        location = location
                    )
                }
                if(expectedType.typeName != "niz") {
                    providedValue.value.forEach { expect(TypeAnnotation(expectedType.typeName), it, location) }
                }
            }
            is Objekat -> {
                if(expectedType.isArrayType) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName}[] \n\tStvarni tip - ${providedValue.typename}",
                        location = location
                    )
                }
                if(expectedType.typeName != "objekat"){
                    val typeDefinition = env.resolveTypeDefinition(expectedType.typeName)
                            ?: throw BosscriptRuntimeException(
                                text = "Tip '${expectedType.typeName}' ne postoji.",
                                location = location
                            )

                    val expectedKeys = typeDefinition.properties.mapTo(HashSet()) { it.name }

                    providedValue.properties.keys.forEach {
                        if(it !in expectedKeys) {
                            throw BosscriptRuntimeException(
                                text = "Tip '${expectedType.typeName}' ne sadrži polje '$it'.",
                                location = location
                            )
                        }
                    }

                    typeDefinition.properties.forEach {
                        val prop = providedValue.properties[it.name]
                                ?: throw BosscriptRuntimeException(
                                    text = "Pronađenom objektu nedostaje polje '${it.name}' sa tipa '${typeDefinition.name}'",
                                    location = location
                                )

                        expect(it.type, prop, location)
                    }
                }
            }
            is ModelObject -> {
                if(expectedType.isArrayType) {
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName}[] \n\tStvarni tip - ${providedValue.typename}",
                        location = location
                    )
                }
                if(providedValue.typename == expectedType.typeName) {
                    return
                }
                if(!providedValue.isOfType(expectedType.typeName)){
                    throw BosscriptRuntimeException(
                        text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName} \n\tStvarni tip - ${providedValue.typename}",
                        location = location
                    )
                }
            }
        }
    }
}