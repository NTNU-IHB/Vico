package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.fmi4j.readReal
import org.joml.Vector3d

class SlaveTransformSystem : SimulationSystem(
    Family.all(SlaveComponent::class.java, TransformComponent::class.java, SlaveTransform::class.java).build()
) {

    private val tmp = Vector3d()

    override fun entityAdded(entity: Entity) {
        val slave = entity.getComponent(SlaveComponent::class.java)
        val slaveTransform = entity.getComponent(SlaveTransform::class.java)
        slaveTransform.xRef?.also { slave.markForReading(it) }
        slaveTransform.yRef?.also { slave.markForReading(it) }
        slaveTransform.zRef?.also { slave.markForReading(it) }
    }

    override fun step(currentTime: Double, stepSize: Double) {

        for (entity in entities) {

            val slave = entity.getComponent(SlaveComponent::class.java)
            val transform = entity.getComponent(TransformComponent::class.java)
            val slaveTransform = entity.getComponent(SlaveTransform::class.java)

            slaveTransform.xRef?.also { tmp.x = slave.readReal(it).value }
            slaveTransform.yRef?.also { tmp.y = slave.readReal(it).value }
            slaveTransform.zRef?.also { tmp.z = slave.readReal(it).value }

            transform.setTranslation(tmp)

        }
    }
}
