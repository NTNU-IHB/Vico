package info.laht.acco.render.shape

import info.laht.acco.math.Vector3


sealed class Shape

data class SphereShape @JvmOverloads constructor(
    var radius: Float = 0.5f
) : Shape()

data class CapsuleShape @JvmOverloads constructor(
    var radius: Float = 0.5f,
    var height: Float = 1f
) : Shape()

data class PlaneShape @JvmOverloads constructor(
    var width: Float = 1f,
    var height: Float = 1f
) : Shape()

data class BoxShape @JvmOverloads constructor(
    val extents: Vector3 = Vector3(1.0, 1.0, 1.0)
) : Shape() {

    val width: Float
        get() = extents.x.toFloat()

    val height: Float
        get() = extents.y.toFloat()

    val depth: Float
        get() = extents.z.toFloat()

    constructor(extents: Float) : this(Vector3(extents.toDouble()))
    constructor(width: Float, height: Float, depth: Float) : this(
        Vector3(
            width.toDouble(),
            height.toDouble(),
            depth.toDouble()
        )
    )

}
