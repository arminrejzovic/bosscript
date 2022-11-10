enum class ValueType {
    Null,
    Number,
    Bool
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

data class BoolValue(
    val value: Boolean = false
): RuntimeValue{
    override val type: ValueType
        get() = ValueType.Number
}
