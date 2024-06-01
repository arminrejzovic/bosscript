package cli

val defaultMainContent = """
        funkcija main(){
            ispis("Pozdrav svijetu!");
        }
""".trimIndent()

val webMainContent = """
        model Pozdrav < BosscriptWebKomponenta {
        
            konstruktor() {
                prototip();
            }
            
            javno {
                var ${'$'}ime = "Default"; 
                
                var stil = css(`
                    h1 {
                        color: blue
                    }
                `);
                
                funkcija render(){
                    vrati html(`
                        <div>
                            <h1>Pozdrav, ${'$'}{ime}!</h1>
                        </div>
                    `);
                }
            }
        }
""".trimIndent()

val defaultTestContent = """
        paket "testovi" {moraBitiTačno};
    
        funkcija test(){
            moraBitiTačno(tačno);
        }
""".trimIndent()

val defaultTomlContent = """
        [projekat]
        program = "main.boss"
""".trimIndent()

val webTomlContent = """
        [web]
        index = 'projekat/index.html'
        cilj = 'js'
""".trimIndent()

