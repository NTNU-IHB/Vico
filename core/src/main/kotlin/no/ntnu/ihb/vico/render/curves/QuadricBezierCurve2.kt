package info.laht.krender.curves

import org.joml.Vector2d

class QuadricBezierCurve2(
    private val v0: Vector2d,
    private val v1: Vector2d,
    private val v2: Vector2d
) : Curve2() {

    override fun getPoint(t: Double): Vector2d {
        return Vector2d(
            Interpolations.QuadraticBezier(t, v0.x(), v1.x(), v2.x()),
            Interpolations.QuadraticBezier(t, v0.y(), v1.y(), v2.y())
        )
    }

}
