package interpreter.packages.testlib

import interpreter.Environment
import interpreter.values.*
import org.junit.jupiter.api.Assertions

val testLib = Environment(variables = hashMapOf(
    "moraBitiTačno" to NativeFunction("moraBitiTačno"){ args ->
        if(args.size != 1 || args[0] !is Logicki){
            throw RuntimeException("Funkcija 'moraBitiTačno' očekuje 1 argument (izraz: logički)")
        }
        val arg = (args[0] as Logicki).value
        Assertions.assertTrue(arg)
        Null()
    },
    "moraBitiTacno" to NativeFunction("moraBitiTačno"){ args ->
        if(args.size != 1 || args[0] !is Logicki){
            throw RuntimeException("Funkcija 'moraBitiTačno' očekuje 1 argument (izraz: logički)")
        }
        val arg = (args[0] as Logicki).value
        Assertions.assertTrue(arg)
        Null()
    },
    "moraBitiNetačno" to NativeFunction("moraBitiNetačno"){ args ->
        if(args.size != 1 || args[0] !is Logicki){
            throw RuntimeException("Funkcija 'moraBitiNetačno' očekuje 1 argument (izraz: logički)")
        }
        val arg = (args[0] as Logicki).value
        Assertions.assertFalse(arg)
        Null()
    },
    "moraBitiNetacno" to NativeFunction("moraBitiNetačno"){ args ->
        if(args.size != 1 || args[0] !is Logicki){
            throw RuntimeException("Funkcija 'moraBitiNetačno' očekuje 1 argument (izraz: logički)")
        }
        val arg = (args[0] as Logicki).value
        Assertions.assertFalse(arg)
        Null()
    },
    "moraBitiJednako" to NativeFunction("moraBitiJednako"){ args ->
        if(args.size != 2){
            throw RuntimeException("Funkcija 'moraBitiJednako' očekuje 2 argument (očekivano: nepoznato, dobijeno: nepoznato)")
        }
        val expected = args[0]
        val actual = args[1]
        Assertions.assertEquals(expected, actual)
        Null()
    },
    "moraBitiRazličito" to NativeFunction("JSONTekst"){ args ->
        if(args.size != 2){
            throw RuntimeException("Funkcija 'moraBitiRazličito' očekuje 2 argument (očekivano: nepoznato, dobijeno: nepoznato)")
        }
        val expected = args[0]
        val actual = args[1]
        Assertions.assertNotEquals(expected, actual)
        Null()
    },
    "moraBitiRazlicito" to NativeFunction("JSONTekst"){ args ->
        if(args.size != 2){
            throw RuntimeException("Funkcija 'moraBitiRazličito' očekuje 2 argument (očekivano: nepoznato, dobijeno: nepoznato)")
        }
        val expected = args[0]
        val actual = args[1]
        Assertions.assertNotEquals(expected, actual)
        Null()
    },
    "moraBitiNedefinisano" to NativeFunction("JSONTekst"){ args ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'moraBitiNedefinisano' očekuje 1 argument (izraz: nepoznato)")
        }
        Assertions.assertTrue(args[0] is Null)
        Null()
    },
    "neSmijeBitiNedefinisano" to NativeFunction("JSONTekst"){ args ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'neSmijeBitiNedefinisano' očekuje 1 argument (izraz: nepoznato)")
        }
        Assertions.assertFalse(args[0] is Null)
        Null()
    },
    "moraIzazvatiGrešku" to ContextualNativeFunction("JSONTekst"){ args, interpreter ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'moraIzazvatiGrešku' očekuje 1 argument (izraz: nepoznato)")
        }

        if (args[0] is NativeFunction) {
            val fn = args[0] as NativeFunction

            Assertions.assertThrows(Exception::class.java) {
                fn.call(listOf())
            }
        }

        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija

            Assertions.assertThrows(Exception::class.java) {
                val functionEnv = Environment(parent = fn.parentEnv, variables = hashMapOf())
                interpreter.evaluateBlockStatement(fn.body, functionEnv)
            }
        }

        Null()
    },
    "moraIzazvatiGresku" to ContextualNativeFunction("JSONTekst"){ args, interpreter ->
        if(args.size != 1){
            throw RuntimeException("Funkcija 'moraIzazvatiGrešku' očekuje 1 argument (izraz: nepoznato)")
        }

        if (args[0] is NativeFunction) {
            val fn = args[0] as NativeFunction

            Assertions.assertThrows(Exception::class.java) {
                fn.call(listOf())
            }
        }

        else if (args[0] is Funkcija) {
            val fn = args[0] as Funkcija

            Assertions.assertThrows(Exception::class.java) {
                val functionEnv = Environment(parent = fn.parentEnv, variables = hashMapOf())
                interpreter.evaluateBlockStatement(fn.body, functionEnv)
            }
        }

        Null()
    },
))