package no.ntnu.ihb.vico.math

import org.joml.*
import kotlin.math.*

val Vector3d_X: Vector3dc = Vector3d(1.0, 0.0, 0.0)
val Vector3d_Y: Vector3dc = Vector3d(0.0, 1.0, 0.0)
val Vector3d_Z: Vector3dc = Vector3d(0.0, 0.0, 1.0)

fun Vector2d.copy() = Vector2d(this)
fun Vector3d.copy() = Vector3d(this)
fun Quaterniond.copy() = Quaterniond(this)

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
    x = max(min, min(max, x))
    y = max(min, min(max, y))
    z = max(min, min(max, z))
}

fun Vector3d.clamp(min: Vector3dc, max: Vector3dc) = apply {
    x = max(min.x(), min(max.x(), x))
    y = max(min.y(), min(max.y(), y))
    z = max(min.z(), min(max.z(), z))
}

fun Vector3d.applyQuaternion(q: Quaterniondc): Vector3d {
    val x = this.x
    val y = this.y
    val z = this.z
    val qx = q.x()
    val qy = q.y()
    val qz = q.z()
    val qw = q.w()

    // calculate quat * vector

    val ix = qw * x + qy * z - qz * y
    val iy = qw * y + qz * x - qx * z
    val iz = qw * z + qx * y - qy * x
    val iw = -qx * x - qy * y - qz * z

    // calculate result * inverse quat

    this.x = ix * qw + iw * -qx + iy * -qz - iz * -qy
    this.y = iy * qw + iw * -qy + iz * -qx - ix * -qz
    this.z = iz * qw + iw * -qz + ix * -qy - iy * -qx

    return this
}

fun Vector3d.subVectors(v1: Vector3dc, v2: Vector3dc): Vector3d {
    return this.set(v1).sub(v2)
}

fun Vector3d.setFromSpherical(s: Spherical): Vector3d {
    return this.setFromSphericalCoords(s.radius, s.phi, s.theta)
}

fun Vector3d.setFromSphericalCoords(radius: Double, phi: Double, theta: Double): Vector3d {

    val sinPhiRadius = sin(phi) * radius

    this.x = sinPhiRadius * sin(theta)
    this.y = cos(phi) * radius
    this.z = sinPhiRadius * cos(theta)

    return this

}

@JvmOverloads
fun Vector3d.fromArray(array: DoubleArray, offset: Int = 0) = apply {
    x = array[offset + 0]
    y = array[offset + 1]
    z = array[offset + 2]
}

@JvmOverloads
fun Vector3dc.toArray(array: DoubleArray = DoubleArray(3), offset: Int = 0): DoubleArray {
    return array.also {
        it[0 + offset] = x()
        it[1 + offset] = y()
        it[2 + offset] = z()
    }
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

fun Quaterniond.setFromUnitVectors(vFrom: Vector3dc, vTo: Vector3dc): Quaterniond {
    // assumes direction vectors vFrom and vTo are normalized

    val EPS = 0.000001

    var r = vFrom.dot(vTo) + 1

    if (r < EPS) {

        r = 0.0

        if (abs(vFrom.x()) > abs(vFrom.z())) {

            this.x = -vFrom.y()
            this.y = vFrom.x()
            this.z = 0.0
            this.w = r

        } else {

            this.x = 0.0
            this.y = -vFrom.z()
            this.z = vFrom.y()
            this.w = r

        }

    } else {

        // crossVectors( vFrom, vTo ); // inlined to avoid cyclic dependency on Vector3

        this.x = vFrom.y() * vTo.z() - vFrom.z() * vTo.y()
        this.y = vFrom.z() * vTo.x() - vFrom.x() * vTo.z()
        this.z = vFrom.x() * vTo.y() - vFrom.y() * vTo.x()
        this.w = r

    }

    return this.normalize()
}
