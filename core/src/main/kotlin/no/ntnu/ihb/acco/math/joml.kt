package no.ntnu.ihb.acco.math

import org.joml.*
import kotlin.math.max
import kotlin.math.sqrt

val Vector3_X: Vector3dc = Vector3d(1.0, 0.0, 0.0)
val Vector3_Y: Vector3dc = Vector3d(0.0, 1.0, 0.0)
val Vector3_Z: Vector3dc = Vector3d(0.0, 0.0, 1.0)

fun Vector2d.addScaledVector(v: Vector2dc, s: Double) = apply {
    this.x += v.x() * s
    this.y += v.y() * s
}

fun Vector3d.addScaledVector(v: Vector3dc, s: Double) = apply {
    this.x += v.x() * s
    this.y += v.y() * s
    this.z += v.z() * s
}

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
