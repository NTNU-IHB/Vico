package no.ntnu.ihb.vico.proxyfmu

import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.importer.AbstractFmu
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.vico.model.SlaveProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class ProxySlaveProvider(
    private val remoteInfo: RemoteInfo,
    private val fmuFile: File
) : SlaveProvider {

    override val modelDescription: CoSimulationModelDescription =
        AbstractFmu.from(fmuFile).asCoSimulationFmu().modelDescription

    override fun instantiate(instanceName: String): SlaveInstance {
        LOG.info("Instantiating proxy slave '$instanceName' with remote=$remoteInfo")
        return ProxySlave(remoteInfo, fmuFile, instanceName, modelDescription)
    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(ProxySlaveProvider::class.java)

    }

}
