package interpreter.stdlib

import interpreter.Broj
import interpreter.Interpreter
import interpreter.Niz
import org.junit.jupiter.api.Test
import kotlin.math.E
import kotlin.math.PI

class StdIOTest {

    @Test
    fun testMathPackageImport() {
        val src = """
            paket "IO";
            
            var fajl = datoteka("C:\Users\armin\IdeaProjects\bosscript\src\test\kotlin\interpreter\stdlib\test.txt");
            fajl.postaviTekst("EDITED WITH BOSSCRIPT
            ");
            ispis(fajl.sadrzaj);
            za svako(x od 1 do 10){
                fajl.dopisi(x);
            }
            ispis(fajl.sadrzaj);
        """.trimIndent()

        val interpreter = Interpreter()
        interpreter.evaluateProgram(src)
    }
}