package no.ntnu.ihb.vico.math

import kotlin.random.Random


private val lut by lazy {
    Array(256) { i ->
        if (i < 16) "0" else i.toString(16)
    }
}

fun generateUUID(): String {

    val d0 = (Random.nextDouble() * 0xffffffff).toInt() or 0
    val d1 = (Random.nextDouble() * 0xffffffff).toInt() or 0
    val d2 = (Random.nextDouble() * 0xffffffff).toInt() or 0
    val d3 = (Random.nextDouble() * 0xffffffff).toInt() or 0

    val uuid = lut[d0 and 0xff] + lut[d0 shr 8 and 0xff] + lut[d0 shr 16 and 0xff] + lut[d0 shr 24 and 0xff] + '-' +
            lut[d1 and 0xff] + lut[d1 shr 8 and 0xff] + '-' + lut[d1 shr 16 and 0x0f or 0x40] + lut[d1 shr 24 and 0xff] + '-' +
            lut[d2 and 0x3f or 0x80] + lut[d2 shr 8 and 0xff] + '-' + lut[d2 shr 16 and 0xff] + lut[d2 shr 24 and 0xff] +
            lut[d3 and 0xff] + lut[d3 shr 8 and 0xff] + lut[d3 shr 16 and 0xff] + lut[d3 shr 24 and 0xff]

    return uuid.toUpperCase()
}
