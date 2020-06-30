package no.ntnu.ihb.acco.scenario

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.RealLambdaProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ScenarioTest {

    private class TestComponent : Component() {

        var value = 99.0

        init {
            registerProperties(RealLambdaProperty("value", 1,
                getter = { it[0] = value },
                setter = { value = it.first() }
            ))
        }

    }

    @Test
    fun testScenarioDsl() {

        val engine = Engine()
        val e = engine.createEntity("e1", TestComponent())

        engine.applyScenario(scenario {

            invokeAt(1.0) {
                real("e1.value") *= 2
            }

        }
        )

        engine.stepUntil(2.0)

        Assertions.assertEquals(2.0 * 99.0, e.getComponent<TestComponent>().value, 1e-6)

        /* assertEquals(2, scenario.timedActions.size)
         assertEquals(1, scenario.predicateActions.size)
         assertEquals(90.0, scenario.endTime?.toDouble() ?: 0.0, 1e-6)*/

    }

}
