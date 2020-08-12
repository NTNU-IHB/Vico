package no.ntnu.ihb.vico.shapes

import org.joml.Vector3f


sealed class Shape

data class SphereShape @JvmOverloads constructor(
        var radius: Float = 0.5f
) : Shape()

data class CylinderShape @JvmOverloads constructor(
        var radius: Float = 0.5f,
        var height: Float = 1.0f
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
        get() = extents.x

    val height: Float
        get() = extents.y

    val depth: Float
        get() = extents.z

    constructor(extents: Float) : this(Vector3f(extents))
    constructor(width: Float, height: Float, depth: Float) : this(Vector3f(width, height, depth))

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

        var scale = 1f

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

        fun scale(value: Float) = apply {
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
