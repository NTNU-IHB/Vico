package no.ntnu.ihb.vico.web

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.systems.IteratingSystem
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.sin

data class SineMoverComponent(
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

class SineMoverSystem : IteratingSystem(
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
