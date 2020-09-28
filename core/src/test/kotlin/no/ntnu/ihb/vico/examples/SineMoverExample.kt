package no.ntnu.ihb.vico.examples

import info.laht.krender.mesh.BoxMesh
import info.laht.krender.mesh.CylinderMesh
import info.laht.krender.mesh.SphereMesh
import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Matrix4f
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.sin

private data class SineMoverComponent(
        var A: Double = 1.0,
        var f: Double = 0.1,
        var phi: Double = 0.0
) : Component() {

    fun compute(t: Double) = A * sin(TWO_PHI * f * t + phi)

    private companion object {
        private const val TWO_PHI = 2 * PI
    }


}

private class SineMoverSystem : IteratingSystem(
        Family.all(Transform::class.java, SineMoverComponent::class.java).build()
) {

    private val tmp = Vector3d()

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val frame = entity.getComponent<Transform>().frame
        val sc = entity.getComponent<SineMoverComponent>()
        frame.setLocalTranslation(frame.getLocalTranslation(tmp).apply { x = sc.compute(currentTime) })

    }

}

private fun e1(engine: Engine): Entity {
    return engine.createEntity("e1").also { e ->

        e.addComponent(Transform()).apply {
            frame.setLocalTranslation(-1.0, 0.0, 0.0)
        }

        e.addComponent(Geometry(BoxMesh())).apply {
            color = ColorConstants.green
        }
        e.addComponent(SineMoverComponent())
    }
}

fun main() {

    EngineBuilder().renderer(ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, 5f))
    }).build().also { engine ->

        val e1 = e1(engine)
        engine.createEntity("e2").also { e ->

            val frame = e.addComponent(Transform()).frame

            frame.setLocalTranslation(1.0, 0.0, 0.0)

            e.addComponent(Geometry(SphereMesh()).apply {
                color = ColorConstants.yellow
            })
            e.addComponent(SineMoverComponent(f = 0.5))
            frame.setParent(e1.getComponent<Transform>().frame)
            engine.getEntityByName("e1")
        }

        engine.addSystem(SineMoverSystem())
        engine.addSystem(GeometryRenderer())

        engine.invokeAt(2.0) {
            engine.getEntityByName("e1").getComponent<Geometry>().visible = false
            engine.getEntityByName("e2").getComponent<Geometry>().wireframe = true

            engine.invokeIn(1.0) {
                engine.getEntityByName("e1").getComponent<Geometry>().visible = true
                engine.getEntityByName("e2").getComponent<Geometry>().wireframe = false
                engine.getEntityByName("e2").getComponent<Geometry>().color = ColorConstants.red
            }

        }

        engine.invokeAt(4.0) {

            engine.createEntity().apply {
                val frame = addComponent(Transform()).frame
                frame.setLocalTranslation(0.0, 2.0, 0.0)
                addComponent(Geometry(CylinderMesh(0.5f, 1f)).apply {
                    color = ColorConstants.blue
                    wireframe = true
                })
                frame.setParent(engine.getEntityByName("e2").getComponent<Transform>().frame)
            }

            engine.removeEntity(engine.getEntityByName("e1"))

        }

        engine.runner.apply {
            enableRealTimeTarget = true
            startAndWait()
        }

    }

}
