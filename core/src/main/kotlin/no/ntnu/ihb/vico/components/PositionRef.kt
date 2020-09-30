package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component

class PositionRef(
        val xRef: String? = null,
        val yRef: String? = null,
        val zRef: String? = null
) : Component {

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}
