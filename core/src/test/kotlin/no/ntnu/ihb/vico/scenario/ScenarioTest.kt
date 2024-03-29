package no.ntnu.ihb.vico.scenario

import no.ntnu.ihb.vico.core.AbstractComponent
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.core.RealLambdaProperty
import no.ntnu.ihb.vico.dsl.ActionContext
import no.ntnu.ihb.vico.dsl.scenario
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ScenarioTest {

    private class TestComponent1 : AbstractComponent() {

        var value = 2.0

        init {
            properties.registerProperties(RealLambdaProperty(this, "value", 1,
                getter = { it[0] = value },
                setter = { value = it.first() }
            ))
        }

    }

    private class TestComponent2 : AbstractComponent() {

        var value = 3.0

        init {
            properties.registerProperties(RealLambdaProperty(this, "value", 1,
                getter = { it[0] = value },
                setter = { value = it.first() }
            ))
        }

    }

    private val scenario = scenario {

        invokeAt(1.0) {
            real("e1.testComponent1.value") *= real("e2.value")
        }

        invokeAt(50.0) {
            real("e1.testComponent1.value").set(3.0)
        }

        invokeWhen {
            `when` {
                real("e1.value") == 3.0
            }.`do` {
                real("e1.testComponent1.value").set(99)
            }
        }

    }

    @Test
    fun testScenarioDsl() {

        val baseStepSize = 0.05
        val engine = Engine(baseStepSize = baseStepSize)
        val e1 = engine.createEntity("e1", TestComponent1())
        engine.createEntity("e2", TestComponent2())

        scenario.applyScenario(engine)

        engine.stepUntil(1.0)
        Assertions.assertEquals(2.0 * 3.0, e1.get<TestComponent1>().value, 1e-6)

        engine.stepUntil(55.0)
        Assertions.assertEquals(99.0, e1.get<TestComponent1>().value, 1e-6)

    }

    @Test
    fun testDynamicInvokeAt() {

        val scenario = scenario {

            val maxRecursion = 5
            var recursionLevel = 0

            fun demo(timePoint: Double, ctx: ActionContext) {
                println("Hello at t=$timePoint")

                if (recursionLevel++ < maxRecursion) {
                    ctx.invokeAt(timePoint) {
                        val t = timePoint+1
                        demo(t, ctx)
                    }
                }
            }

            init {

                demo(5.0, this)
            }

        }

        Engine().use { engine ->

            scenario.applyScenario(engine)
            engine.stepUntil(100)

        }

    }

}
