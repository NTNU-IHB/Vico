package no.ntnu.ihb.acco.physics.bullet

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.RealConnector
import no.ntnu.ihb.acco.core.ScalarConnection
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.physics.MotionControl
import no.ntnu.ihb.acco.physics.RigidBodyComponent
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import kotlin.random.Random

fun main() {

    Engine(1.0 / 100).use { engine ->

        engine.createEntity("plane").apply {
            val t = addComponent(TransformComponent())
            t.setLocalTranslation(0.0, -1.0, 0.0)
            addComponent(RigidBodyComponent(motionControl = MotionControl.STATIC))
            val shape = BoxShape(10.0, 0.1, 10.0)
            addComponent(ColliderComponent(shape))
            addComponent(GeometryComponent(shape))
        }

        for (i in 0 until 20) {
            engine.createEntity("sphere_$i").apply {
                addComponent(TransformComponent()).apply {
                    setLocalTranslation(
                        Random.nextDouble(-1.0, 1.0),
                        2.0,
                        Random.nextDouble(-1.0, 1.0)
                    )
                }
                addComponent(RigidBodyComponent())
                val shape = SphereShape(0.1)
                addComponent(ColliderComponent(shape))
                addComponent(GeometryComponent(shape))
            }
        }

        val sphere0 = engine.getEntityByName("sphere_0")

        val test = engine.createEntity("test").apply {
            addComponent(TransformComponent())
            val shape = BoxShape(0.1)
            addComponent(ColliderComponent(shape))
            addComponent(GeometryComponent(shape).apply {
                setColor(Color.red)
            })
        }

        val source = RealConnector(sphere0.getComponent<TransformComponent>(), "localPosition")
        val sink = RealConnector(test.getComponent<TransformComponent>(), "localPosition")

        engine.addConnection(ScalarConnection(source, sink))

        engine.addSystem(BulletSystem())
        engine.addSystem(JmeRenderSystem())

        engine.runner.apply {
            startAndWait(true)
        }

    }

}
