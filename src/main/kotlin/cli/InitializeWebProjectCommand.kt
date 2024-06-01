
import cli.defaultTestContent
import cli.webMainContent
import cli.webTomlContent
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File

@Command(name = "kreiraj-web-projekat", description = ["Kreiraj novi Bosscript web projekat"])
class InitializeWebProjectCommand : Runnable {
    @Parameters(
        index = "0",
        description = ["Ime projekta"],
        arity = "1",
        paramLabel = "IME_PROJEKTA"
    )
    lateinit var projectName: String

    @Parameters(
        index = "1",
        description = ["Folder u kojem će se kreirati projekat"],
        defaultValue = ".",
        arity = "0..1",
        paramLabel = "FOLDER"
    )
    lateinit var targetDirectory: String


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

            mainBosscriptFile.writeText(webMainContent)
            mainTestFile.writeText(defaultTestContent)
            projectTomlFile.writeText(webTomlContent)

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