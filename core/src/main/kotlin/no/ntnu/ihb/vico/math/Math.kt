@file: JvmName("Math")

package no.ntnu.ihb.vico.math

import kotlin.math.*

val LN2 = ln(2.0)
const val DEG2RAD = (PI / 180.0)
const val RAD2DEG = (180.0 / PI)

const val TWO_PI = (PI * 2)

fun clamp(value: Int, min: Int, max: Int): Int {
    return max(min, min(max, value))
}

fun clamp(value: Double, min: Number, max: Number): Double {
    return max(min.toDouble(), min(max.toDouble(), value))
}

fun mapLinear(x: Double, a1: Double, a2: Double, b1: Double, b2: Double): Double {
    return b1 + (x - a1) * (b2 - b1) / (a2 - a1)
}

fun lerp(x: Double, y: Double, t: Double): Double {
    return (1 - t) * x + t * y
}

fun smoothStep(x: Double, min: Double, max: Double): Double {
    if (x <= min) {
        return 0.0
    }
    if (x >= max) {
        return 1.0
    }

    return (x - min) / (max - min).let {
        it * it * (3 - 2 * it)
    }

}

fun isPowerOfTwo(value: Int): Boolean {
    return (value and (value - 1)) == 0 && value != 0
}

fun ceilPowerOfTwo(value: Double): Double {
    return 2.0.pow(ceil(ln(value) / LN2))
}

fun floorPowerOfTwo(value: Double): Double {
    return 2.0.pow(floor(ln(value) / LN2))
}

fun transposeMatrix4Array(te: FloatArray): FloatArray {

    var tmp: Float = te[1]; te[1] = te[4]; te[4] = tmp
    tmp = te[2]; te[2] = te[8]; te[8] = tmp
    tmp = te[6]; te[6] = te[9]; te[9] = tmp

    tmp = te[3]; te[3] = te[12]; te[12] = tmp
    tmp = te[7]; te[7] = te[13]; te[13] = tmp
    tmp = te[11]; te[11] = te[14]; te[14] = tmp

    return te
}

fun transposeMatrix4Array(te: DoubleArray): DoubleArray {

    var tmp: Double = te[1]; te[1] = te[4]; te[4] = tmp
    tmp = te[2]; te[2] = te[8]; te[8] = tmp
    tmp = te[6]; te[6] = te[9]; te[9] = tmp

    tmp = te[3]; te[3] = te[12]; te[12] = tmp
    tmp = te[7]; te[7] = te[13]; te[13] = tmp
    tmp = te[11]; te[11] = te[14]; te[14] = tmp

    return te
}
