package no.ntnu.ihb.vico.util

fun interface Converter<E, T> {
    fun convert(value: E): T
}
