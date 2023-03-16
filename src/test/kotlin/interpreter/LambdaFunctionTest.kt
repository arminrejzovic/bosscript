package interpreter

import BlockStatement
import CallExpression
import FunctionDeclaration
import FunctionExpression
import FunctionParameter
import Identifier
import ObjectLiteral
import Property
import ReturnStatement
import TypeAnnotation
import VariableDeclaration
import VariableStatement
import org.junit.jupiter.api.Test
import udemy.*
import udemy.Function

class LambdaFunctionTest {
    @Test
    fun testFunctionAssignedToVar() {
        val src = """
            var xf = funkcija(){};
            xf;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Function(
                name = "",
                params = arrayListOf(),
                returnType = null,
                body = BlockStatement(
                    body = arrayListOf()
                ),
                parentEnv = null
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testFullLambda() {
        val src = """
            var xf = funkcija(a:broj, b:logicki): broj{
                vrati a;
            };
            xf;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = arrayListOf(
            Null(),
            Function(
                name = "",
                params = arrayListOf(
                    FunctionParameter(
                        identifier = Identifier(symbol = "a"),
                        type = TypeAnnotation(
                            typeName = "broj"
                        )
                    ),
                    FunctionParameter(
                        identifier = Identifier(symbol = "b"),
                        type = TypeAnnotation(
                            typeName = "logicki"
                        )
                    )
                ),
                returnType = TypeAnnotation(
                    typeName = "broj"
                ),
                body = BlockStatement(
                    body = arrayListOf(
                        ReturnStatement(
                            argument = Identifier(symbol = "a")
                        )
                    )
                ),
                parentEnv = null
            )
        )

        assert(result == expectedResult)
    }

    @Test
    fun testLambdaAsArg() {
        val src = """
            funkcija onClick(cb){
                vrati "onClick called";
            }
            onClick(funkcija(){});
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Text(
            value = "onClick called"
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testLambdaAsObjectProp() {
        val src = """
            var t = {
                a: funkcija(){},
                b: 10
            };
            ispis(t);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

//        val expectedResult = arrayListOf(
//            Text(
//                value = "Test"
//            )
//        )
//
//        assert(result == expectedResult)
        println(result)
    }
}