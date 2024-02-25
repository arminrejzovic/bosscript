package transpiler

import parser.*

class Transpiler {
    private val parser = Parser(js = true)
    private var indent = 0


    private fun indent(): String{
        return " ".repeat(indent)
    }
    fun transpileProgram(src: String): String {
        val program = parser.parseProgram(src)
        val sb = StringBuilder()
        program.body.forEach {
            sb.append(transpile(it))
            sb.append("\n")
            indent = 0
        }
        return sb.toString()
    }

    private fun transpile(node: Statement): String {
        when (node) {
            // ---------------------------------------------------------------------------------------------------------
            // Literals
            is NumericLiteral -> {
                return "${node.value}"
            }

            is StringLiteral -> {
                return "'${node.value}'"
            }

            is BooleanLiteral -> {
                return "${node.value}"
            }

            is NullLiteral -> {
                return "null"
            }

            is ArrayLiteral -> {
                val str = StringBuilder("[")
                node.arr.forEach {
                    str.append(transpile(it))
                    str.append(", ")
                }
                str.deleteCharAt(str.length - 1)
                str.deleteCharAt(str.length - 1)
                str.append("]")
                return str.toString()
            }

            is ObjectLiteral -> {
                val str = StringBuilder("{ ")
                node.properties.forEach {
                    str.append(indent()).append("${it.key}: ${transpile(it.value)}, ")
                }
                str.deleteCharAt(str.length - 1)
                str.deleteCharAt(str.length - 1)
                str.append(" }")
                return str.toString()
            }

            is JavascriptSnippet -> {
                return transpileJavascriptSnippet(node)
            }
            // ---------------------------------------------------------------------------------------------------------

            is BinaryExpression -> {
                return "${transpile(node.left)} ${node.operator} ${transpile(node.right)}"
            }

            is UnaryExpression -> {
                return "${node.operator} ${transpile(node.operand)}"
            }

            is Identifier -> {
                if(node.symbol == "@"){
                    return "this"
                }
                if(stdlibMappings[node.symbol] != null){
                    return stdlibMappings[node.symbol]!!
                }
                return node.symbol
            }

            is VariableStatement -> {
                return transpileVariableStatement(node)
            }

            is AssignmentExpression -> {
                return transpileAssignmentExpression(node)
            }

            is BlockStatement -> {
                return transpileBlockStatement(node)
            }

            is FunctionDeclaration -> {
                return transpileFunctionDeclaration(node)
            }

            is FunctionExpression -> {
                return transpileFunctionExpression(node)
            }

            is ReturnStatement -> {
                return transpileReturnStatement(node)
            }

            is BreakStatement -> {
                return transpileBreakStatement(node)
            }

            is IfStatement -> {
                return transpileIfStatement(node)
            }

            is UnlessStatement -> {
                return transpileUnlessStatement(node)
            }

            is CallExpression -> {
                return transpileFunctionCall(node)
            }

            is ForStatement -> {
                return transpileForStatement(node)
            }

            is WhileStatement -> {
                return transpileWhileStatement(node)
            }

            is DoWhileStatement -> {
                return transpileDoWhileStatement(node)
            }

            is MemberExpression -> {
                return transpileMemberExpression(node)
            }

            is LogicalExpression -> {
                return "${transpile(node.left)} ${node.operator} ${transpile(node.right)}"
            }

            is ImportStatement -> {
                return transpileImportStatement(node)
            }

            is ModelDefinitionStatement -> {
                return transpileModelDefinition(node)
            }

            is TryCatchStatement -> {
                return transpileTryCatch(node)
            }

            is TipDefinitionStatement -> {
                throw Exception("Prilikom transpilacije u JavaScript nije moguće definisati tipove")
            }

            else -> {
                throw Exception("Pronađen neočekivani token '$node'")
            }
        }
    }

    private fun transpileJavascriptSnippet(js: JavascriptSnippet): String {
        return js.code
    }

    private fun transpileModelDefinition(stmt: ModelDefinitionStatement): String {
        val sb = StringBuilder("class ${stmt.className.symbol}")
        with(stmt){
            if(parentClassName != null){
                sb.append(" extends ${parentClassName.symbol} ")
            }
            sb.append("{\n")
            sb.append("    constructor(${transpileParams(constructor.params)}) ${transpileBlockStatement(constructor.body)}")

            privateBlock?.getBody()?.forEach {
                sb.append(indent()).append(transpileModelProperty(it)).append("\n")
            }

            publicBlock?.getBody()?.forEach {
                sb.append(indent()).append(transpileModelProperty(it)).append("\n")
            }
        }
        sb.append("\n}")

        return sb.toString()
    }

    private fun transpileModelProperty(prop: Statement): String{
        if(prop is FunctionDeclaration){
            return "${prop.name.symbol}(${transpileParams(prop.params)}) ${transpileBlockStatement(prop.body)}"
        }
        else if(prop is VariableStatement){
            val sb = StringBuilder()
            sb.deleteCharAt(sb.length - 1)
            sb.deleteCharAt(sb.length - 1)
            sb.append(";")
            return sb.toString()
        }
        throw Exception("Neispravan član modela")
    }

