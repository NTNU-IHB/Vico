package no.ntnu.ihb.acco.core


typealias ComponentClass = Class<out Component>

interface Component

interface CosimulationComponent : Component {

    val variables: Map<String, Var<*>>

    fun getVariable(name: String) = variables[name]

}
