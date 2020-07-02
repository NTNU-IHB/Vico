package no.ntnu.ihb.vico.cli

import no.ntnu.ihb.acco.scenario.Scenario
import java.io.File
import javax.script.ScriptEngineManager

private val validExtensions = listOf("kts", "vico")

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

fun invokeScript(scriptFile: File) {

    if (!validateFile(scriptFile)) return
    setupScriptingEnvironment()

    val imports = """
        
        import no.ntnu.ihb.acco.core.*
        import no.ntnu.ihb.acco.components.*
        
        import no.ntnu.ihb.acco.chart.*
        
        import no.ntnu.ihb.acco.render.*
        import no.ntnu.ihb.acco.render.shape.*
        import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
        
        import no.ntnu.ihb.acco.physics.*
        import no.ntnu.ihb.acco.physics.bullet.BulletSystem
        
    """.trimIndent()

    scriptEngine.apply {
        val scriptContent = scriptFile.readLines().toMutableList()
        val insertionPoint = if (scriptContent[0].startsWith("#!")) 1 else 0
        scriptContent.add(insertionPoint, imports)
        eval(scriptContent.joinToString("\n"))
    }

}

fun parseScenario(scriptFile: File): Scenario? {

    if (!validateFile(scriptFile)) return null
    setupScriptingEnvironment()

    val imports = """
        import no.ntnu.ihb.acco.scenario.*
    """.trimIndent()

    return try {
        scriptEngine.let {
            val scriptContent = imports + scriptFile.readText()
            it.eval(scriptContent) as Scenario
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

}
