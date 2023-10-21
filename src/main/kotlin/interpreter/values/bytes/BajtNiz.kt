package interpreter.values.bytes

import interpreter.values.*

class BajtNiz(
    override val value: ByteArray,
): RuntimeValue {
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf(
            "velicina" to Broj(value.size.toDouble()),
            "dobavi" to NativeFunction("dobavi"){args ->
                if(args.size != 1 || args[0] !is Broj){
                    throw Exception("Argument mismatch")
                }
                val index = (args[0] as Broj).value.toInt()
                return@NativeFunction Bajt(value[index])
            },
            "postavi" to NativeFunction("postavi"){args ->
                if(args.size != 2 || args[0] !is Broj || args[1] !is Bajt){
                    throw Exception("Argument mismatch")
                }
                val index = (args[0] as Broj).value.toInt()
                val byte = (args[1] as Bajt).value
                value[index] = byte
                return@NativeFunction Null()
            },
            "isjecak" to NativeFunction("isjecak"){args ->
                if(args.size != 2 || args[0] !is Broj || args[1] !is Broj){
                    throw Exception("Argument mismatch")
                }
                val start = (args[0] as Broj).value.toInt()
                val end = (args[0] as Broj).value.toInt()

                val bytes = value.copyOfRange(start, end)

                return@NativeFunction BajtNiz(bytes)
            },
            "jednako" to NativeFunction("jednako"){args ->
                if(args.size != 1 || args[0] !is BajtNiz){
                    throw Exception("Argument mismatch")
                }
                val other = args[0] as BajtNiz

                return@NativeFunction Logicki(value = value.contentEquals(other.value))
            },

            "hashKod" to NativeFunction("hashKod"){args ->
                if(args.isNotEmpty()){
                    throw Exception("Argument mismatch")
                }

                return@NativeFunction Broj(value.hashCode().toDouble())
            },

            "dekodiraj" to NativeFunction("dekodiraj"){args ->
                if(args.isNotEmpty()){
                    throw Exception("Argument mismatch")
                }

                return@NativeFunction Tekst(value = String(value))
            }
        )
    override val typename: String
        get() = "bajt"

    override fun getProperty(prop: String): RuntimeValue {
        return builtIns[prop] ?: throw Exception("Vrijednost '$prop' ne postoji na tipu 'BajtNiz'")
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