package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.dsl.ScenarioContext
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileReader
import javax.script.ScriptEngineManager

class TestScenarioScript {

    private val scriptEngine by lazy {
        ScriptEngineManager().getEngineByExtension("main.kts")
    }

    @Test
    fun testInline() {

        Engine().use { engine ->
            (scriptEngine.eval(scenarioText) as ScenarioContext?)
                ?.applyScenario(engine)
            engine.stepUntil(2.0)
        }

    }

    @Test
    fun testFile() {

        Engine().use { engine ->
            (scriptEngine.eval(FileReader(scenarioFile)) as ScenarioContext?)
                ?.applyScenario(engine)
            engine.stepUntil(2.0)
        }

    }

    private companion object {
        private val cl = TestScenarioScript::class.java.classLoader
        val scenarioFile = File(cl.getResource("scenario/scenario.main.kts")!!.file.replace("%20", " "))
        val scenarioText by lazy { scenarioFile.readText() }
    }

}
