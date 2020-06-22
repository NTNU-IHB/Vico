package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.StringArray


sealed class Connector<E : Property>(
    val component: Component,
    val variable: E
)

class IntConnector(
    component: Component,
    variable: IntProperty
) : Connector<IntProperty>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getIntegerProperty(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class RealConnector(
    component: Component,
    variable: RealProperty
) : Connector<RealProperty>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getRealProperty(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class StrConnector(
    component: Component,
    variable: StrProperty
) : Connector<StrProperty>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getStringProperty(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}

class BoolConnector(
    component: Component,
    variable: BoolProperty
) : Connector<BoolProperty>(component, variable) {
    constructor(component: Component, name: String) : this(
        component, component.getBooleanProperty(name)
            ?: throw IllegalArgumentException("No variable named '$name' registered in component!")
    )
}


interface Connection {

    val source: Component
    val targets: List<Component>

    fun transferData()

    companion object {
        const val CONNECTION_NEEDS_UPDATE = "connectionNeedsUpdate"
    }

}

class ScalarConnection<E : Property>(
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
            is IntProperty -> {
                val read = IntArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as IntProperty).write(read) }
            }
            is RealProperty -> {
                val read = DoubleArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as RealProperty).write(read) }
            }
            is StrProperty -> {
                val read = StringArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as StrProperty).write(read) }
            }
            is BoolProperty -> {
                val read = BooleanArray(size).also { v.read(it) }
                sinks.forEach { (it.variable as BoolProperty).write(read) }
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
