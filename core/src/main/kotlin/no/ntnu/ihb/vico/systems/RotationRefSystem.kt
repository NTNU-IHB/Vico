package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.RotationRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.RealProperty
import no.ntnu.ihb.vico.core.SimulationSystem
import no.ntnu.ihb.vico.math.Euler
import no.ntnu.ihb.vico.math.setFromEuler
import org.joml.Quaterniond

class RotationRefSystem : SimulationSystem(
        Family.all(Transform::class.java, RotationRef::class.java).build()
) {

    private val tmpEuler = Euler()
    private val tmpQuat = Quaterniond()
    private val tmpArray = DoubleArray(1)
    private val map: MutableMap<String, RealProperty> = mutableMapOf()

    override fun entityAdded(entity: Entity) {

        val p: RotationRef = entity.get()

        p.xRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }
        p.yRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }
        p.zRef?.also { ref -> entity.getRealPropertyOrNull(ref.name)?.also { v -> map[ref.name] = v } }

    }

    override fun entityRemoved(entity: Entity) {

        val p: RotationRef = entity.get()

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

            entity.get<RotationRef>().also { ref ->

                ref.xRef?.also {
                    val read = map.getValue(it.name).read(tmpArray).first()
                    tmpEuler.x = ref.repr.ensureRadians(it.linearTransform?.invoke(read) ?: read)
                }
                ref.yRef?.also {
                    val read = map.getValue(it.name).read(tmpArray).first()
                    tmpEuler.y = ref.repr.ensureRadians(it.linearTransform?.invoke(read) ?: read)
                }
                ref.zRef?.also {
                    val read = map.getValue(it.name).read(tmpArray).first()
                    tmpEuler.z = ref.repr.ensureRadians(it.linearTransform?.invoke(read) ?: read)
                }

            }

            tmpQuat.setFromEuler(tmpEuler)
            entity.get<Transform>().setQuaternion(tmpQuat)

        }
    }

}
