import interpreter.Environment
import interpreter.Interpreter
import interpreter.Objekat
import interpreter.Tekst

fun main() {
    val src = """
          funkcija safet(){
               var pridjevi = ["ljepotan", "grmalj", "smeće", "yeti", "smrad"];
               var rand = nasumicni(pridjevi.duzina);
               ispis("Safet je:", pridjevi[rand]);
          }
          
           var run = tacno;
          radi {
                safet();
                var prompt = unos("Zelite li prekinuti?");
                ako(prompt == "da"){
                    run = netacno;
                }
          } dok(run);  
        """.trimIndent()

    val interpreter = Interpreter()
    interpreter.evaluateProgram(src)
}