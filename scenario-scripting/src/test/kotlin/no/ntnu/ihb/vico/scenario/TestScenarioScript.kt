package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.core.EngineBuilder
import org.junit.jupiter.api.Test
import java.io.File

internal class TestScenarioScript {

    @Test
    fun testInline() {

        val scenario = """

            import no.ntnu.ihb.vico.dsl.*

            scenario {
            
                invokeAt(1.0) {
                    println("Hello from script")
                }
            
            }
            
        """.trimIndent()

        EngineBuilder().build().use { engine ->

            parseScenario(scenario)?.applyScenario(engine)

            engine.stepUntil(2.0)

        }

    }

    @Test
    fun testFile() {

        val scenario = File(javaClass.classLoader.getResource("scenario/testScenario.main.kts")!!.file)

        EngineBuilder().build().use { engine ->

            parseScenario(scenario)?.applyScenario(engine)

            engine.stepUntil(2.0)

        }

    }

}
