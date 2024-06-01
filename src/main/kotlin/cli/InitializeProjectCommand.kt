package cli

import picocli.CommandLine.*
import java.io.File

@Command(name = "kreiraj-projekat", description = ["Kreiraj novi Bosscript projekat"])
class InitializeProjectCommand : Runnable {
    @Option(names = ["--ime"], description = ["Ime projekta"], required = true)
    lateinit var projectName: String

    @Parameters(
        index = "0",
        description = ["Folder u kojem će se kreirati projekat"],
        defaultValue = ".",
        arity = "0..1"
    )
    lateinit var targetDirectory: String

    private val defaultMainContent = """
        funkcija main(){
            ispis("Pozdrav svijetu!");
        }
    """.trimIndent()

    private val defaultTestContent = """
        paket "testovi" {moraBitiTačno};
    
        funkcija test(){
            moraBitiTačno(tačno);
        }
    """.trimIndent()

    private val tomlContent = """
        [projekat]
        program = "main.boss"
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

        val packagesDir = File(projectDir, "paketi")
        val projectFolder = File(projectDir, "projekat")
        val testsFolder = File(projectDir, "testovi")

        val mainBosscriptFile = File(projectFolder, "main.boss")
        val mainTestFile = File(testsFolder, "main.test.boss")
        val projectTomlFile = File(projectDir, "projekat.toml")

        try {
            packagesDir.mkdirs()
            projectFolder.mkdirs()
            testsFolder.mkdirs()

            mainBosscriptFile.writeText(defaultMainContent)
            mainTestFile.writeText(defaultTestContent)
            projectTomlFile.writeText(tomlContent)

            println("Projekat uspješno kreiran: ${projectDir.absolutePath}")
        } catch (e: Exception) {
            println("Greška prilikom kreiranja projekta: ${e.message}")
        }
    }
}