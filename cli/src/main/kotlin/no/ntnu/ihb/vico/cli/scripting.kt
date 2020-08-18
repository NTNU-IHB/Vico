package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.vico.dsl.ScenarioContext
import java.io.File
import javax.script.ScriptEngineManager

private val validExtensions = listOf("kts")

private fun validateFile(scriptFile: File): Boolean {
    val ext = scriptFile.extension
    if (ext !in validExtensions) {
        System.err.println(
            "FATAL: Extension '$ext' is not a valid extension for Vico scripts. " +
                    "Valid extensions are: $validExtensions"
        )
        return false
    }
    return true
}

private val scriptEngine
    get() = ScriptEngineManager().getEngineByExtension("kts")


private fun setupScriptingEnvironment() {
    System.setProperty("idea.io.use.nio2", "true")
}

fun parseScenario(scriptFile: File): ScenarioContext? {

    if (!validateFile(scriptFile)) return null
    setupScriptingEnvironment()

    val imports = """
        import no.ntnu.ihb.vico.scenario.*
    """.trimIndent()

    return try {
        scriptEngine.let {
            val scriptContent = imports + scriptFile.readText()
            it.eval(scriptContent) as ScenarioContext
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

}
