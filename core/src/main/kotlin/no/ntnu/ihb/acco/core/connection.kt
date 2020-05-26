package no.ntnu.ihb.acco.core

interface Source<E> {
    fun get(): E
}

interface Sink<E> {
    fun set(value: E)
}

interface Converter<E, T> {
    fun convert(value: E): T
}

interface Connection<E, T> {
    fun transferData()
}

class ScalarConnection<E>(
    private val source: Source<E>,
    private val sinks: List<Sink<E>>
) : Connection<E, E> {

    override fun transferData() {
        val value = source.get()
        sinks.forEach { sink ->
            sink.set(value)
        }
    }
}

class ConvertingConnection<E, T>(
    private val source: Source<E>,
    private val sinks: List<Sink<T>>,
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
