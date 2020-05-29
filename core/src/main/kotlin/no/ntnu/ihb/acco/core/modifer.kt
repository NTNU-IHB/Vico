package no.ntnu.ihb.acco.core

interface RealModifier {

    fun apply(originalValue: Double): Double

}

class LinearTransform(
    val factor: Double,
    val offset: Double
) : RealModifier {

    override fun apply(originalValue: Double): Double {
        return factor * originalValue + offset
    }
}
