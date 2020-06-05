package no.ntnu.ihb.acco.core


abstract class Source<E>(
    val component: Component
) {
    abstract fun get(): E
}

abstract class Sink<E>(
    val component: Component
) {
    abstract fun set(value: E)
}

interface Converter<E, T> {
    fun convert(value: E): T
}

interface Connection<E, T> {

    val source: Source<E>
    val sinks: List<Sink<T>>

    fun transferData()

    companion object {
        const val CONNECTION_NEEDS_UPDATE = "connectionNeedsupdate"
    }

}

class ScalarConnection<E>(
    override val source: Source<E>,
    override val sinks: List<Sink<E>>
) : Connection<E, E> {

    override fun transferData() {
        val value = source.get()
        sinks.forEach { sink ->
            sink.set(value)
        }
    }
}

class ConvertingConnection<E, T>(
    override val source: Source<E>,
    override val sinks: List<Sink<T>>,
    private val converter: Converter<E, T>
) : Connection<E, T> {

    override fun transferData() {
        val value = source.get()
        val convertedValue = converter.convert(value)
        sinks.forEach { sink ->
            sink.set(convertedValue)
        }
    }

}
