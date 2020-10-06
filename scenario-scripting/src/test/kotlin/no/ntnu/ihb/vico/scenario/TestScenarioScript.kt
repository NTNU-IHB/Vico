package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.core.EngineBuilder
import org.junit.jupiter.api.Test

internal class TestScenarioScript {

    @Test
    fun test() {

        val scenario = """

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

}
