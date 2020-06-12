package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.acco.core.RealModifier

data class VariableHandle @JvmOverloads constructor(
    val componentName: String,
    val variableName: String,
    val modifier: RealModifier? = null
)
