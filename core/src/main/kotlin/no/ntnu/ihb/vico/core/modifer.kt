package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.math.clamp

fun interface RealModifier {

    fun invoke(originalValue: Double): Double

}

data class ClampModifier(
    val min: Double,
    val max: Double
) : RealModifier {

    override fun invoke(originalValue: Double): Double {
        return clamp(originalValue, min, max)
    }

}

data class LinearTransform(
    val factor: Double,
    val offset: Double
) : RealModifier {

    override fun invoke(originalValue: Double): Double {
        return factor * originalValue + offset
    }

}
