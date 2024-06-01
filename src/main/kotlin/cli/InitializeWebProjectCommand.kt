import picocli.CommandLine.*
import java.io.File

@Command(name = "kreiraj-web-projekat", description = ["Kreiraj novi Bosscript web projekat"])
class InitializeWebProjectCommand : Runnable {
    @Option(names = ["--ime"], description = ["Ime projekta"], required = true)
    lateinit var projectName: String

    @Parameters(index = "0", description = ["Folder u kojem će se kreirati projekat"], defaultValue = ".", arity = "0..1")
    lateinit var targetDirectory: String

    private val defaultMainContent = """
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

    private val defaultTestContent = """
        paket "testovi" {moraBitiTačno};
    
        funkcija test(){
            moraBitiTačno(tačno);
        }
    """.trimIndent()

    private val tomlContent = """
        [web]
        index = 'projekat/index.html'
        cilj = 'js'
    """.trimIndent()


    override fun run() {
        val targetDir = File(targetDirectory)

        if (!targetDir.exists() || !targetDir.isDirectory) {
            println("Navedeni folder ne postoji: $targetDirectory")
            return
        }

        val projectDir = File(targetDir, projectName)
        if (projectDir.exists()) {
            println("Projekat već postoji: ${projectDir.absolutePath}")
            return
        }

        val jsFolder = File(projectDir, "js")
        val packagesFolder = File(projectDir, "paketi")
        val projectFolder = File(projectDir, "projekat")
        val componentsFolder = File(projectFolder, "komponente")
        val testsFolder = File(projectDir, "testovi")

        val mainBosscriptFile = File(componentsFolder, "main.boss")
        val mainTestFile = File(testsFolder, "main.test.boss")
        val projectTomlFile = File(projectDir, "projekat.toml")
        val indexHtml = File(projectFolder, "index.html")

        try {
            jsFolder.mkdirs()
            packagesFolder.mkdirs()
            projectFolder.mkdirs()
            componentsFolder.mkdirs()
            testsFolder.mkdirs()

            mainBosscriptFile.writeText(defaultMainContent)
            mainTestFile.writeText(defaultTestContent)
            projectTomlFile.writeText(tomlContent)

            val defaultHtmlContent = """
                <!DOCTYPE html>
                <html lang="ba">
                <head>
                    <meta charset="UTF-8">
                    <title>${projectName}</title>
                </head>
                <body>
                    <pozdrav ime="Bosscript"></pozdrav>
                </body>
                </html>
            """.trimIndent()

            indexHtml.writeText(defaultHtmlContent)

            println("Projekat uspješno kreiran: ${projectDir.absolutePath}")
        }
        catch (e: Exception) {
            println("Greška prilikom kreiranja projekta: ${e.message}")
        }
    }
}