    private fun transpileTryCatch(stmt: TryCatchStatement): String {
        val sb = StringBuilder("try")
        val regex = Regex("""(?<!\w)g\b(?![(.\w)])""")

        sb.append(transpileBlockStatement(stmt.tryBlock))
        sb.append(indent()).append("catch(error)")
        sb.append(transpileBlockStatement(stmt.catchBlock).replace(regex, "error"))

        if(stmt.finallyBlock != null){
            sb.append(indent()).append("finally")
            sb.append(transpileBlockStatement(stmt.finallyBlock))
        }
        return sb.toString()
    }

    private fun transpileImportStatement(stmt: ImportStatement): String {
        if(stmt.imports != null){
            val sb = StringBuilder("import { ")
            stmt.imports.forEach {
                sb.append(it.symbol).append(", ")
            }
            sb.deleteCharAt(sb.length - 2)
            sb.append("} from '${stmt.packageName}';")
            return sb.toString()
        }
        return "import ${stmt.packageName} from '${stmt.packageName}';"
    }


    private fun transpileVariableStatement(stmt: VariableStatement): String {
        val sb = StringBuilder(if (stmt.isConstant) "const " else "let ")
        stmt.declarations.forEach {
            sb.append(transpileVariableDeclaration(it)).append(", ")
        }
        sb.deleteCharAt(sb.length - 1)
        sb.deleteCharAt(sb.length - 1)
        sb.append(";")
        return sb.toString()
    }

    private fun transpileVariableDeclaration(declaration: VariableDeclaration): String {
        val sb = StringBuilder(declaration.identifier)
        if(declaration.value != null){
            sb.append(" = ")
            sb.append(transpile(declaration.value))
        }
        return sb.toString()
    }

    private fun transpileAssignmentExpression(expr: AssignmentExpression): String {
        return "${transpile(expr.assignee)} ${expr.assignmentOperator} ${transpile(expr.value)}"
    }

    private fun transpileBlockStatement(block: BlockStatement): String {
        indent += 4
        val sb = StringBuilder("{\n")
        block.body.forEach {
            sb.append(indent()).append(transpile(it)).append("\n")
        }
        indent -= 4
        sb.append(indent()).append("}\n")
        return sb.toString()
    }

    private fun transpileFunctionDeclaration(declaration: FunctionDeclaration): String {
        return "function ${declaration.name.symbol}(${transpileParams(declaration.params)}) ${transpileBlockStatement(declaration.body)}"
    }

    private fun transpileParams(params: ArrayList<FunctionParameter>): String {
        val sb = StringBuilder()
        params.forEachIndexed { i, it ->
            sb.append(it.identifier.symbol)
            if(i != params.lastIndex){
                sb.append(", ")
            }
        }
        return sb.toString()
    }

    private fun transpileFunctionExpression(expr: FunctionExpression): String {
        return "function(${transpileParams(expr.params)})${transpileBlockStatement(expr.body)}"
    }

    private fun transpileFunctionCall(call: CallExpression): String {
        val sb = StringBuilder("${transpile(call.callee)}(")
        call.args.forEachIndexed { i, it ->
            sb.append(transpile(it))
            if(i != call.args.lastIndex){
                sb.append(", ")
            }
        }
        sb.append(")")

        return sb.toString()
    }

    private fun transpileIfStatement(stmt: IfStatement): String {
        val sb = StringBuilder("if(${transpile(stmt.condition)})")
        sb.append(transpile(stmt.consequent)).append("\n")
        if(stmt.alternate != null){
            sb.append("else ")
            sb.append(transpile(stmt.alternate)).append("\n")
        }

        return sb.toString()
    }

    private fun transpileUnlessStatement(stmt: UnlessStatement): String {
        val sb = StringBuilder("if(!${transpile(stmt.condition)})")
        sb.append(transpile(stmt.consequent)).append("\n")
        if(stmt.alternate != null){
            sb.append("else ")
            sb.append(transpile(stmt.alternate)).append("\n")
        }

        return sb.toString()
    }

    private fun transpileForStatement(stmt: ForStatement): String {
        val sb = StringBuilder(
            "for(let ${stmt.counter.symbol} = ${transpile(stmt.startValue)}; ${stmt.counter.symbol} <= ${transpile(stmt.endValue)}; "
        )
        sb.append("${stmt.counter.symbol} += ")
        if(stmt.step != null){
            sb.append(transpile(stmt.step))
        }
        else{
            sb.append("1;")
        }

        sb.append(transpileBlockStatement(stmt.body))
        return sb.toString()
    }

    private fun transpileWhileStatement(stmt: WhileStatement): String {
        val sb = StringBuilder("while(${transpile(stmt.condition)})")
        sb.append(transpileBlockStatement(stmt.body))
        return sb.toString()
    }

    private fun transpileDoWhileStatement(stmt: DoWhileStatement): String {
        val sb = StringBuilder("do")
        sb.append(transpileBlockStatement(stmt.body))
        sb.append("while(${transpile(stmt.condition)});")
        return sb.toString()
    }

    private fun transpileReturnStatement(stmt: ReturnStatement): String {
        if(stmt.argument == null){
            return "return;"
        }
        return "return ${transpile(stmt.argument)}"
    }

    private fun transpileBreakStatement(stmt: BreakStatement): String {
        return "break;"
    }

    private fun transpileMemberExpression(expr: MemberExpression): String {
        with(expr){
            if(isComputed){
                return "${transpile(targetObject)}[${transpile(property)}]"
            }
            else{
                return "${transpile(targetObject)}.${transpile(property)}"
            }
        }
    }
}