package info.laht.krender.curves

import org.joml.Vector3d

class LineCurve3(
    private val v1: Vector3d,
    private val v2: Vector3d
) : Curve3() {

    override fun getPoint(t: Double): Vector3d {
        if (t == 1.0) {
            return Vector3d(v2)
        }
        return Vector3d().apply {
            sub(v2, v1)
            mul(t)
            add(v1)
        }
    }

}
