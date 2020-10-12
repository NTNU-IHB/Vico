package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.math.Angle

class RotationRef(
        val xRef: String? = null,
        val yRef: String? = null,
        val zRef: String? = null,
        val repr: Angle.Unit
) : Component {

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}
