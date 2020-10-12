package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.LinearTransform
import no.ntnu.ihb.vico.math.Angle

data class RealRef(
    val name: String,
    val linearTransform: LinearTransform? = null
)

data class PositionRef(
    val xRef: RealRef? = null,
    val yRef: RealRef? = null,
    val zRef: RealRef? = null
) : Component {

    constructor(
        xRef: String? = null,
        yRef: String? = null,
        zRef: String? = null
    ) : this(xRef?.let { RealRef(it) }, yRef?.let { RealRef(it) }, zRef?.let { RealRef(it) })

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}

data class RotationRef(
    val xRef: RealRef? = null,
    val yRef: RealRef? = null,
    val zRef: RealRef? = null,
    var repr: Angle.Unit
) : Component {

    constructor(
        xRef: String? = null,
        yRef: String? = null,
        zRef: String? = null,
        repr: Angle.Unit
    ) : this(xRef?.let { RealRef(it) }, yRef?.let { RealRef(it) }, zRef?.let { RealRef(it) }, repr)

    init {
        require(listOfNotNull(xRef, yRef, zRef).isNotEmpty())
        { "At least one reference must be non-null!" }
    }

}
