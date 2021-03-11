package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Mappable

class Water(
    width: Number,
    height: Number
) : Component, Mappable {

    constructor(size: Number) : this(size, size)

    val width: Float = width.toFloat()
    val height: Float = height.toFloat()

    override fun getData(setup: Boolean): Map<String, Any?> {
        return mapOf(
            "width" to width,
            "height" to height
        )
    }

}
