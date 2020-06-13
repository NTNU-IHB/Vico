package no.ntnu.ihb.acco.render.shape

import org.joml.Vector3f


sealed class Shape

data class SphereShape @JvmOverloads constructor(
    var radius: Float = 0.5f
) : Shape()

data class CylinderShape @JvmOverloads constructor(
    var radius: Float = 0.5f,
    var height: Float = 1f
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
    val extents: Vector3f = Vector3f(1f, 1f, 1f)
) : Shape() {

    val width: Float
        get() = extents.x.toFloat()

    val height: Float
        get() = extents.y.toFloat()

    val depth: Float
        get() = extents.z.toFloat()

    constructor(extents: Float) : this(Vector3f(extents))
    constructor(width: Float, height: Float, depth: Float) : this(Vector3f(width, height, depth))

}
