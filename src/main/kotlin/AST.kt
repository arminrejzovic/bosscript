enum class NodeType {
    // Statements
    Program,
    VariableDeclaration,

    //Expressions
    NumericLiteral,
    Identifier,
    BinaryExpression,
    AssignmentExpression
}

interface Statement {
    val kind: NodeType
}

data class Program(val body: ArrayList<Statement>) : Statement {
    override val kind: NodeType
        get() = NodeType.Program
}

data class AssignmentExpression(
    val assignee: Expression,
    val value: Expression
) : Expression {
    override val kind: NodeType
        get() = NodeType.AssignmentExpression

}

data class VariableDeclaration(
    val isConstant: Boolean = false,
    val identifier: String,
    val value: Expression?
): Statement{
    override val kind: NodeType
        get() = NodeType.VariableDeclaration
}

interface Expression : Statement

data class BinaryExpression(
    val left: Expression,
    val right: Expression,
    val operator: String
) : Expression {
    override val kind: NodeType
        get() = NodeType.BinaryExpression
}

data class Identifier(
    val symbol: String
) : Expression {
    override val kind: NodeType
        get() = NodeType.Identifier
}

data class NumericLiteral(
    val value: Double
) : Expression {
    override val kind: NodeType
        get() = NodeType.NumericLiteral
}