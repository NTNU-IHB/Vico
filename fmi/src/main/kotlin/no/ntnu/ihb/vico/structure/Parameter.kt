package no.ntnu.ihb.vico.structure

sealed class Parameter {
    abstract val name: String
}

class IntegerParameter(
    override val name: String,
    val value: Int
) : Parameter()

class RealParameter(
    override val name: String,
    val value: Double
) : Parameter()

class BooleanParameter(
    override val name: String,
    val value: Boolean
) : Parameter()

class StringParameter(
    override val name: String,
    val value: String
) : Parameter()

class ParameterSet(
    val name: String,
    private val parameters: Set<Parameter>
) : Iterable<Parameter> by parameters {

    constructor(name: String, parameters: List<Parameter>) : this(name, parameters.toSet())

    constructor(name: String, parameter: Parameter, vararg additionalParameters: Parameter)
            : this(name, listOf(parameter, *additionalParameters))

    val integerParameters: List<IntegerParameter>
        get() {
            return parameters.mapNotNull { if (it is IntegerParameter) it else null }
        }

    val realParameters: List<RealParameter>
        get() {
            return parameters.mapNotNull { if (it is RealParameter) it else null }
        }

    val booleanParameters: List<BooleanParameter>
        get() {
            return parameters.mapNotNull { if (it is BooleanParameter) it else null }
        }

    val stringParameters: List<StringParameter>
        get() {
            return parameters.mapNotNull { if (it is StringParameter) it else null }
        }

    override fun toString(): String {
        return "ParameterSet(name=$name, parameters=${parameters.map { it.name }})"
    }

}
