package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.components.PositionRefComponent
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.RealProperty
import no.ntnu.ihb.acco.core.SimulationSystem
import org.joml.Vector3d

class PositionRefSystem : SimulationSystem(
    Family.all(TransformComponent::class.java, PositionRefComponent::class.java).build()
) {

    private val tmpVector = Vector3d()
    private val tmpArray = DoubleArray(1)
    private val map: MutableMap<String, RealProperty> = mutableMapOf()

    override fun entityAdded(entity: Entity) {

        val p = entity.getComponent<PositionRefComponent>()

        p.xRef?.also { ref -> entity.getRealProperty(ref)?.also { v -> map[ref] = v } }
        p.yRef?.also { ref -> entity.getRealProperty(ref)?.also { v -> map[ref] = v } }
        p.zRef?.also { ref -> entity.getRealProperty(ref)?.also { v -> map[ref] = v } }

    }

    override fun entityRemoved(entity: Entity) {
        val p = entity.getComponent<PositionRefComponent>()

        p.xRef?.also { map.remove(it) }
        p.yRef?.also { map.remove(it) }
        p.zRef?.also { map.remove(it) }

    }

    override fun postInit() {
        update()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        update()
    }

    private fun update() {
        for (entity in entities) {

            val pRef = entity.getComponent<PositionRefComponent>()

            pRef.xRef?.also { tmpVector.x = map.getValue(it).read(tmpArray)[0] }
            pRef.yRef?.also { tmpVector.y = map.getValue(it).read(tmpArray)[0] }
            pRef.zRef?.also { tmpVector.z = map.getValue(it).read(tmpArray)[0] }

            entity.transform.setTranslation(tmpVector)

        }
    }

}
