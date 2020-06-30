package no.ntnu.ihb.acco.scenario

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ScenarioTest {

    @Test
    fun testScenarioDsl() {

        val scenario = scenario {
            endTime = 90.0

            invokeAt(5.0) {
                +"myVar".add(6.0)
                +"someVar".div(1.0)
            }
            invokeWhen("%iterations% > 6") {
                +"myVar".assign(5.0)
            }
        }

        assertEquals(2, scenario.timedActions.size)
        assertEquals(1, scenario.predicateActions.size)
        assertEquals(90.0, scenario.endTime?.toDouble() ?: 0.0, 1e-6)

    }

}
