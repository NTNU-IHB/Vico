package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Mappable

class Trail @JvmOverloads constructor(
    length: Float? = null,
    color: Int? = null
) : Component, Mappable {

    val length: Float = length ?: 10f
    val color: Int = color ?: ColorConstants.blue

    override fun getData(setup: Boolean): Map<String, Any?>? {
        return if (setup) {
            mapOf(
                "length" to length,
                "color" to color
            )
        } else {
            null
        }
    }
}
