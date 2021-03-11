package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.dsl.RealProvider
import no.ntnu.ihb.vico.dsl.ValueProvider
import no.ntnu.ihb.vico.math.Angle

data class PositionRef(
    val xRef: ValueProvider? = null,
    val yRef: ValueProvider? = null,
    val zRef: ValueProvider? = null
) : Component {

    constructor(
        xRef: String? = null,
        yRef: String? = null,
        zRef: String? = null
    ) : this(xRef?.let { RealProvider(it) }, yRef?.let { RealProvider(it) }, zRef?.let { RealProvider(it) })

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}

data class RotationRef(
    val xRef: ValueProvider? = null,
    val yRef: ValueProvider? = null,
    val zRef: ValueProvider? = null,
    var repr: Angle.Unit
) : Component {

    constructor(
        xRef: String? = null,
        yRef: String? = null,
        zRef: String? = null,
        repr: Angle.Unit
    ) : this(xRef?.let { RealProvider(it) }, yRef?.let { RealProvider(it) }, zRef?.let { RealProvider(it) }, repr)

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}
