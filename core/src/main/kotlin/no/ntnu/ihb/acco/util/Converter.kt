package no.ntnu.ihb.acco.util

fun interface Converter<E, T> {
    fun convert(value: E): T
}
