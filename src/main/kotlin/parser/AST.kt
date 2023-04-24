package parser

interface Statement {
    val kind: NodeType
}

data class EmptyStatement(
    override val kind: NodeType = NodeType.EmptyStatement
) : Statement

data class BreakStatement(
    override val kind: NodeType = NodeType.BreakStatement
) : Statement

data class Program(
    val body: ArrayList<Statement>,
    override val kind: NodeType = NodeType.Program
) : Statement

data class BlockStatement(
    val body: ArrayList<Statement>,
    override val kind: NodeType = NodeType.Block
) : Statement

data class TryCatchStatement(
    val tryBlock: BlockStatement,
    val catchBlock: BlockStatement,
    val finallyBlock: BlockStatement?,
    override val kind: NodeType = NodeType.TryCatch
): Statement

data class FunctionDeclaration(
    val name: Identifier,
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val kind: NodeType = NodeType.FunctionDeclaration
) : Statement

data class FunctionExpression(
    val params: ArrayList<FunctionParameter>,
    val returnType: TypeAnnotation?,
    val body: BlockStatement,
    override val kind: NodeType = NodeType.FunctionExpression
) : Expression

data class FunctionParameter(
    val identifier: Identifier,
    val type: TypeAnnotation?,
    override val kind: NodeType = NodeType.FunctionParameter
): Statement

data class ReturnStatement(
    val argument: Expression?,
    override val kind: NodeType = NodeType.ReturnStatement
) : Statement

data class IfStatement(
    val condition: Expression,
    val consequent: Statement,
    val alternate: Statement?,
    override val kind: NodeType = NodeType.IfStatement
) : Statement

data class WhileStatement(
    val condition: Expression,
    val body: BlockStatement,
    override val kind: NodeType = NodeType.WhileStatement
) : Statement

data class DoWhileStatement(
    val condition: Expression,
    val body: BlockStatement,
    override val kind: NodeType = NodeType.DoWhileStatement
) : Statement

data class ForStatement(
    val counter: Identifier,
    val startValue: Expression,
    val endValue: Expression,
    val step: Expression?,
    val body: BlockStatement,
    override val kind: NodeType = NodeType.ForStatement
) : Statement

data class UnlessStatement(
    val condition: Expression,
    val consequent: Statement,
    val alternate: Statement?,
    override val kind: NodeType = NodeType.UnlessStatement
) : Statement

data class VariableStatement(
    val declarations: ArrayList<VariableDeclaration>,
    val isConstant: Boolean = false,
    override val kind: NodeType = NodeType.VariableStatement
) : Statement

data class AssignmentExpression(
    val assignee: Expression,
    val value: Expression,
    val assignmentOperator: String,
    override val kind: NodeType = NodeType.AssignmentExpression
) : Expression

data class MemberExpression(
    val isComputed: Boolean,
    val targetObject: Expression,
    val property: Expression,
    override val kind: NodeType = NodeType.MemberExpression
) : Expression

data class LogicalExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    override val kind: NodeType = NodeType.LogicalExpression
) : Expression

data class VariableDeclaration(
    val identifier: String,
    val value: Expression?,
    override val kind: NodeType = NodeType.VariableDeclaration
): Statement

data class TipDefinitionStatement(
    val name: Identifier,
    val parentType: Identifier?,
    val properties: ArrayList<TypeProperty>,
    override val kind: NodeType = NodeType.TypeDefinition
): Statement

data class TypeProperty(
    val name: String,
    val type: TypeAnnotation,
    override val kind: NodeType = NodeType.TypePropertyDefinition
): Statement

data class TypeAnnotation(
    val typeName: String,
    val isArrayType: Boolean = false,
    override val kind: NodeType = NodeType.TypeAnnotation
): Statement

data class ImportStatement(
    val packageName: String,
    val imports: ArrayList<Identifier>?,
    override val kind: NodeType = NodeType.ImportStatement
): Statement

interface Expression : Statement

data class BinaryExpression(
    val left: Expression,
    val right: Expression,
    val operator: String,
    override val kind: NodeType = NodeType.BinaryExpression
) : Expression

data class UnaryExpression(
    val operator: String,
    val argument: Expression,
    override val kind: NodeType = NodeType.UnaryExpression
) : Expression


data class Identifier(
    val symbol: String,
    override val kind: NodeType = NodeType.Identifier
) : Expression

data class NumericLiteral(
    val value: Double,
    override val kind: NodeType =  NodeType.NumericLiteral
) : Expression

data class StringLiteral(
    val value: String,
    override val kind: NodeType = NodeType.StringLiteral
) : Expression

data class BooleanLiteral(
    val value: Boolean,
    override val kind: NodeType = NodeType.BooleanLiteral
) : Expression

data class NullLiteral(
    val value: Unit? = null,
    override val kind: NodeType = NodeType.NullLiteral
) : Expression

data class Property(
    val key: String,
    val value: Expression,
    override val kind: NodeType = NodeType.Property
) : Expression

data class ObjectLiteral(
    val properties: ArrayList<Property>,
    override val kind: NodeType = NodeType.Object
) : Expression

data class ArrayLiteral(
    val arr: ArrayList<Expression>,
    override val kind: NodeType = NodeType.ArrayLiteral
): Expression


data class CallExpression(
    val args: ArrayList<Expression>,
    val callee: Expression,
    override val kind: NodeType = NodeType.CallExpression
) : Expression

