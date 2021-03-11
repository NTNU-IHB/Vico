package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Mappable

class Camera(
    val fov: Int = 65
) : Component, Mappable {

    override fun getData(setup: Boolean): Map<String, Any?>? {
        return if (setup) {
            mapOf(
                "fov" to fov
            )
        } else {
            null
        }
    }

}
