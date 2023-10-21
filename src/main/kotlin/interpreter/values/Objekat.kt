package interpreter.values

import interpreter.packages.JSONStringify

open class Objekat(
    val properties: HashMap<String, RuntimeValue>,
) : RuntimeValue {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Objekat

        return properties == other.properties
    }

    override fun toString(): String {
        return properties.toString().replace("=", ": ")
    }

    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf(
            "zakljucaj" to zakljucaj,
            "zaključaj" to zakljucaj,
            "kljucevi" to kljucevi,
            "ključevi" to kljucevi,
            "vrijednosti" to vrijednosti,
            "parovi" to parovi,
            "imaKljuc" to imaKljuc,
            "imaKljuč" to imaKljuc,
            "sadrziVrijednost" to sadrziVrijednost,
            "sadržiVrijednost" to sadrziVrijednost,
        )
    override val typename: String
        get() = "objekat"

    override fun getProperty(prop: String): RuntimeValue {
        return (builtIns[prop] ?: properties[prop]) ?: throw Exception("Vrijednost $prop ne postoji na objektu $this")
    }

    private val zakljucaj = NativeFunction("zakljucaj"){
        return@NativeFunction ReadonlyObject(HashMap(properties))
    }

    private val kljucevi = NativeFunction("kljucevi"){
        val keys: ArrayList<RuntimeValue> = ArrayList()
        properties.keys.forEach { key -> keys.add(Tekst(key)) }
        return@NativeFunction Niz(keys)
    }

    private val vrijednosti = NativeFunction("vrijednosti"){
        val values: ArrayList<RuntimeValue> = ArrayList()
        properties.values.forEach { value -> values.add(value) }
        return@NativeFunction Niz(values)
    }

    private val parovi = NativeFunction("parovi"){
        val pairs: ArrayList<RuntimeValue> = ArrayList()
        properties.forEach { (k, v) ->  pairs.add(Niz(arrayListOf(Tekst(k), v)))}
        return@NativeFunction Niz(pairs)
    }

    private val imaKljuc = NativeFunction("imaKljuč"){ args ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'imaKljuč' prihvata 1 argument (ključ: tekst)")
        }
        if(args[0] !is Tekst){
            throw RuntimeException("Funkcija 'imaKljuč' prihvata 1 argument (ključ: tekst).")
        }
        val key = args[0].value
        return@NativeFunction Logicki(properties.containsKey(key))
    }

    private val sadrziVrijednost = NativeFunction("sadrziVrijednost"){ args ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'sadrziVrijednost' prihvata 1 argument (vrijednost: nepoznato)")
        }

        val value = args[0]
        return@NativeFunction Logicki(properties.containsValue(value))
    }

    fun setProperty(prop: String, newValue: RuntimeValue): RuntimeValue {
        properties[prop] = newValue
        return properties[prop]!!
    }

    fun JSONString(): String{
        val sb = StringBuilder("{")
        properties.forEach {
            if(it.value !is Funkcija && it.value !is NativeFunction && it.value !is ContextualNativeFunction){
                sb.append("\"${it.key}\": ${JSONStringify(it.value)}")
                sb.append(", ")
            }
        }
        sb.append("\b\b}")
        return sb.toString()
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }
}