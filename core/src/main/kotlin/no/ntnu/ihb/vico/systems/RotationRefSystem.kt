package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.RotationRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.RealProperty
import no.ntnu.ihb.vico.core.SimulationSystem
import no.ntnu.ihb.vico.math.Angle
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

        p.xRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }
        p.yRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }
        p.zRef?.also { ref -> entity.getRealPropertyOrNull(ref)?.also { v -> map[ref] = v } }

    }

    override fun entityRemoved(entity: Entity) {

        val p: RotationRef = entity.get()

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

            val ref: RotationRef = entity.get()

            ref.xRef?.also { tmpEuler.x = ensureRadians(map.getValue(it).read(tmpArray).first(), ref.repr) }
            ref.yRef?.also { tmpEuler.y = ensureRadians(map.getValue(it).read(tmpArray).first(), ref.repr) }
            ref.zRef?.also { tmpEuler.z = ensureRadians(map.getValue(it).read(tmpArray).first(), ref.repr) }

            tmpQuat.setFromEuler(tmpEuler)
            entity.get<Transform>().setQuaternion(tmpQuat)

        }
    }

    private fun ensureRadians(value: Double, unit: Angle.Unit): Double {
        return if (unit == Angle.Unit.RAD) value else Math.toRadians(value)
    }

}
