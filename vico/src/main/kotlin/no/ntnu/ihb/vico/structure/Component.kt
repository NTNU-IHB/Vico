package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.vico.Model

class Component(
    private val model: Model,
    val instanceName: String,
    val stepSizeHint: Double? = null
) {

    val modelDescription: CoSimulationModelDescription
        get() = model.modelDescription

    private val connectors = mutableSetOf<Connector>()
    private val parameterSets = linkedMapOf<String, ParameterSet>()

    fun instantiate() = model.instantiate(instanceName)

    fun getConnector(name: String): Connector {
        return connectors.find { it.name == name }
            ?: throw IllegalArgumentException("No connector named '$name' in component '${model.modelDescription.modelName}'!")
    }

    fun addConnector(connector: Connector) {
        connector.validate(modelDescription)
        check(connectors.add(connector)) {
            "Connector '${connector.name}' has already been added to component '${model.modelDescription.modelName}'!"
        }
    }

    fun getParameterSet(name: String): ParameterSet? {
        return parameterSets[name]
    }

    fun addParameterSet(name: String, parameters: List<Parameter<*>>) = apply {
        addParameterSet(name, ParameterSet(parameters))
    }

    fun addParameterSet(name: String, parameterSet: ParameterSet) = apply {
        check(name !in parameterSets) {
            "ParameterSet '$name' has already been added to component '${model.modelDescription.modelName}'!"
        }
        parameterSets[name] = parameterSet
    }

}
