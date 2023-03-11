package udemy

import ArrayLiteral
import BlockStatement
import Expression
import FunctionParameter
import ReturnStatement
import StringLiteral
import TypeAnnotation

interface RuntimeValue{
    val value: Any?
    val builtIns: HashMap<String, RuntimeValue>
}

data class Number(
    override val value: Double,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(

    )
): RuntimeValue

data class Text(
    override val value: String,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue

data class Bool(
    override val value: Boolean,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue

data class Null(
    override val value: Nothing? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
):RuntimeValue

data class Array(
    override val value: ArrayList<RuntimeValue>,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue

data class Object(
    val properties: HashMap<String, RuntimeValue>,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "tekst" to Function(
            name = "tekst",
            params = arrayListOf(),
            returnType = TypeAnnotation(
                typeName = "tekst",
                isArrayType = false
            ),
            body = BlockStatement(
                body = arrayListOf(
                    ReturnStatement(
                        argument = StringLiteral(
                            value = properties.toString()
                        )
                    )
                )
            )
        ),
        "kljucevi" to Function(
            name = "kljucevi",
            params = arrayListOf(),
            returnType = TypeAnnotation(
                typeName = "tekst",
                isArrayType = true
            ),
            body = BlockStatement(
                body = arrayListOf(
                    ReturnStatement(
                        argument = ArrayLiteral(
                            arr = properties.keys.map { StringLiteral(it) } as ArrayList<Expression>
                        )
                    )
                )
            )
        ),
        "vrijednosti" to Function(
            name = "vrijednosti",
            params = arrayListOf(),
            returnType = TypeAnnotation(
                typeName = "tekst",
                isArrayType = true
            ),
            body = BlockStatement(
                body = arrayListOf(
                    ReturnStatement(
                        argument = ArrayLiteral(
                            arr = properties.values.map { StringLiteral(it.toString()) } as ArrayList<Expression>
                        )
                    )
                )
            )
        )
    )
): RuntimeValue

data class Function(
    val name: String,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue
