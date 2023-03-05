package udemy

import BlockStatement
import FunctionParameter
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
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue

data class Function(
    val name: String,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val value: Any? = null,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf()
): RuntimeValue
