package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.vico.master.MasterAlgorithm

data class DefaultExperiment(
    val startTime: Double = 0.0,
    val stopTime: Double?,
    val algorithm: MasterAlgorithm? = null
)
