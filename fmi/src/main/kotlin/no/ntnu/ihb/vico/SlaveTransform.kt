package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.core.Component

class SlaveTransform(
    val xRef: String? = null,
    val yRef: String? = null,
    val zRef: String? = null
) : Component {

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}
