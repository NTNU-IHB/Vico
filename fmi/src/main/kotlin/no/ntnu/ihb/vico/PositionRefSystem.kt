package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.fmi4j.readReal
import org.joml.Vector3d

class PositionRefSystem : SimulationSystem(
    Family.all(SlaveComponent::class.java, TransformComponent::class.java, PositionRefComponent::class.java).build()
) {

    private val tmp = Vector3d()

    override fun postInit() {
        update()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        update()
    }

    private fun update() {
        for (entity in entities) {

            val slave = entity.getComponent<SlaveComponent>()
            val slaveTransform = entity.getComponent<PositionRefComponent>()

            slaveTransform.xRef?.also { tmp.x = slave.readReal(it).value }
            slaveTransform.yRef?.also { tmp.y = slave.readReal(it).value }
            slaveTransform.zRef?.also { tmp.z = slave.readReal(it).value }

            entity.transform.setTranslation(tmp)

        }
    }


}
