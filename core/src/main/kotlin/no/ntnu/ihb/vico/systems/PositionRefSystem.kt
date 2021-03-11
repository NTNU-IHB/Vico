package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import org.joml.Vector3d

class PositionRefSystem : ObserverSystem(
    Family.all(Transform::class.java, PositionRef::class.java).build()
) {

    private val tmpVector = Vector3d()

    override fun postInit() {
        update()
    }

    override fun observe(currentTime: Double) {
        update()
    }

    private fun update() {

        tmpVector.set(0.0)

        for (entity in entities) {

            entity.get<PositionRef>().also { ref ->

                ref.xRef?.also {
                    tmpVector.x = it.get(engine)
                }
                ref.yRef?.also {
                    tmpVector.y = it.get(engine)
                }
                ref.zRef?.also {
                    tmpVector.z = it.get(engine)
                }

            }

            entity.get<Transform>().setTranslation(tmpVector)

        }
    }

}
