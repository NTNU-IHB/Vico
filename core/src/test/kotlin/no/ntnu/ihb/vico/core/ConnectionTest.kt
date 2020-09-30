package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class ConnectionTest {

    class ValueComponent(
            var value: Double = 0.0
    ) : AbstractComponent() {

        init {
            properties.registerProperties(
                    RealLambdaProperty("value", 1,
                            getter = { it[0] = value },
                            setter = { value = it[0] }
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
                sourceEntity.getComponent<ValueComponent>(), "value"
            )

            val sinkConnector = RealConnector(
                sinkEntity.getComponent<ValueComponent>(), "value"
            )

            engine.addConnection(ScalarConnection(sourceConnector, sinkConnector))

            engine.init()

            Assertions.assertEquals(someValue, (sinkConnector.component as ValueComponent).value)

        }

    }

}
