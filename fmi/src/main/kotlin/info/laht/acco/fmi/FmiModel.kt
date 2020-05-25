package info.laht.acco.fmi

import info.laht.acco.Model
import no.ntnu.ihb.fmi4j.CoSimulationModel
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import java.io.File
import java.net.URL

class FmiModel private constructor(
    private val fmu: CoSimulationModel
) : Model {

    constructor(url: URL) : this(AbstractFmu.from(url).asCoSimulationFmu())

    constructor(file: File) : this(AbstractFmu.from(file).asCoSimulationFmu())

    val fmiModelDescription: CoSimulationModelDescription
        get() = fmu.modelDescription

    override val modelDescription
        get() = fmu.modelDescription

    override fun instantiate(instanceName: String): SlaveInstance {
        return fmu.newInstance(instanceName)
    }

}
