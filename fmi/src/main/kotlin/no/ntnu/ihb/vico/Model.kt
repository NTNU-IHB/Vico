package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription

interface Model {

    val modelDescription: CoSimulationModelDescription

    fun instantiate(instanceName: String): SlaveInstance

}
