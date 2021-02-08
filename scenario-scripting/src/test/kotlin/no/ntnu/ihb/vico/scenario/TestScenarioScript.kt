package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.core.EngineBuilder
import org.junit.jupiter.api.Test
import java.io.File

internal class TestScenarioScript {

    @Test
    fun testInline() {

        EngineBuilder().build().use { engine ->
            parseScenario(scenarioText)?.applyScenario(engine)
            engine.stepUntil(2.0)
        }

    }

    @Test
    fun testFile() {

        EngineBuilder().build().use { engine ->
            parseScenario(scenarioFile)?.applyScenario(engine)
            engine.stepUntil(2.0)
        }

    }

    private companion object {
        private val cl = TestScenarioScript::class.java.classLoader
        val scenarioFile = File(cl.getResource("scenario/testScenario.main.kts")!!.file)
        val scenarioText by lazy { scenarioFile.readText() }
    }

}
