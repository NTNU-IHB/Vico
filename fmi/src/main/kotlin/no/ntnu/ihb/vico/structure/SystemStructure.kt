package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.master.MasterAlgorithm
import no.ntnu.ihb.vico.model.SlaveProvider

class SystemStructure @JvmOverloads constructor(
    val name: String? = null
) {

    private val components: MutableSet<Component> = mutableSetOf()
    private val connections: MutableSet<ConnectionInfo<*>> = mutableSetOf()
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

        val connection: ConnectionInfo<*> = when (sourceVariable.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> IntegerConnectionInfo(
                sourceComponent, sourceVariable as IntegerVariable,
                targetComponent, targetVariable as IntegerVariable
            )
            VariableType.REAL -> RealConnectionInfo(
                sourceComponent, sourceVariable as RealVariable,
                targetComponent, targetVariable as RealVariable
            )
            VariableType.STRING -> StringConnectionInfo(
                sourceComponent, sourceVariable as StringVariable,
                targetComponent, targetVariable as StringVariable
            )
            VariableType.BOOLEAN -> BooleanConnectionInfo(
                sourceComponent, sourceVariable as BooleanVariable,
                targetComponent, targetVariable as BooleanVariable
            )
        }

        addConnection(connection)
    }

    fun addConnection(connection: ConnectionInfo<*>) = apply {
        check(connections.add(connection)) { "Connection already exists!" }
    }

    @JvmOverloads
    fun addComponent(slaveProvider: SlaveProvider, instanceName: String, stepSizeHint: Double? = null) {
        addComponent(Component(slaveProvider, instanceName, stepSizeHint))
    }

    fun addComponent(component: Component) {
        check(components.add(component)) { "$component has already been added" }
    }

    @JvmOverloads
    fun apply(engine: Engine, algorithm: MasterAlgorithm = FixedStepMaster(), parameterSet: String? = null) {

        components.forEach { c ->
            engine.createEntity(c.instanceName, c)
        }

        engine.addSystem(SlaveSystem(algorithm = algorithm, parameterSet = parameterSet))

        connections.forEach { c ->

            val source = getComponent(c.source.instanceName)
            val target = getComponent(c.target.instanceName)

            val connection = when (c) {
                is IntegerConnectionInfo -> {
                    IntConnection(
                        IntConnector(source, c.sourceVariable.name),
                        IntConnector(target, c.targetVariable.name)
                    )
                }
                is RealConnectionInfo -> {
                    RealConnection(
                        RealConnector(source, c.sourceVariable.name, c.modifier),
                        RealConnector(target, c.targetVariable.name)
                    )
                }
                is BooleanConnectionInfo -> {
                    BoolConnection(
                        BoolConnector(source, c.sourceVariable.name),
                        BoolConnector(target, c.targetVariable.name)
                    )
                }
                is StringConnectionInfo -> {
                    StrConnection(
                        StrConnector(source, c.sourceVariable.name),
                        StrConnector(target, c.targetVariable.name)
                    )
                }
            }

            engine.addConnection(connection)

        }

    }

}
