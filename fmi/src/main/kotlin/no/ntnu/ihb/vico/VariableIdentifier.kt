package no.ntnu.ihb.vico

data class VariableIdentifier @JvmOverloads constructor(
    val componentName: String,
    val variableName: String,
    val modifier: ((Double) -> Double)? = null
)
