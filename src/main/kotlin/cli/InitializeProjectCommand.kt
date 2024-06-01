package cli

import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File

@Command(name = "kreiraj-projekat", description = ["Kreiraj novi Bosscript projekat"])
class InitializeProjectCommand : Runnable {
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
            projectTomlFile.writeText(defaultTomlContent)

            println("Projekat uspješno kreiran: ${projectDir.absolutePath}")
        } catch (e: Exception) {
            println("Greška prilikom kreiranja projekta: ${e.message}")
        }
    }
}