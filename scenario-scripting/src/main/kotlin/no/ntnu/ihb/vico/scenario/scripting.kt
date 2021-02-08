package no.ntnu.ihb.vico.scenario

import info.laht.kts.KtsScriptRunner
import no.ntnu.ihb.vico.dsl.ScenarioContext
import java.io.File

@JvmOverloads
fun parseScenario(scriptFile: File, cacheDir: File? = null): ScenarioContext? {
    return try {
        KtsScriptRunner(cacheDir).eval(scriptFile) as ScenarioContext?
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

@JvmOverloads
fun parseScenario(script: String, cacheDir: File? = null): ScenarioContext? {
    return try {
        KtsScriptRunner(cacheDir).eval(script) as ScenarioContext?
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}
