package no.ntnu.ihb.vico.fmi

import no.ntnu.ihb.fmi4j.CoSimulationModel
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import java.io.File
import java.net.URL

class SlaveProvider private constructor(
    private val fmu: CoSimulationModel
) {

    constructor(url: URL) : this(AbstractFmu.from(url).asCoSimulationFmu())

    constructor(file: File) : this(AbstractFmu.from(file).asCoSimulationFmu())

    val modelDescription: CoSimulationModelDescription
        get() = fmu.modelDescription

    fun instantiate(instanceName: String): SlaveInstance {
        return fmu.newInstance(instanceName)
    }

}
