package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.RotationRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import no.ntnu.ihb.vico.math.Euler
import no.ntnu.ihb.vico.math.setFromEuler
import org.joml.Quaterniond

class RotationRefSystem : ObserverSystem(
    Family.all(Transform::class.java, RotationRef::class.java).build()
) {

    private val tmpEuler = Euler()
    private val tmpQuat = Quaterniond()

    override fun postInit() {
        update()
    }

    override fun observe(currentTime: Double) {
        update()
    }

    private fun update() {

        tmpQuat.identity()
        tmpEuler.set(0.0, 0.0, 0.0)

        for (entity in entities) {

            entity.get<RotationRef>().also { ref ->

                ref.xRef?.also {
                    tmpEuler.x = ref.repr.ensureRadians(it.get(engine))
                }
                ref.yRef?.also {
                    tmpEuler.y = ref.repr.ensureRadians(it.get(engine))
                }
                ref.zRef?.also {
                    tmpEuler.z = ref.repr.ensureRadians(it.get(engine))
                }

            }

            tmpQuat.setFromEuler(tmpEuler)
            entity.get<Transform>().setQuaternion(tmpQuat)

        }
    }

}
