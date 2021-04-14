package no.ntnu.ihb.vico.proxyfmu

import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.vico.model.SlaveProvider
import java.io.File

class ProxySlaveProvider(
    private val remoteInfo: RemoteInfo,
    private val fmuFile: File
) : SlaveProvider {

    override val modelDescription: CoSimulationModelDescription =
        AbstractFmu.from(fmuFile).asCoSimulationFmu().modelDescription

    override fun instantiate(instanceName: String): SlaveInstance {
        return ProxySlave(remoteInfo, fmuFile, instanceName, modelDescription)
    }
}
