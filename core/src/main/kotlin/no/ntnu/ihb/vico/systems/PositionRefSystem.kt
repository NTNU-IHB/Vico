package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.RealProperty
import no.ntnu.ihb.vico.core.SimulationSystem
import org.joml.Vector3d

class PositionRefSystem : SimulationSystem(
    Family.all(Transform::class.java, PositionRef::class.java).build()
) {

    private val tmpVector = Vector3d()
    private val tmpArray = DoubleArray(1)
    private val map: MutableMap<String, RealProperty> = mutableMapOf()

    override fun entityAdded(entity: Entity) {

        val p: PositionRef = entity.get()

        p.xRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }
        p.yRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }
        p.zRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }

    }

    override fun entityRemoved(entity: Entity) {

        val p: PositionRef = entity.get()

        p.xRef?.also { map.remove(it.name) }
        p.yRef?.also { map.remove(it.name) }
        p.zRef?.also { map.remove(it.name) }

    }

    override fun postInit() {
        update()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        update()
    }

    private fun update() {
        for (entity in entities) {

            val pRef: PositionRef = entity.get()

            pRef.xRef?.also {
                val read = map.getValue(it.name).read(tmpArray).first()
                tmpVector.x = it.linearTransform?.invoke(read) ?: read
            }
            pRef.yRef?.also {
                val read = map.getValue(it.name).read(tmpArray).first()
                tmpVector.y = it.linearTransform?.invoke(read) ?: read
            }
            pRef.zRef?.also {
                val read = map.getValue(it.name).read(tmpArray).first()
                tmpVector.z = it.linearTransform?.invoke(read) ?: read
            }

            entity.get<Transform>().setTranslation(tmpVector)

        }
    }

}
