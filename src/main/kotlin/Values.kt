enum class ValueType {
    Null,
    Number,
    Bool,
    String,
    Object
}

interface RuntimeValue{
    val type: ValueType
    val toString: String
}

data class NullValue(
    val value: Unit? = null,
    override val type: ValueType = ValueType.Null,
    override val toString: String = value.toString()
): RuntimeValue

data class NumberValue(
    val value: Double = 0.0,
    override val type: ValueType = ValueType.Number,
    override val toString: String = value.toString()
): RuntimeValue

data class StringValue(
    val value: String = "",
    override val type: ValueType = ValueType.String,
    override val toString: String = value
): RuntimeValue

data class BoolValue(
    val value: Boolean = false,
    override val type: ValueType = ValueType.Bool,
    override val toString: String = value.toString()
): RuntimeValue

data class ObjectValue(
    val properties: HashMap<String, RuntimeValue>,
    override val type: ValueType = ValueType.Object,
    override val toString: String = properties.toString(),
    val keys: MutableSet<String> = properties.keys,
    val values: MutableCollection<RuntimeValue> = properties.values
): RuntimeValue
