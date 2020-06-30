package no.ntnu.ihb.vico.cli.commands

import picocli.CommandLine
import java.io.File
import javax.script.ScriptEngineManager

private val validExtensions = listOf("kts", "vico")

@CommandLine.Command(name = "run", description = ["Run a Vico script"])
class Run : Runnable {

    @CommandLine.Parameters(
        arity = "1",
        paramLabel = "SCRIPT",
        description = ["Path to Vico script"]
    )
    private lateinit var script: File

    override fun run() {

        val ext = script.extension
        if (ext !in validExtensions) {
            System.err.println(
                "FATAL: Extension '$ext' is not a valid extension for Vico scripts. " +
                        "Valid ones are: $validExtensions"
            )
            return
        }

        System.setProperty("idea.io.use.nio2", "true")

        with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval(script.readText())
        }
    }

}
