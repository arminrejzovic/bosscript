package typechecker

import errors.BosscriptRuntimeException
import interpreter.Environment
import interpreter.values.*
import interpreter.values.classes.ModelDefinition
import interpreter.values.classes.ModelObject
import parser.TypeAnnotation

class TypeChecker(private val env: Environment) {
    fun expect(expectedType: TypeAnnotation, providedValue: RuntimeValue, location: Pair<Int, Int> = Pair(-1, -1)) {
        // TODO make this function static, remove the default (-1,-1) location
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

                val trait = env.resolveTraitNullable(expectedType.typeName)
                if(trait != null){
                    val modelDefinition = env.getVariable(providedValue.typename) as ModelDefinition
                    if(!modelDefinition.traits.contains(trait)){
                        throw BosscriptRuntimeException(
                            text = "Greška u tipovima: \n\tOčekivani tip - ${expectedType.typeName} \n\tStvarni tip - ${providedValue.typename}",
                            location = location
                        )
                    }
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

    companion object {
        fun assertTraitConformity(expected: Funkcija, actual: Funkcija, location: Pair<Int, Int>){
            if(expected.returnType != null && expected.returnType != actual.returnType){
                throw BosscriptRuntimeException(text = "Povratni tip funkcije ${actual.name} (${actual.returnType?.typeName}) se ne poklapa sa očekivanim povratnim tipom iz osobine.", location = location)
            }
            if (expected.params.size != actual.params.size){
                throw BosscriptRuntimeException(text = "Broj parametara funkcije ${actual.name} se ne poklapa sa definicijom funkcije u osobini.", location = location)
            }

            expected.params.forEachIndexed { index, expectedParam ->
                val actualParam = actual.params[index]
                if(expectedParam.type != null && expectedParam.type != actualParam.type){
                    throw BosscriptRuntimeException(text = "Tip parametra ${actualParam.identifier} se ne poklapa sa očekivanim povratnim tipom iz osobine.", location = location)
                }
            }
        }
    }
}