package no.ntnu.ihb.vico.structure

sealed class Parameter<E> {
    abstract val name: String
    abstract val value: E
}

class IntegerParameter(
    override val name: String,
    override val value: Int
) : Parameter<Int>()

class RealParameter(
    override val name: String,
    override val value: Double
) : Parameter<Double>()

class BooleanParameter(
    override val name: String,
    override val value: Boolean
) : Parameter<Boolean>()

class StringParameter(
    override val name: String,
    override val value: String
) : Parameter<String>()

class EnumerationParameter(
    override val name: String,
    override val value: Int
) : Parameter<Int>()


class ParameterSet(
    val name: String,
    private val parameters: List<Parameter<*>>
) : Iterable<Parameter<*>> by parameters {

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

    val enumerationParameters: List<EnumerationParameter>
        get() {
            return parameters.mapNotNull { if (it is EnumerationParameter) it else null }
        }

}
