package no.ntnu.ihb.acco.math

import org.joml.Matrix3d
import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.Vector3dc
import kotlin.math.max
import kotlin.math.sqrt


fun Vector3d.clamp(min: Double, max: Double) = apply {
    x = max(min, kotlin.math.min(max, x))
    y = max(min, kotlin.math.min(max, y))
    z = max(min, kotlin.math.min(max, z))
}

fun Vector3d.clamp(min: Vector3dc, max: Vector3dc) = apply {
    x = max(min.x(), kotlin.math.min(max.x(), x))
    y = max(min.y(), kotlin.math.min(max.y(), y))
    z = max(min.z(), kotlin.math.min(max.z(), z))
}

fun Vector3d.fromArray(array: DoubleArray) = apply {
    fromArray(array, 0)
}

fun Vector3d.fromArray(array: DoubleArray, offset: Int) = apply {
    x = array[offset]
    y = array[offset + 1]
    z = array[offset + 2]
}

fun Matrix4dc.getMaxScaleOnAxis(): Double {
    val scaleXSq = m00() * m00() + m01() * m01() + m02() * m02()
    val scaleYSq = m10() * m10() + m11() * m11() + m12() * m12()
    val scaleZSq = m20() * m20() + m21() * m21() + m22() * m22()
    return sqrt(max(max(scaleXSq, scaleYSq), scaleZSq))
}

fun Matrix4dc.getNormalMatrix(): Matrix3d {
    return Matrix3d(this).invert().transpose()
}
