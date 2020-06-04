package no.ntnu.ihb.vico.fmuproxy

import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmuproxy.AbstractRpcFmuClient
import no.ntnu.ihb.vico.model.Model

class ProxyModel(
    private val client: AbstractRpcFmuClient
) : Model {

    override val modelDescription by lazy { client.modelDescription }

    override fun instantiate(instanceName: String): SlaveInstance {
        return client.newInstance(instanceName)
    }

}
