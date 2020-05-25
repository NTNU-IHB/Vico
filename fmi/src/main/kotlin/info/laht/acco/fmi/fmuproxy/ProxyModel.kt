package info.laht.acco.fmi.fmuproxy

import info.laht.acco.Model
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmuproxy.AbstractRpcFmuClient

class ProxyModel(
    private val client: AbstractRpcFmuClient
) : Model {

    override val modelDescription by lazy { client.modelDescription }

    override fun instantiate(instanceName: String): SlaveInstance {
        return client.newInstance(instanceName)
    }

}
