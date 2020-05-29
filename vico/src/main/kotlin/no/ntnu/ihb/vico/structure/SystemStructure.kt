package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.vico.Model
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem

class SystemStructure @JvmOverloads constructor(
    val name: String? = null
) {

    private val components = mutableSetOf<Component>()
    private val connections = mutableSetOf<SspConnection<*>>()
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

        check(sourceVariable.type == targetVariable.type)

        val connection: SspConnection<*> = when (sourceVariable.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> SspIntegerConnection(
                sourceComponent, sourceVariable as IntegerVariable,
                targetComponent, targetVariable as IntegerVariable
            )
            VariableType.REAL -> SspRealConnection(
                sourceComponent, sourceVariable as RealVariable,
                targetComponent, targetVariable as RealVariable
            )
            VariableType.STRING -> SspStringConnection(
                sourceComponent, sourceVariable as StringVariable,
                targetComponent, targetVariable as StringVariable
            )
            VariableType.BOOLEAN -> SspBooleanConnection(
                sourceComponent, sourceVariable as BooleanVariable,
                targetComponent, targetVariable as BooleanVariable
            )
        }

        addConnection(connection)
    }

    fun addConnection(connection: SspConnection<*>) = apply {
        check(connections.add(connection)) { "Connection already exists!" }
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
                addComponent(SlaveComponent(c.instantiate()).apply {
                    c.parametersSets.forEach {
                        addParameterSet(it)
                    }
                })
                engine.addEntity(this)
            }
        }

        val system = SlaveSystem()
        engine.addSystem(system)

        connections.forEach { c ->
            system.addConnection(c.toSlaveConnection(system.slaves))
        }

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
