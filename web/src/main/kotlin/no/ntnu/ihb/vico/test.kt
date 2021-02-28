package no.ntnu.ihb.vico

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.sin

private data class SineMoverComponent(
    var A: Double = 1.0,
    var f: Double = 0.1,
    var phi: Double = 0.0
) : Component {

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

        val transform = entity.get<Transform>()
        val sc = entity.get<SineMoverComponent>()
        transform.setLocalTranslation(transform.getLocalTranslation(tmp).apply { x = sc.compute(currentTime) })

    }

}

fun main() {

    EngineBuilder().build().use { engine ->

        val e1 = engine.createEntity("e1")
        e1.add<Transform>()
        e1.add(Geometry(BoxMesh()).apply {
            color = ColorConstants.blue
        })
        e1.add<SineMoverComponent>()

        engine.addSystem(GeometryRenderer())
        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

    }

}
