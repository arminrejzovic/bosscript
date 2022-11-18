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
    val value: Unit? = null
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.Null
}

data class NumberValue(
    val value: Double = 0.0
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.Number
}

data class StringValue(
    val value: String = ""
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.String
}

data class BoolValue(
    val value: Boolean = false
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.Bool
}

data class ObjectValue(
    val properties: HashMap<String, RuntimeValue>
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.Object
}
