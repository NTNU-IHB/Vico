package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.model.SlaveProvider

class Component(
    slaveProvider: SlaveProvider,
    instanceName: String,
    stepSizeHint: Double? = null
) : SlaveComponent(slaveProvider, instanceName, stepSizeHint) {

    private val connectors: MutableSet<Connector> = mutableSetOf()

    fun getConnector(name: String): Connector {
        return connectors.find { it.name == name }
            ?: throw IllegalArgumentException("No connector named '$name' in component '$instanceName'!")
    }

    fun addConnector(connector: Connector) {
        connector.validate(modelDescription)
        check(connectors.add(connector)) {
            "Connector '${connector.name}' has already been added to component '${instanceName}'!"
        }
    }

}
