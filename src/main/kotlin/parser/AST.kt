package parser

interface Statement {
    val start: Pair<Int, Int>
    val end: Pair<Int, Int>
    val kind: NodeType
}

data class EmptyStatement(
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.EmptyStatement
) : Statement

data class BreakStatement(
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.BreakStatement
) : Statement

data class Program(
    val body: ArrayList<Statement>,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.Program
) : Statement

data class BlockStatement(
    val body: ArrayList<Statement>,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.Block
) : Statement

data class TryCatchStatement(
    val tryBlock: BlockStatement,
    val catchBlock: BlockStatement,
    val exceptionIdentifier: Identifier,
    val finallyBlock: BlockStatement?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.TryCatch
): Statement

data class FunctionDeclaration(
    val name: Identifier,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.FunctionDeclaration
) : Statement

data class FunctionExpression(
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.FunctionExpression
) : Expression

data class FunctionParameter(
    val identifier: String,
    val type: TypeAnnotation?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.FunctionParameter
): Statement

data class ReturnStatement(
    val argument: Expression?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ReturnStatement
) : Statement

data class IfStatement(
    val condition: Expression,
    val consequent: Statement,
    val alternate: Statement?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.IfStatement
) : Statement

data class WhileStatement(
    val condition: Expression,
    val body: BlockStatement,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.WhileStatement
) : Statement

data class DoWhileStatement(
    val condition: Expression,
    val body: BlockStatement,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.DoWhileStatement
) : Statement

data class ForStatement(
    val counter: Identifier,
    val startValue: Expression,
    val endValue: Expression,
    val step: Expression?,
    val body: BlockStatement,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ForStatement
) : Statement

data class UnlessStatement(
    val condition: Expression,
    val consequent: Statement,
    val alternate: Statement?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.UnlessStatement
) : Statement

data class VariableStatement(
    val declarations: ArrayList<VariableDeclaration>,
    val isConstant: Boolean = false,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.VariableStatement
) : Statement

data class AssignmentExpression(
    val assignee: Expression,
    val value: Expression,
    val assignmentOperator: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.AssignmentExpression
) : Expression

data class MemberExpression(
    val isComputed: Boolean,
    val targetObject: Expression,
    val property: Expression,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.MemberExpression
) : Expression

data class LogicalExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.LogicalExpression
) : Expression

data class VariableDeclaration(
    val identifier: String,
    val value: Expression?,
    val type: TypeAnnotation? = null,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.VariableDeclaration
): Statement

data class TipDefinitionStatement(
    val name: Identifier,
    val parentType: Identifier?,
    val properties: ArrayList<TypeProperty>,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.TypeDefinition
): Statement

data class ModelDefinitionStatement(
    val className: Identifier,
    val parentClassName: Identifier?,
    val constructor: FunctionDeclaration,
    val privateBlock: ModelBlock?,
    val publicBlock: ModelBlock?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ModelDefinition
): Statement

data class ModelBlock(
    private val body: ArrayList<Statement> = arrayListOf(),
    override var start: Pair<Int, Int> = Pair(0, 0),
    override var end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ModelBlock
): Statement{
    fun getBody(): ArrayList<Statement>{
        return body
    }

    fun addStatement(stmt: Statement){
        if(stmt is VariableStatement || stmt is FunctionDeclaration){
            body.add(stmt)
            return
        }
        throw Exception("Nedostaje očekivani izraz pristupanja članu")
    }
}

data class TypeProperty(
    val name: String,
    val type: TypeAnnotation,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.TypePropertyDefinition
): Statement

open class TypeAnnotation(
    val typeName: String,
    val isArrayType: Boolean = false,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.TypeAnnotation
): Statement

data class FunctionTypeAnnotation (
    val parameters: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation
) : TypeAnnotation("funkcija", isArrayType = false)

data class UnionTypeAnnotation (
    val possibleTypes: ArrayList<TypeAnnotation>
): TypeAnnotation("unija", false)

data class ImportStatement(
    val packageName: String,
    val imports: ArrayList<Identifier>?,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ImportStatement
): Statement

data class JavascriptSnippet(
    val code: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.TypeDefinition
): Expression

interface Expression : Statement

data class BinaryExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.BinaryExpression
) : Expression

data class UnaryExpression(
    val operator: String,
    val operand: Expression,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.UnaryExpression
) : Expression


data class Identifier(
    val symbol: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.Identifier
) : Expression {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Identifier

        return symbol == other.symbol
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}

data class NumericLiteral(
    val value: Double,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType =  NodeType.NumericLiteral
) : Expression

data class StringLiteral(
    val value: String,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.StringLiteral
) : Expression

data class BooleanLiteral(
    val value: Boolean,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.BooleanLiteral
) : Expression

data class NullLiteral(
    val value: Unit? = null,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.NullLiteral
) : Expression

data class Property(
    val key: String,
    val value: Expression,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.Property
) : Expression

data class ObjectLiteral(
    val properties: ArrayList<Property>,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.Object
) : Expression

data class ArrayLiteral(
    val arr: ArrayList<Expression>,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.ArrayLiteral
): Expression


data class CallExpression(
    val args: ArrayList<Expression>,
    val callee: Expression,
    override val start: Pair<Int, Int> = Pair(0, 0),
    override val end: Pair<Int, Int> = Pair(0, 0),
    override val kind: NodeType = NodeType.CallExpression
) : Expression

