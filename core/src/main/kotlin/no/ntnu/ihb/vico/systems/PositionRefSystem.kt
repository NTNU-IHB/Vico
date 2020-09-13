package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.PositionRefComponent
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.RealProperty
import no.ntnu.ihb.vico.core.SimulationSystem
import org.joml.Vector3d

class PositionRefSystem : SimulationSystem(
    Family.all(Transform::class.java, PositionRefComponent::class.java).build()
) {

    private val tmpVector = Vector3d()
    private val tmpArray = DoubleArray(1)
    private val map: MutableMap<String, RealProperty> = mutableMapOf()

    override fun entityAdded(entity: Entity) {

        val p: PositionRefComponent = entity.getComponent()

        p.xRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }
        p.yRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }
        p.zRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }

    }

    override fun entityRemoved(entity: Entity) {

        val p: PositionRefComponent = entity.getComponent()

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

            pRef.xRef?.also { tmpVector.x = map.getValue(it).read(tmpArray).first() }
            pRef.yRef?.also { tmpVector.y = map.getValue(it).read(tmpArray).first() }
            pRef.zRef?.also { tmpVector.z = map.getValue(it).read(tmpArray).first() }

            entity.getComponent<Transform>().frame.setTranslation(tmpVector)

        }
    }

}
