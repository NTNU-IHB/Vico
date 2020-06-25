package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.*
import no.ntnu.ihb.acco.core.RealConnector
import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.model.SlaveProvider

class SystemStructure @JvmOverloads constructor(
    val name: String? = null
) {

    private val components: MutableSet<Component> = mutableSetOf()
    private val connections: MutableSet<Connection<*>> = mutableSetOf()
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

        val connection: Connection<*> = when (sourceVariable.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> IntegerConnection(
                sourceComponent, sourceVariable as IntegerVariable,
                targetComponent, targetVariable as IntegerVariable
            )
            VariableType.REAL -> RealConnection(
                sourceComponent, sourceVariable as RealVariable,
                targetComponent, targetVariable as RealVariable
            )
            VariableType.STRING -> StringConnection(
                sourceComponent, sourceVariable as StringVariable,
                targetComponent, targetVariable as StringVariable
            )
            VariableType.BOOLEAN -> BooleanConnection(
                sourceComponent, sourceVariable as BooleanVariable,
                targetComponent, targetVariable as BooleanVariable
            )
        }

        addConnection(connection)
    }

    fun addConnection(connection: Connection<*>) = apply {
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
    fun apply(engine: Engine, parameterSet: String? = null) {

        components.forEach { c ->
            Entity(c.instanceName).apply {
                addComponent(SlaveComponent(c.slaveProvider, c.instanceName).apply {
                    c.parametersSets.forEach {
                        addParameterSet(it)
                    }
                })
                engine.addEntity(this)
            }
        }

        val system = SlaveSystem(parameterSet = parameterSet)
        engine.addSystem(system)

        connections.forEach { c ->

            val source = engine.getEntityByName(c.source.instanceName).getComponent<SlaveComponent>()
            val target = engine.getEntityByName(c.target.instanceName).getComponent<SlaveComponent>()

            when (c) {
                is IntegerConnection -> {
                    engine.addConnection(
                        ScalarConnection(
                            IntConnector(source, c.sourceVariable.name),
                            IntConnector(target, c.targetVariable.name)
                        )
                    )
                }
                is RealConnection -> {
                    engine.addConnection(
                        ScalarConnection(
                            RealConnector(source, c.sourceVariable.name),
                            RealConnector(target, c.targetVariable.name)
                        )
                    )
                }
                is BooleanConnection -> {
                    engine.addConnection(
                        ScalarConnection(
                            BoolConnector(source, c.sourceVariable.name),
                            BoolConnector(target, c.targetVariable.name)
                        )
                    )
                }
                is StringConnection -> {
                    engine.addConnection(
                        ScalarConnection(
                            StrConnector(source, c.sourceVariable.name),
                            StrConnector(target, c.targetVariable.name)
                        )
                    )
                }
            }

        }

    }

}
