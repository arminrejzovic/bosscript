package cli

import picocli.CommandLine.Command
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.io.path.relativeTo

@Command(name = "pokreni", description = ["Kreiraj novi Bosscript projekat"])
class RunProjectCommand : Runnable {
    override fun run() {
        val mainFile = File("projekat/main.boss")

        val projectDirectory = Paths.get("./projekat")
        val modulesPaths = Files
            .walk(projectDirectory)
            .filter{ Files.isRegularFile(it) && it.toString().endsWith(".boss") && it.name != mainFile.name}
            .toList()

        val modulesSrc = modulesPaths.map { path ->
            Files.readString(path)
        }

        modulesPaths.forEach { path ->
            println(path.relativeTo(Paths.get("projekat")))
        }

    }
}