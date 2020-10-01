package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.util.StringArray

interface Connection {

    val source: Component
    val targets: List<Component>

    fun transferData()

}

class ScalarConnection(
    private val sourceConnector: Connector,
    private val sinks: List<Connector>
) : Connection {

    override val source: Component
        get() = sourceConnector.component
    override val targets: List<Component>
        get() = sinks.map { it.component }

    private val size: Int = sourceConnector.property.size

    private val intBuffer by lazy { IntArray(size) }
    private val realBuffer by lazy { DoubleArray(size) }
    private val booleanBuffer by lazy { BooleanArray(size) }
    private val stringBuffer by lazy { StringArray(size) }

    constructor(
        source: Connector,
        sink: Connector
    ) : this(source, listOf(sink))

    init {
        require(sinks.filter {
            it.property.size == size
        }.size == sinks.size) { "Dimension mismatch between source and sink(s)" }
    }

    override fun transferData() {

        when (val v = sourceConnector.property) {
            is IntProperty -> {
                val read = v.read(intBuffer)
                sinks.forEach { (it.property as IntProperty).write(read) }
            }
            is RealProperty -> {
                val read = v.read(realBuffer)
                sinks.forEach { (it.property as RealProperty).write(read) }
            }
            is StrProperty -> {
                val read = v.read(stringBuffer)
                sinks.forEach { (it.property as StrProperty).write(read) }
            }
            is BoolProperty -> {
                val read = v.read(booleanBuffer)
                sinks.forEach { (it.property as BoolProperty).write(read) }
            }
        }

    }
}
