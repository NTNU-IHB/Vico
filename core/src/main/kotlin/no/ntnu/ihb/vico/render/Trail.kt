package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component

class Trail @JvmOverloads constructor(
    length: Float? = null,
    color: Int? = null
) : Component {

    val length: Float = length ?: 10f
    val color: Int = color ?: ColorConstants.blue

}
