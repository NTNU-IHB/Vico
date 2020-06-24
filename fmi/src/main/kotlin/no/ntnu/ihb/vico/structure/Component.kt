package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.vico.model.Model

class Component(
    val model: Model,
    val instanceName: String,
    val stepSizeHint: Double? = null
) {

    val modelName: String
        get() = model.modelDescription.modelName

    val modelDescription: CoSimulationModelDescription
        get() = model.modelDescription

    private val connectors = mutableSetOf<Connector>()
    private val _parameterSets = mutableSetOf<ParameterSet>()
    val parametersSets: Set<ParameterSet> = _parameterSets

    fun instantiate() = model.instantiate(instanceName)

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

    fun addParameterSet(name: String, parameters: List<Parameter<*>>) = apply {
        addParameterSet(ParameterSet(name, parameters))
    }

    fun addParameterSet(parameterSet: ParameterSet) = apply {
        check(_parameterSets.add(parameterSet)) {
            "ParameterSet '${parameterSet.name}' has already been added to component '${instanceName}'!"
        }
    }

}
