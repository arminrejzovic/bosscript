package interpreter.values.bytes

import interpreter.values.*

data class BajtNiz(
    override val value: ByteArray,
    override val builtIns: HashMap<String, RuntimeValue> = hashMapOf(
        "velicina" to Broj(value.size.toDouble()),
        "dobavi" to object : NativeFunkcija("dobavi"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 || args[0] !is Broj){
                    throw Exception("Argument mismatch")
                }
                val index = (args[0] as Broj).value.toInt()
                return Bajt(value[index])
            }
        },
        "postavi" to object : NativeFunkcija("postavi"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 2 || args[0] !is Broj || args[1] !is Bajt){
                    throw Exception("Argument mismatch")
                }
                val index = (args[0] as Broj).value.toInt()
                val byte = (args[1] as Bajt).value
                value[index] = byte
                return Null()
            }
        },
        "isjecak" to object : NativeFunkcija("isjecak"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 2 || args[0] !is Broj || args[1] !is Broj){
                    throw Exception("Argument mismatch")
                }
                val start = (args[0] as Broj).value.toInt()
                val end = (args[0] as Broj).value.toInt()

                val bytes = value.copyOfRange(start, end)

                return BajtNiz(bytes)
            }
        },
        "jednako" to object : NativeFunkcija("jednako"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.size != 1 || args[0] !is BajtNiz){
                    throw Exception("Argument mismatch")
                }
                val other = args[0] as BajtNiz

                return Logicki(value = value.contentEquals(other.value))
            }
        },

        "hashKod" to object : NativeFunkcija("hashKod"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.isNotEmpty()){
                    throw Exception("Argument mismatch")
                }

                return Broj(value.hashCode().toDouble())
            }
        },

        "dekodiraj" to object : NativeFunkcija("dekodiraj"){
            override fun call(vararg args: RuntimeValue): RuntimeValue {
                if(args.isNotEmpty()){
                    throw Exception("Argument mismatch")
                }

                return Tekst(value = String(value))
            }
        }
    ),
    override val typename: String = "bajt"
): RuntimeValue {
    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("BajtNiz has no property $prop")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BajtNiz

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}