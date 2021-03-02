package no.ntnu.ihb.vico

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.CylinderMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Matrix4f
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.sin
import kotlin.system.exitProcess

private data class SineMoverComponent(
    var A: Double = 1.0,
    var f: Double = 0.1,
    var phi: Double = 0.0
) : Component {

    val axis = Vector3d(0.0, 1.0, 0.0)
    lateinit var initialPosition: Vector3d

    fun compute(t: Double) = A * sin(TWO_PHI * f * t + phi)

    private companion object {
        private const val TWO_PHI = 2 * PI
    }

}

private class SineMoverSystem : IteratingSystem(
    Family.all(Transform::class.java, SineMoverComponent::class.java).build()
) {

    private val tmp = Vector3d()

    override fun entityAdded(entity: Entity) {
        val transform = entity.get<Transform>()
        val sc = entity.get<SineMoverComponent>()
        sc.initialPosition = transform.getLocalTranslation(Vector3d())
    }

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val transform = entity.get<Transform>()
        val sc = entity.get<SineMoverComponent>()
        val value = sc.axis.mul(sc.compute(currentTime), tmp)
        transform.setLocalTranslation(sc.initialPosition.add(value, tmp))

    }

}

fun main() {

    EngineBuilder().build().use { engine ->

        val e1 = engine.createEntity("e1")
        e1.add<Transform>()
        e1.add(Geometry(
            BoxMesh(1f, 2f, 1f),
            Matrix4f().setTranslation(5f, 0f, 0f)
        ).apply {
            color = ColorConstants.blue
            wireframe = true
        })
        e1.add<SineMoverComponent>()

        val e2 = engine.createEntity("e2")
        e2.add<Transform>()
        e2.add(Geometry(SphereMesh(0.5f)).apply {
            color = ColorConstants.yellow
            opacity = 0.8f
        })
        e2.add<SineMoverComponent>().apply {
            axis.set(0.0, 0.0, 1.0).normalize()
        }

        val e3 = engine.createEntity("e3")
        e3.add<Transform>().apply {
            setLocalTranslation(-5.0, 0.0, 0.0)
        }
        e3.add(Geometry(CylinderMesh(0.5f, 1f)).apply {
            color = ColorConstants.green
            opacity = 0.5f
        })
        e3.add<SineMoverComponent>().apply {
            axis.set(1.0, 1.0, 1.0).normalize()
        }

//        val e4 = engine.createEntity("e4")
//        e4.add<Transform>().apply {
//            setLocalTranslation(-5.0, 0.0, 0.0)
//        }
//        val mesh = ObjLoader().load(File("examples/gunnerus/obj/Gunnerus.obj"))
//        e4.add(Geometry(mesh))

        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

        exitProcess(0)

    }

}
