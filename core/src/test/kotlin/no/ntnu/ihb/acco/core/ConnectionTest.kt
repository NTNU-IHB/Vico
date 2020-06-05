package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Test


internal class ConnectionTest {

    class ValueComponent(var value: Double = 0.0) : Component

    @Test
    fun test() {

        Engine().use { engine ->

            val sourceEntity = Entity("source").addComponent(ValueComponent(99.9))
            engine.addEntity(sourceEntity)

            val source = object : Source<Double>(sourceEntity.getComponent(ValueComponent::class.java)) {
                override fun get(): Double {
                    return sourceEntity.getComponent(ValueComponent::class.java).value
                }
            }

            val sinkEntity = Entity("sink").addComponent(ValueComponent())
            engine.addEntity(sinkEntity)

            val sink = object : Sink<Double>(sinkEntity.getComponent(ValueComponent::class.java)) {
                override fun set(value: Double) {
                    sinkEntity.getComponent(ValueComponent::class.java).value = value
                }
            }

            val connections = ConnectionManager()
            engine.addSystem(connections)
            connections.add(ScalarConnection(source, sink))

            engine.init()

            println((sink.component as ValueComponent).value)


        }

    }

}
