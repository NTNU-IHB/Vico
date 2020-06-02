package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescription
import java.util.*

private fun defaultInstanceName(modelDescription: ModelDescription): String {
    return "${modelDescription.modelName}_${UUID.randomUUID()}"
}

interface Model {

    val modelDescription: CoSimulationModelDescription

    fun instantiate(instanceName: String = defaultInstanceName(modelDescription)): SlaveInstance

}
