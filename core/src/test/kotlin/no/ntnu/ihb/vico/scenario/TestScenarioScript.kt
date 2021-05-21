package no.ntnu.ihb.vico.scenario

import info.laht.kts.KtsScriptRunner
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.dsl.ScenarioContext
import org.junit.jupiter.api.Test
import java.io.File

class TestScenarioScript {

    @Test
    fun testInline() {

        Engine().use { engine ->
            (KtsScriptRunner().eval(scenarioText) as ScenarioContext?)
                ?.applyScenario(engine)
            engine.stepUntil(2.0)
        }

    }

    @Test
    fun testFile() {

        Engine().use { engine ->
            (KtsScriptRunner().eval(scenarioFile) as ScenarioContext?)
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
