package interpreter.values

/**
 * data class TraitDefinitionStatement(
 *     val name: Identifier,
 *     val functions: ArrayList<FunctionDeclaration>,
 *     override val start: Pair<Int, Int> = Pair(0, 0),
 *     override val end: Pair<Int, Int> = Pair(0, 0),
 *     override val kind: NodeType = NodeType.TraitDefinition
 * ): Statement
 */
class Trait (
    val name: String,
    val functions: ArrayList<Funkcija>
): RuntimeValue{

    fun nonAbstractFunctions() = functions.filter { it.body.body.isNotEmpty() }
    fun abstractFunctions() = functions.filter { it.body.body.isEmpty() }
    override val value: Any?
        get() = null
    override val builtIns: HashMap<String, RuntimeValue>
        get() = hashMapOf()
    override val typename: String
        get() = name

    override fun getProperty(prop: String): RuntimeValue {
        throw Exception("Definicija osobine nema pripadajuće vrijednosti.")
    }
}