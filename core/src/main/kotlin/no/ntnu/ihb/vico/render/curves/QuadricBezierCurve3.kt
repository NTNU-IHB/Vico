package info.laht.krender.curves

import org.joml.Vector3d

class QuadricBezierCurve3(
    private val v0: Vector3d,
    private val v1: Vector3d,
    private val v2: Vector3d
) : Curve3() {

    override fun getPoint(t: Double): Vector3d {
        return Vector3d(
            Interpolations.QuadraticBezier(t, v0.x(), v1.x(), v2.x()),
            Interpolations.QuadraticBezier(t, v0.y(), v1.y(), v2.y()),
            Interpolations.QuadraticBezier(t, v0.z(), v1.z(), v2.z())
        )
    }

}
