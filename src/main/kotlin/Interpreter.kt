fun interpret(astNode: Statement, environment: Environment): RuntimeValue{
    when (astNode.kind){
        NodeType.Program -> {
            return interpretProgram(astNode as Program, environment)
        }
        NodeType.NumericLiteral -> {
            return NumberValue((astNode as NumericLiteral).value)
        }
        NodeType.Identifier -> {
            return interpretIdentifier(astNode as Identifier, environment)
        }
        NodeType.BinaryExpression -> {
            return interpretBinaryExpression(astNode as BinaryExpression, environment)
        }
        NodeType.VariableDeclaration -> {
            return interpretVariableDeclaration(astNode as VariableDeclaration, environment)
        }
        NodeType.AssignmentExpression -> {
            return interpretAssignment(astNode as AssignmentExpression, environment)
        }
        else -> {
            println(astNode.toString().toIndentString())
            throw Exception("Unrecognized AST node found")
        }
    }
}

fun interpretIdentifier(identifier: Identifier, environment: Environment): RuntimeValue {
    return environment.getVariable(identifier.symbol)
}

fun interpretProgram(program: Program, environment: Environment): RuntimeValue{
    var lastEvaluated: RuntimeValue = NullValue()

    program.body.forEach {
        lastEvaluated = interpret(it, environment)
    }

    return lastEvaluated
}

fun interpretBinaryExpression(binaryExpression: BinaryExpression, environment: Environment): RuntimeValue{
    val left = interpret(binaryExpression.left, environment)
    val right = interpret(binaryExpression.right, environment)

    if (left.type == ValueType.Number && right.type == ValueType.Number){
        return interpretNumericBinaryExpression(left as NumberValue, right as NumberValue, binaryExpression.operator)
    }

    return NullValue()
}

fun interpretNumericBinaryExpression(left: NumberValue, right: NumberValue, operator: String): NumberValue {
    var result = 0.0
    when(operator){
        "+" -> result = left.value + right.value
        "-" -> result = left.value - right.value
        "*" -> result = left.value * right.value
        "/" -> {
            if(right.value == 0.0){
                throw Exception("Division by zero")
            }
            result = left.value / right.value
        }
        "%" -> {
            result = left.value % right.value
        }
    }
    return NumberValue(result)
}

fun interpretVariableDeclaration(declaration: VariableDeclaration, env: Environment): RuntimeValue{
    val value = if (declaration.value == null) NullValue() else interpret(declaration.value, env)

    return env.declareVariable(declaration.identifier, value, declaration.isConstant)
}

fun interpretAssignment(node: AssignmentExpression, env: Environment): RuntimeValue{
    if (node.assignee.kind != NodeType.Identifier){
        throw Exception("Invalid lvalue provided")
    }
    return env.assignVariable((node.assignee as Identifier).symbol, interpret(node.value, env))
}
