package no.ntnu.ihb.acco.core


sealed class Connector<E : Var<*>>(
    val component: Component,
    val variable: E
)

class IntConnector(
    component: Component,
    variable: IntVar
) : Connector<IntVar>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getIntegerVariable(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class RealConnector(
    component: Component,
    variable: RealVar
) : Connector<RealVar>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getRealVariable(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class StrConnector(
    component: Component,
    variable: StrVar
) : Connector<StrVar>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getStringVariable(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class BoolConnector(
    component: Component,
    variable: BoolVar
) : Connector<BoolVar>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getBooleanVariable(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}


interface Converter<E, T> {
    fun convert(value: E): T
}

interface Connection {

    val source: Component
    val targets: List<Component>

    fun transferData()

    companion object {
        const val CONNECTION_NEEDS_UPDATE = "connectionNeedsUpdate"
    }

}

class ScalarConnection<E : Var<*>>(
    private val sourceConnector: Connector<E>,
    private val sinks: List<Connector<E>>
) : Connection {

    override val source: Component
        get() = sourceConnector.component
    override val targets: List<Component>
        get() = sinks.map { it.component }

    private val size: Int = sourceConnector.variable.size


    constructor(
        source: Connector<E>,
        sink: Connector<E>
    ) : this(source, listOf(sink))

    init {
        require(sinks.filter {
            it.variable.size == size
        }.size == sinks.size) { "Dimension mismatch between source and sink(s)" }
    }

    override fun transferData() {

        when (val v = sourceConnector.variable) {
            is IntVar -> {
                val read = IntArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as IntVar).write(read) }
            }
            is RealVar -> {
                val read = DoubleArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as RealVar).write(read) }
            }
            is StrVar -> {
                val read = StringArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as StrVar).write(read) }
            }
            is BoolVar -> {
                val read = BooleanArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as BoolVar).write(read) }
            }
        }

    }
}

/*class ConvertingConnection<E: Var<*>, T: Var<*>>(
    override val source: Connector<E>,
    override val sinks: List<Connector<T>>,
    private val converter: Converter<E, T>
) : Connection<E, T> {

    constructor(source: Connector<Var<E>>, sink: Connector<T>, converter: Converter<E, T>)
            : this(source, listOf(sink), converter)

    override fun transferData() {
        *//*val value = source.variable
        val convertedValue = converter.convert(value)
        sinks.forEach { sink ->
            sink.set(convertedValue)
        }*//*
    }

}*/
