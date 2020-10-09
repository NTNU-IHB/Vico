package no.ntnu.ihb.vico.physics.bullet

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.core.RealConnector
import no.ntnu.ihb.vico.core.ScalarConnection
import no.ntnu.ihb.vico.physics.Collider
import no.ntnu.ihb.vico.physics.MotionControl
import no.ntnu.ihb.vico.physics.RigidBodyComponent
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import org.joml.Matrix4f
import kotlin.random.Random

fun main() {

    val renderer = ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, 20f))
    }
    EngineBuilder().stepSize(1.0 / 100).renderer(renderer).build().use { engine ->

        engine.createEntity("plane").apply {
            add(Transform()).apply {
                setLocalTranslation(0.0, -1.0, 0.0)
                //localRotateX(Angle.deg(90.0))
            }
            add(RigidBodyComponent(motionControl = MotionControl.STATIC))
            val shape = BoxMesh(10f, 0.1f, 10f)
            add(Collider(shape))
            add(Geometry(shape))
        }

        for (i in 0 until 20) {
            engine.createEntity("sphere_$i").apply {
                add(Transform()).apply {
                    setLocalTranslation(
                            Random.nextDouble(-1.0, 1.0),
                            2.0,
                            Random.nextDouble(-1.0, 1.0)
                    )
                }
                add(RigidBodyComponent())
                val shape = SphereMesh(0.1f)
                add(Collider(shape))
                add(Geometry(shape))
            }
        }

        val sphere0 = engine.getEntityByName("sphere_0")

        val test = engine.createEntity("test").apply {
            add(Transform())
            val shape = BoxMesh(0.1f, 0.1f)
            add(Collider(shape))
            add(Geometry(shape).apply {
                color = ColorConstants.red
            })
        }

        val source = RealConnector(sphere0.get<Transform>(), "localPosition")
        val sink = RealConnector(test.get<Transform>(), "localPosition")

        engine.addConnection(ScalarConnection(source, sink))

        engine.addSystem(BulletSystem())
        engine.addSystem(GeometryRenderer())

        engine.runner.apply {
            startAndWait(false)
        }

    }

}
