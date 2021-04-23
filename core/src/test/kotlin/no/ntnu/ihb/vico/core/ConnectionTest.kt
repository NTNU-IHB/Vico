package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class ConnectionTest {

    class ValueComponent(
        var value: Double = 0.0
    ) : AbstractComponent() {

        init {
            properties.registerProperties(
                RealScalarProperty(
                    this,
                    "value",
                    getter = { value },
                    setter = { value = it }
                )
            )
        }

    }

    @Test
    fun test() {

        val someValue = 99.0

        Engine().use { engine ->

            val sourceEntity = engine.createEntity("source", ValueComponent(someValue))
            val sinkEntity = engine.createEntity("sink", ValueComponent())

            val sourceConnector = RealConnector(
                sourceEntity.get<ValueComponent>(), "value"
            )

            val sinkConnector = RealConnector(
                sinkEntity.get<ValueComponent>(), "value"
            )

            engine.addConnection(RealConnection(sourceConnector, sinkConnector))

            engine.init()

            Assertions.assertEquals(someValue, (sinkConnector.component as ValueComponent).value)

        }

    }

}
