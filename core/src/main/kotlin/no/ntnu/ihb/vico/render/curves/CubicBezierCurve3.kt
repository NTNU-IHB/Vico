package info.laht.krender.curves

import org.joml.Vector3d

class CubicBezierCurve3(
    private val v0: Vector3d,
    private val v1: Vector3d,
    private val v2: Vector3d,
    private val v3: Vector3d
) : Curve3() {

    override fun getPoint(t: Double): Vector3d {
        return Vector3d(
            Interpolations.CubicBezier(t, v0.x(), v1.x(), v2.x(), v3.x()),
            Interpolations.CubicBezier(t, v0.y(), v1.y(), v2.y(), v3.y()),
            Interpolations.CubicBezier(t, v0.z(), v1.z(), v2.z(), v3.z())
        )
    }

}
