package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class ConnectionTest {

    class ValueComponent(var value: Double = 0.0) : Component() {
        init {
            registerVariable("value", RealLambdaVar(1,
                getter = { it[0] = value },
                setter = { value = it[0] }
            ))
        }
    }

    @Test
    fun test() {

        val someValue = 99.0

        Engine().use { engine ->

            val sourceEntity = Entity("source").addComponent(ValueComponent(someValue))
            engine.addEntity(sourceEntity)

            val sourceConnector = RealConnector(
                sourceEntity.getComponent(ValueComponent::class.java), "value"
            )

            val sinkEntity = Entity("sink").addComponent(ValueComponent())
            engine.addEntity(sinkEntity)

            val sinkConnector = RealConnector(
                sinkEntity.getComponent(ValueComponent::class.java), "value"
            )

            val connections = ConnectionManager()
            engine.addSystem(connections)
            connections.addConnection(ScalarConnection(sourceConnector, sinkConnector))

            engine.init()

            Assertions.assertEquals(someValue, (sinkConnector.component as ValueComponent).value)


        }

    }

}
