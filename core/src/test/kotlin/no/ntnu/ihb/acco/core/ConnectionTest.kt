package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class ConnectionTest {

    class ValueComponent(
        var value: Double = 0.0
    ) : Component() {

        init {
            registerProperties(
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

            val sourceEntity = Entity("source").apply {
                addComponent(ValueComponent(someValue))
            }

            val sinkEntity = Entity("sink").apply {
                addComponent(ValueComponent())
            }

            val sourceConnector = RealConnector(
                sourceEntity.getComponent<ValueComponent>(), "value"
            )

            val sinkConnector = RealConnector(
                sinkEntity.getComponent<ValueComponent>(), "value"
            )

            engine.addEntity(sourceEntity)
            engine.addEntity(sinkEntity)

            engine.addConnection(ScalarConnection(sourceConnector, sinkConnector))

            engine.init()

            Assertions.assertEquals(someValue, (sinkConnector.component as ValueComponent).value)

        }

    }

}
