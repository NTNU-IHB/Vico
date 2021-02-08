package no.ntnu.ihb.vico.cli.commands

import info.laht.kts.KtsScriptRunner
import picocli.CommandLine
import java.io.File
import java.net.URLClassLoader
import kotlin.concurrent.thread

@CommandLine.Command(name = "eval", description = ["Evaluate kotlin script"])
class Eval : Runnable {

    @CommandLine.Parameters(
        arity = "1",
        paramLabel = "SCRIPT",
        description = ["Path to the script to evaluate"]
    )
    private lateinit var script: File

    override fun run() {

        require(script.exists())
        require(script.extension == "kts")

        val cacheDir = File(".kts").apply { mkdir() }
        thread(true, contextClassLoader = URLClassLoader(arrayOf(), null)) {
            KtsScriptRunner(cacheDir).eval(script)?.also { result ->
                println(result)
            }
        }

    }

}
