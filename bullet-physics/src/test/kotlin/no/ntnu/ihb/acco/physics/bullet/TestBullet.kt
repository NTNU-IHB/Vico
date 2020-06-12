package no.ntnu.ihb.acco.physics.bullet

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.RealConnector
import no.ntnu.ihb.acco.core.ScalarConnection
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.physics.MotionControl
import no.ntnu.ihb.acco.physics.RigidBodyComponent
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeEngineRunner
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import kotlin.random.Random

fun main() {

    Engine(1.0 / 100).also { engine ->

        Entity("plane").also { e ->
            e.transform.setLocalTranslation(0.0, -1.0, 0.0)
            e.addComponent(RigidBodyComponent(motionControl = MotionControl.STATIC))
            val shape = BoxShape(10f, 0.1f, 10f)
            e.addComponent(ColliderComponent(shape))
            e.addComponent(GeometryComponent(shape))
            engine.addEntity(e)
        }

        for (i in 0 until 2) {
            Entity("sphere_$i").also { e ->
                e.transform.setLocalTranslation(Random.nextDouble(-1.0, 1.0), 2.0, Random.nextDouble(-1.0, 1.0))
                e.addComponent(RigidBodyComponent())
                val shape = SphereShape(0.1f)
                e.addComponent(ColliderComponent(shape))
                e.addComponent(GeometryComponent(shape))
                engine.addEntity(e)
            }
        }

        Entity("test").also { e ->
            val shape = BoxShape(0.1f)
            e.addComponent(ColliderComponent(shape))
            e.addComponent(GeometryComponent(shape).apply {
                color.set(Color.red)
            })
            engine.addEntity(e)
        }

        val source = RealConnector(engine.getEntityByName("sphere_0").transform, "localPosition")
        val sink = RealConnector(engine.getEntityByName("test").transform, "localPosition")

        engine.addConnection(ScalarConnection(source, sink))

        engine.addSystem(BulletSystem())

        JmeEngineRunner(engine).apply {
            start()
        }

    }

}
