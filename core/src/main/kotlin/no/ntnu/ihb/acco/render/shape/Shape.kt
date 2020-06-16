package no.ntnu.ihb.acco.render.shape

import org.joml.Vector3d


sealed class Shape

data class SphereShape @JvmOverloads constructor(
    var radius: Double = 0.5
) : Shape()

data class CylinderShape @JvmOverloads constructor(
    var radius: Double = 0.5,
    var height: Double = 1.0
) : Shape()

data class CapsuleShape @JvmOverloads constructor(
    var radius: Double = 0.5,
    var height: Double = 1.0
) : Shape()

data class PlaneShape @JvmOverloads constructor(
    var width: Double = 1.0,
    var height: Double = 1.0
) : Shape()

data class BoxShape @JvmOverloads constructor(
    val extents: Vector3d = Vector3d(1.0, 1.0, 1.0)
) : Shape() {

    val width: Double
        get() = extents.x

    val height: Double
        get() = extents.y

    val depth: Double
        get() = extents.z

    constructor(extents: Double) : this(Vector3d(extents))
    constructor(width: Double, height: Double, depth: Double) : this(Vector3d(width, height, depth))

}


class TrimeshShape private constructor(
    builder: Builder
) : Shape() {

    val indices = builder.indices.toIntArray()
    val vertices = builder.vertices.toFloatArray()
    val normals = builder.normals.toFloatArray()
    val colors = builder.colors.toFloatArray()
    val uvs = builder.uvs.toFloatArray()

    val scale = builder.scale

    class Builder {

        internal val indices = mutableListOf<Int>()
        internal val vertices = mutableListOf<Float>()
        internal val normals = mutableListOf<Float>()
        internal val colors = mutableListOf<Float>()
        internal val uvs = mutableListOf<Float>()

        var scale = 1.0

        fun indices(indices: List<Int>) = apply {
            this.indices.addAll(indices)
        }

        fun indices(vararg indices: Int): Builder = apply {
            this.indices.addAll(indices.toList())
        }

        fun vertices(indices: List<Float>) = apply {
            vertices.addAll(indices)
        }

        fun vertices(vararg vertices: Float) = apply {
            this.vertices.addAll(vertices.toList())
        }

        fun normals(normals: List<Float>) = apply {
            this.normals.addAll(normals)
        }

        fun normals(vararg normals: Float) = apply {
            this.normals.addAll(normals.toList())
        }

        fun colors(uvs: List<Float>) = apply {
            colors.addAll(uvs)
        }

        fun colors(vararg uvs: Float) = apply {
            colors.addAll(uvs.toList())
        }

        fun uvs(uvs: List<Float>) = apply {
            this.uvs.addAll(uvs)
        }

        fun uvs(vararg uvs: Float) = apply {
            this.uvs.addAll(uvs.toList())
        }

        fun scale(value: Double) = apply {
            this.scale = value
        }

        fun build(): TrimeshShape {
            return TrimeshShape(this)
        }

    }

    fun hasIndices(): Boolean {
        return indices.isNotEmpty()
    }

    fun hasVertices(): Boolean {
        return vertices.isNotEmpty()
    }

    fun hasNormals(): Boolean {
        return normals.isNotEmpty()
    }

    fun hasColors(): Boolean {
        return colors.isNotEmpty()
    }

    fun hasUvs(): Boolean {
        return uvs.isNotEmpty()
    }

}
