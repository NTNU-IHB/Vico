package no.ntnu.ihb.vico.core

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
