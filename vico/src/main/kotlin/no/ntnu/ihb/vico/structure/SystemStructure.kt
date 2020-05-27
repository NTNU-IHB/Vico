package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.FixedStepSlaveSystem
import no.ntnu.ihb.vico.Model
import no.ntnu.ihb.vico.SlaveComponent

class SystemStructure @JvmOverloads constructor(
    val name: String? = null
) {

    val components = mutableSetOf<Component>()
    val connections = mutableSetOf<Connection>()
    var defaultExperiment: DefaultExperiment? = null

    private fun getComponent(instanceName: String): Component {
        return components.firstOrNull { it.instanceName == instanceName }
            ?: throw IllegalArgumentException("Could not find any component named '$instanceName'!")
    }

    fun addConnection(
        sourceComponentName: String, sourceVariableName: String,
        targetComponentName: String, targetVariableName: String
    ) {

        val sourceComponent = components.first { it.instanceName == sourceComponentName }
        val targetComponent = components.first { it.instanceName == targetComponentName }

        val sourceVariable = sourceComponent.modelDescription.getVariableByName(sourceVariableName)
        val targetVariable = targetComponent.modelDescription.getVariableByName(targetVariableName)

        addConnection(Connection(sourceComponent, sourceVariable, targetComponent, targetVariable))
    }

    fun addConnection(connection: Connection) = apply {
        check(connections.add(connection))
    }

    @JvmOverloads
    fun addComponent(model: Model, instanceName: String, stepSizeHint: Double? = null) {
        addComponent(Component(model, instanceName, stepSizeHint))
    }

    fun addComponent(component: Component) {
        check(components.add(component)) { "$component has already been added" }
    }

    fun apply(engine: Engine) {

        components.forEach { c ->
            Entity(c.instanceName).apply {
                addComponent(SlaveComponent(c.instantiate()))
                engine.addEntity(this)
            }
        }

        val algorithm = FixedStepSlaveSystem()
        connections.forEach { c ->
            algorithm.addConnection(c)
        }

        engine.addSystem(algorithm)


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
