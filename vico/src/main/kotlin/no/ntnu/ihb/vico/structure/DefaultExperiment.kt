package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.System

data class DefaultExperiment(
    val startTime: Double,
    val stopTime: Double?,
    val system: System? = null
)
