package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.variables.Causality


enum class ConnectorKind {

    INPUT,
    OUTPUT,
    INOUT,
    PARAMETER,
    CALCULATED_PARAMETER

}

sealed class Connector(
    val name: String,
    val kind: ConnectorKind
) {

    internal fun validate(md: ModelDescription) {

        val variable = md.modelVariables.getByName(name)
        when (kind) {
            ConnectorKind.INPUT -> require(variable.causality == Causality.INPUT) { "Illegal kind for connector '${md.modelName}.$name': ${variable.causality}" }
            ConnectorKind.OUTPUT -> require(variable.causality == Causality.OUTPUT) { "Illegal kind for connector '${md.modelName}.$name': ${variable.causality}" }
            ConnectorKind.PARAMETER -> require(variable.causality == Causality.PARAMETER) { "Illegal kind for connector '$${md.modelName}.$name': ${variable.causality}" }
            ConnectorKind.CALCULATED_PARAMETER -> require(variable.causality == Causality.CALCULATED_PARAMETER) { "Illegal kind for connector '$${md.modelName}.$name': ${variable.causality}" }
            else -> throw UnsupportedOperationException("Unsupported connector kind: $kind")
        }

    }

}

class IntegerConnector(
    name: String,
    kind: ConnectorKind
) : Connector(name, kind)

class RealConnector(
    name: String,
    kind: ConnectorKind,
    val unit: String? = null
) : Connector(name, kind)

class BooleanConnector(
    name: String,
    kind: ConnectorKind
) : Connector(name, kind)

class StringConnector(
    name: String,
    kind: ConnectorKind
) : Connector(name, kind)
