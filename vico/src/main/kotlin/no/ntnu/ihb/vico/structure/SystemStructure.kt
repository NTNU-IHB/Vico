package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.vico.ComponentConnection
import no.ntnu.ihb.vico.Connection
import no.ntnu.ihb.vico.Model
import no.ntnu.ihb.vico.SlaveComponent

class SystemStructure @JvmOverloads constructor(
    val name: String? = null
) {

    val components = mutableSetOf<SlaveComponent>()
    val connections = mutableSetOf<ComponentConnection>()
    var defaultExperiment: DefaultExperiment? = null

    private fun getComponent(instanceName: String): SlaveComponent {
        return components.firstOrNull { it.instanceName == instanceName }
            ?: throw IllegalArgumentException("Could not find any component named '$instanceName'!")
    }

    fun addConnection(
        sourceComponent: String, sourceVariable: String,
        targetComponent: String, targetVariable: String
    ): Connection<*> {
        TODO()
    }

    fun addConnection(connection: ComponentConnection) = apply {
        check(connections.add(connection))
    }

    @JvmOverloads
    fun addComponent(model: Model, instanceName: String, stepSizeHint: Double? = null) {
        addComponent(SlaveComponent(model, instanceName, stepSizeHint))
    }

    fun addComponent(component: SlaveComponent) {
        check(components.add(component)) { "$component has already been added" }
    }

}

/*
sealed class ConnectionTemplate(
    val sourceComponent: String,
    val sourceVariable: ScalarVariable,
    val targetComponent: String,
    val targetVariable: ScalarVariable
) {

    val connectionType = sourceVariable.type
    internal val modifiers by lazy { mutableListOf<Modifier<E>>() }

    init {
        require(sourceVariable != targetVariable)
        require(sourceVariable.type == targetVariable.type)
    }

    fun addModifier(modifier: Modifier<E>) = apply {
        modifiers.add(modifier)
    }

}

class IntegerConnectionTemplate(
     sourceComponent: String,
     sourceVariable: IntegerVariable,
     targetComponent: String,
     targetVariable: IntegerVariable
): ConnectionTemplate<Int>(
    sourceComponent, sourceVariable, targetComponent, targetVariable
)

class RealConnectionTemplate(
    sourceComponent: String,
    sourceVariable: RealVariable,
    targetComponent: String,
    targetVariable: RealVariable
): ConnectionTemplate<Double>(
    sourceComponent, sourceVariable, targetComponent, targetVariable
)

class BooleanConnectionTemplate(
    sourceComponent: String,
    sourceVariable: BooleanVariable,
    targetComponent: String,
    targetVariable: BooleanVariable
): ConnectionTemplate<Boolean>(
    sourceComponent, sourceVariable, targetComponent, targetVariable
)

class StringConnectionTemplate(
    sourceComponent: String,
    sourceVariable: StringVariable,
    targetComponent: String,
    targetVariable: StringVariable
): ConnectionTemplate<String>(
    sourceComponent, sourceVariable, targetComponent, targetVariable
)

class EnumerationConnectionTemplate(
    sourceComponent: String,
    sourceVariable: EnumerationVariable,
    targetComponent: String,
    targetVariable: EnumerationVariable
): ConnectionTemplate<Int>(
    sourceComponent, sourceVariable, targetComponent, targetVariable
)
*/
