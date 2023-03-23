package interpreter.stdlib

import interpreter.Broj
import interpreter.Interpreter
import org.junit.jupiter.api.Test
import kotlin.math.PI

class StdMathTest {

    @Test
    fun testMathPackageImport() {
        val src = """
            paket "matematika";
            
            ispis(apsolutnaVrijednost(-3));
            ispis(apsolutnaVrijednost(3));
            ispis(apsolutnaVrijednost(0));
            
            ispis(pi);
            
            ispis(arcsin(pi/2));
            ispis(sin(pi/2));
            ispis(arccos(pi/2));
            ispis(cos(pi/2));
            ispis(tg(pi/2));
            ispis(arctg(pi/2));
            
            ispis(korijen(144));
            
            ispis(e);
            
            ispis(exp(3));
            
            ispis(ln(3));
            ispis(log(3, 2));
            ispis(log10(3));
            ispis(log2(3));
            
            ispis(max(10, 66));
            ispis(min(10, 66));
            
            ispis(zaokruzi(6.6969696));
            
            ispis(kubniKorijen(8));
            
            ispis(beskonacno);
            ispis(minusBeskonacno);
            
            pi;
        """.trimIndent()

        val interpreter = Interpreter()
        val result = interpreter.evaluateProgram(src)

        val expectedResult = Broj(
            value = PI
        )

        assert(result.last() == expectedResult)
    }
}