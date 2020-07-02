package no.ntnu.ihb.vico.cli.commands

import no.ntnu.ihb.vico.cli.invokeScript
import picocli.CommandLine
import java.io.File

@CommandLine.Command(name = "run", description = ["Run a Vico script"])
class Run : Runnable {

    @CommandLine.Parameters(
        arity = "1",
        paramLabel = "SCRIPT",
        description = ["Path to script"]
    )
    private lateinit var scriptFile: File

    override fun run() {
        invokeScript(scriptFile)
    }

}
