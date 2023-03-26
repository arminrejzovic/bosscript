package interpreter.values

import errors.SyntaxError

data class ReturnValue(
    override val value: RuntimeValue,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(),
) : RuntimeValue {
    init {
        /*
         A ReturnValue is a wrapper that exists just so that return statements can be bubbled up to the top level
         Its value can't be another ReturnValue (since you cant say return return x)
         This should never happen in reality, but let's have code handling it just in case
         */
        if (value is ReturnValue) {
            throw SyntaxError("Something went wrong")
        }
    }

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("ReturnValue members should never be accessed")
    }
}