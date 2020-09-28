package info.laht.krender.curves

import org.joml.Vector2d

class LineCurve2(
    private val v1: Vector2d,
    private val v2: Vector2d
) : Curve2() {

    override fun getPoint(t: Double): Vector2d {
        if (t == 1.0) {
            return Vector2d(v2)
        }
        return Vector2d(v2).sub(v1).apply {
            mul(t).add(v1)
        }
    }

}
