package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.dsl.ScenarioContext
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import javax.script.ScriptEngineManager

private val isEnvironmentSetup = AtomicBoolean(false)

private val scriptEngine
    get() = ScriptEngineManager().getEngineByExtension("main.kts")


private fun setupScriptingEnvironment() {
    if (!isEnvironmentSetup.getAndSet(true)) {
        System.setProperty("idea.io.use.nio2", "true")
    }
}

fun parseScenario(scriptFile: File): ScenarioContext? {
    return parseScenario(scriptFile.readText())
}

fun parseScenario(script: String): ScenarioContext? {

    setupScriptingEnvironment()

    return try {
        scriptEngine.let {
            it.eval(script) as ScenarioContext
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

}
