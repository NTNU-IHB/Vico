package no.ntnu.ihb.acco.core

fun interface RealModifier {

    fun invoke(originalValue: Double): Double

}

data class LinearTransform(
    val factor: Double,
    val offset: Double
) : RealModifier {

    override fun invoke(originalValue: Double): Double {
        return factor * originalValue + offset
    }

}
