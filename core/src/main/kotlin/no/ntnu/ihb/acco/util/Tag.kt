package no.ntnu.ihb.acco.util

fun String.toTag() = Tag(this)

data class Tag(
    val value: String
)
