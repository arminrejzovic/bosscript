enum class ValueType {
    Null,
    Number,
    Bool,
    String,
    Object
}

interface RuntimeValue{
    val type: ValueType
}

data class NullValue(
    val value: Unit? = null,
    override val type: ValueType = ValueType.Null,
): RuntimeValue

data class NumberValue(
    val value: Double = 0.0,
    override val type: ValueType = ValueType.Number,
): RuntimeValue

data class StringValue(
    val value: String = "",
    override val type: ValueType = ValueType.String,
): RuntimeValue

data class BoolValue(
    val value: Boolean = false,
    override val type: ValueType = ValueType.Bool,
): RuntimeValue

data class ObjectValue(
    val properties: HashMap<String, RuntimeValue>,
    override val type: ValueType = ValueType.Object,
): RuntimeValue

interface IterableRuntimeValue{
    val value: Iterable<Any>
}