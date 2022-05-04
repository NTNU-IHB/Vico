package no.ntnu.ihb.vico.cli.commands

import picocli.CommandLine
import java.io.File
import java.io.FileReader
import java.net.URLClassLoader
import javax.script.ScriptEngineManager
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

        val scriptEngine = ScriptEngineManager().getEngineByExtension("main.kts")

        val t = thread(true, contextClassLoader = URLClassLoader(arrayOf(), null)) {
            val eval = scriptEngine.eval(FileReader(script))
            eval?.also { result ->
                println(result)
            }
        }
        t.join()

    }

}
