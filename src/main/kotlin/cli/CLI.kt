package cli

import InitializeWebProjectCommand
import picocli.CommandLine.Command

@Command(
    name = "example-cli",
    subcommands = [
        InitializeProjectCommand::class,
        InitializeWebProjectCommand::class
    ],
    mixinStandardHelpOptions = true,
    version = ["bosscript-cli 1.0"],
    description = ["Bosscript komandni alat."]
)
class BosscriptCLI : Runnable{
    override fun run() {
        println("BOSSCRIPT")
    }

}