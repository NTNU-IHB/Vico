package no.ntnu.ihb.acco.scenario

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.RealLambdaProperty
import no.ntnu.ihb.acco.dsl.scenario
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ScenarioTest {

    private class TestComponent1 : Component() {

        var value = 2.0

        init {
            registerProperties(RealLambdaProperty("value", 1,
                getter = { it[0] = value },
                setter = { value = it.first() }
            ))
        }

    }

    private class TestComponent2 : Component() {

        var value = 3.0

        init {
            registerProperties(RealLambdaProperty("value", 1,
                getter = { it[0] = value },
                setter = { value = it.first() }
            ))
        }

    }

    val scenario = scenario {

        invokeAt(1.0) {
            real("e1.value") *= real("e2.value")
        }

        invokeAt(2.0) {
            real("e1.value").set(3.0)
        }

        invokeWhen {
            predicate {
                real("e1.value") == 3.0
            }.`do` {
                real("e1.value").set(99)
            }
        }

    }

    @Test
    fun testScenarioDsl() {

        val engine = Engine()
        val e1 = engine.createEntity("e1", TestComponent1())
        engine.createEntity("e2", TestComponent2())

        engine.applyScenario(scenario)

        engine.stepUntil(1.0)
        Assertions.assertEquals(2.0 * 3.0, e1.getComponent<TestComponent1>().value, 1e-6)

        engine.stepUntil(2.0)
        Assertions.assertEquals(99.0, e1.getComponent<TestComponent1>().value, 1e-6)


    }

}
