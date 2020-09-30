package info.laht.krender.threekt

import info.laht.threekt.math.Matrix4
import info.laht.threekt.math.Quaternion
import info.laht.threekt.math.Vector3
import org.joml.Matrix4fc
import org.joml.Quaternionfc
import org.joml.Vector3d
import org.joml.Vector3fc

internal fun Vector3.set(v: Vector3fc): Vector3 {
    return set(v.x(), v.y(), v.z())
}

internal fun Quaternion.set(q: Quaternionfc): Quaternion {
    return set(q.x(), q.y(), q.z(), q.w())
}

internal fun Matrix4.set(m: Matrix4fc): Matrix4 {
    return set(
        m.m00(), m.m10(), m.m20(), m.m30(),
        m.m01(), m.m11(), m.m21(), m.m31(),
        m.m02(), m.m12(), m.m22(), m.m32(),
        m.m03(), m.m13(), m.m23(), m.m33()
    )
}

internal fun Vector3d.set(v: Vector3): Vector3d {
    return Vector3d(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
}

internal fun List<Vector3fc>.flatten(): FloatArray {
    var i = 0
    val array = FloatArray(3 * size)
    for (v in this) {
        array[i++] = v.x()
        array[i++] = v.y()
        array[i++] = v.z()
    }
    return array
}

