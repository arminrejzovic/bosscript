package interpreter

import org.junit.jupiter.api.Test
import udemy.Interpreter
import udemy.Number
import udemy.Text

class MemberExpressionsTest {
    @Test
    fun testSimpleMember() {
        val src = """
            var x = "Str";
            x.duzina;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
            value = 3.0
        )

        assert(result.last() == expectedResult)
    }

    @Test
    fun testMemberOfObjectMember() {
        val src = """
            var x = {
                a: "Str"
            };
            x.a.duzina;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Number(
            value = 3.0
        )

        println(result.last())
        assert(result.last() == expectedResult)
    }

    @Test
    fun testBuiltinFunctionMember() {
        val src = """
            var x = {
                a: "STR"
            };
            x.a.malaSlova();
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Text(
            value = "str"
        )

        println(result.last())
        assert(result.last() == expectedResult)
    }

    @Test
    fun testMemberAssignment() {
        val src = """
            var x = {
                a: "STR"
            };
            ispis("Prije", x);
            x.a = "Hello";
            ispis("Poslije", x);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        println(result)
        //assert(result.last() == expectedResult)
    }

    @Test
    fun testNestedMemberAssignment() {
        val src = """
            var x = {
                y: {
                    z: "str"
                }
            };
            ispis("Prije", x);
            x.y.z = {a: "Hello"};
            ispis("Poslije", x);
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        println(result)
        //assert(result.last() == expectedResult)
    }

    @Test
    fun testNullMemberAccess() {
        val src = """
            var x = nedefinisano;
            x.proto;
        """.trimIndent()

        val interpreter = Interpreter()
        var error = ""
        val expectedError = "Null has no properties"

        try {
            interpreter.evaluateProgram(src)
        } catch (e: Exception){
            error = e.message ?: ""
        }

        assert(error == expectedError)
    }

    @Test
    fun testNonExistingPropAssignmentOnObject() {
        val src = """
            var x = {};
            x.a = "A";
            ispis(x);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)

        //assert(error == expectedError)
    }

    @Test
    fun testNonExistingPropAssignmentOnNonObject() {
        val src = """
            var x = 33;
            x.a = "A";
            ispis(x);
        """.trimIndent()

        val interpreter = Interpreter()
        var error = ""
        val expectedError = "Number is not an Object"
        try {
            interpreter.evaluateProgram(src)
        } catch (e: Exception){
            error = e.message ?: ""
        }

        assert(error == expectedError)
    }

    @Test
    fun testComputedPropertyAccess() {
        val src = """
            var x = {
                a: 10,
                b: 3
            };
            x["a"];
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Number(
            value = 10.0
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }

    @Test
    fun testNonExistentComputedPropertyAccess() {
        val src = """
            var x = {
                a: 10,
                b: 3
            };
            x["c"];
        """.trimIndent()

        val interpreter = Interpreter()
        var error = ""
        val expectedError = "Property c does not exist on object"
        try {
            interpreter.evaluateProgram(src)
        } catch (e: Exception){
            error = e.message ?: ""
        }

        assert(error == expectedError)
    }

    @Test
    fun testComputedPropertyAssignment() {
        val src = """
            var x = {
                a: 10,
                b: 3
            };
            x["a"] = 99;
            x["a"];
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Number(
            value = 99.0
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }

    @Test
    fun testNonExistingComputedPropertyAssignment() {
        val src = """
            var x = {
                a: 10,
                b: 3
            };
            x["c"] = 99;
            x.c;
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Number(
            value = 99.0
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }

    @Test
    fun testArrayIndex() {
        val src = """
            var x = [1,2,3,4,5,6];
            x[2];
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Number(
            value = 3.0
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }

    @Test
    fun testArrayAssignByIndex() {
        val src = """
            var x = [1,2,3,4,5,6];
            x[2] = 99;
            ispis(x);
            x[2];
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Number(
            value = 99.0
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }

    @Test
    fun test2DArray() {
        val src = """
            var x = [
                ["A", "B", "C"],
                ["D", "E", "F"],
                ["G", "H", "I"]
            ];
            
            x[1][0];
        """.trimIndent()

        val interpreter = Interpreter()
        val expectedResult = Text(
            value = "D"
        )

        val result = interpreter.evaluateProgram(src).last()

        assert(expectedResult == result)
    }
}