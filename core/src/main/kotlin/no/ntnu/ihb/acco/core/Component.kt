package no.ntnu.ihb.acco.core


typealias ComponentClass = Class<out Component>

interface Component

interface CoSimulationComponent : Component {

    val variables: Map<String, Var<*>>

    fun getVariable(name: String) = variables[name]
    fun getIntegerVariable(name: String) = variables[name] as IntVar
    fun getRealVariable(name: String) = variables[name] as RealVar
    fun getStringVariable(name: String) = variables[name] as StrVar
    fun getBooleanVariable(name: String) = variables[name] as BoolVar

}
