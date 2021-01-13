package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.util.StringArray

interface Connection {

    val source: Component
    val targets: List<Component>

    fun transferData()

}

abstract class ScalarConnection<E : Connector>(
    protected val sourceConnector: E,
    protected val sinks: List<E>
) : Connection {

    protected val size: Int = sourceConnector.property.size

    override val source: Component
        get() = sourceConnector.component
    override val targets: List<Component>
        get() = sinks.map { it.component }

    init {
        require(sinks.filter {
            it.property.size == size
        }.size == sinks.size) { "Dimension mismatch between source and sink(s)" }
    }

}

class IntConnection(
    sourceConnector: IntConnector,
    sinks: List<IntConnector>
) : ScalarConnection<IntConnector>(sourceConnector, sinks) {

    private val buffer = IntArray(size)

    constructor(
        source: IntConnector,
        sink: IntConnector
    ) : this(source, listOf(sink))

    override fun transferData() {
        if (size == 1) {
            val read = sourceConnector.property.read()
            sinks.forEach { it.property.write(read) }
        } else {
            val read = sourceConnector.property.read(buffer)
            sinks.forEach { it.property.write(read) }
        }
    }
}

class RealConnection(
    sourceConnector: RealConnector,
    sinks: List<RealConnector>
) : ScalarConnection<RealConnector>(sourceConnector, sinks) {

    private val buffer = DoubleArray(size)

    constructor(
        source: RealConnector,
        sink: RealConnector
    ) : this(source, listOf(sink))

    override fun transferData() {
        if (size == 1) {
            val read = sourceConnector.property.read()
            val mod = sourceConnector.modifier
            sinks.forEach { it.property.write(mod?.invoke(read) ?: read) }
        } else {
            val read = sourceConnector.property.read(buffer)
            val mod = sourceConnector.modifier
            sinks.forEach {
                it.property.write(mod?.let { mod -> read.map { value -> mod.invoke(value) }.toDoubleArray() } ?: read)
            }
        }
    }

}

class BoolConnection(
    sourceConnector: BoolConnector,
    sinks: List<BoolConnector>
) : ScalarConnection<BoolConnector>(sourceConnector, sinks) {

    private val buffer = BooleanArray(size)

    constructor(
        source: BoolConnector,
        sink: BoolConnector
    ) : this(source, listOf(sink))

    override fun transferData() {
        if (size == 1) {
            val read = sourceConnector.property.read()
            sinks.forEach { it.property.write(read) }
        } else {
            val read = sourceConnector.property.read(buffer)
            sinks.forEach { it.property.write(read) }
        }
    }

}

class StrConnection(
    sourceConnector: StrConnector,
    sinks: List<StrConnector>
) : ScalarConnection<StrConnector>(sourceConnector, sinks) {

    private val buffer = StringArray(size)

    constructor(
        source: StrConnector,
        sink: StrConnector
    ) : this(source, listOf(sink))

    override fun transferData() {
        if (size == 1) {
            val read = sourceConnector.property.read()
            sinks.forEach { it.property.write(read) }
        } else {
            val read = sourceConnector.property.read(buffer)
            sinks.forEach { it.property.write(read) }
        }
    }

}
