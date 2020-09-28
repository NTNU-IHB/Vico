package info.laht.krender.mesh

import info.laht.krender.util.JomlUtil.fromArray
import org.joml.*
import java.util.*

class Trimesh(
    override val indices: MutableList<Int> = mutableListOf(),
    override val vertices: MutableList<Float> = mutableListOf(),
    override val normals: MutableList<Float> = mutableListOf(),
    override val uvs: MutableList<Float> = mutableListOf()
) : TrimeshShape {

    fun hasIndices(): Boolean {
        return indices.isNotEmpty()
    }

    fun hasVertices(): Boolean {
        return vertices.isNotEmpty()
    }

    fun hasNormals(): Boolean {
        return normals.isNotEmpty()
    }

    fun hasUvs(): Boolean {
        return uvs.isNotEmpty()
    }

    fun indices(indices: List<Int>) = apply {
        this.indices.addAll(indices)
    }

    fun indices(indices: IntArray) = apply {
        indices.forEach { this.indices.add(it) }
    }

    fun vertices(vertices: List<Float>) = apply {
        this.vertices.addAll(vertices)
    }

    fun vertices(vertices: FloatArray) = apply {
        vertices.forEach { this.vertices.add(it) }
    }

    fun normals(normals: List<Float>) = apply {
        this.normals.addAll(normals)
    }

    fun normals(normals: FloatArray) = apply {
        normals.forEach { this.normals.add(it) }
    }

    fun uvs(uvs: List<Float>) = apply {
        this.uvs.addAll(uvs)
    }

    fun uvs(uvs: FloatArray) = apply {
        uvs.forEach { this.uvs.add(it) }
    }

    fun scale(scale: Float) = apply {
        var i = 0
        while (i < vertices.size) {
            vertices[i + 0] = vertices[i + 0] * scale
            vertices[i + 1] = vertices[i + 1] * scale
            vertices[i + 2] = vertices[i + 2] * scale
            i += 3
        }
    }

    @JvmOverloads
    fun generateIndices(force: Boolean = false) = apply {
        if (indices.isEmpty() || force) {
            indices.clear()
            for (i in 0 until vertices.size / 3) {
                indices.add(i)
            }
        }
        return this
    }


    fun rotateX(angle: Float) = apply {
        return applyMatrix4(Matrix4f().rotate(angle, 1f, 0f, 0f))
    }

    fun rotateY(angle: Float) = apply {
        return applyMatrix4(Matrix4f().rotate(angle, 0f, 1f, 0f))
    }

    fun rotateZ(angle: Float) = apply {
        return applyMatrix4(Matrix4f().rotate(angle, 0f, 0f, 1f))
    }

    fun translateX(x: Float) = apply {
        return applyMatrix4(Matrix4f().setTranslation(x, 0f, 0f))
    }

    fun translateY(y: Float) = apply {
        return applyMatrix4(Matrix4f().setTranslation(0f, y, 0f))
    }

    fun translateZ(z: Float) = apply {
        return applyMatrix4(Matrix4f().setTranslation(0f, 0f, z))
    }

    fun translate(x: Float, y: Float, z: Float) = apply {
        return applyMatrix4(Matrix4f().setTranslation(x, y, z))
    }

    fun applyMatrix4(m: Matrix4fc) = apply {

        val v = Vector3f()
        for (i in 0 until vertices.size / 3) {
            val index = i * 3
            v[vertices[index + 0], vertices[index + 1]] = vertices[index + 2]
            v.mulPosition(m)
            vertices[index + 0] = v.x
            vertices[index + 1] = v.y
            vertices[index + 2] = v.z
        }

        if (hasNormals()) {
            val normalMatrix = m.normal(Matrix3f())
            var i = 0
            while (i < normals.size) {
                v.set(normals[i], normals[i + 1], normals[i + 2]).mul(normalMatrix)
                normals[i + 0] = v.x
                normals[i + 1] = v.y
                normals[i + 2] = v.z
                i += 3
            }
        }

    }

    fun computeVertexNormals() = apply {
        if (vertices.isNotEmpty()) {
            normals.clear()
            for (i in vertices.indices) {
                normals.add(0f)
            }
            var vA: Int
            var vB: Int
            var vC: Int
            val pA = Vector3f()
            val pB = Vector3f()
            val pC = Vector3f()
            val cb = Vector3f()
            val ab = Vector3f()

            // indexed elements
            if (indices.isNotEmpty()) {
                val start = 0
                val count = indices.size
                var i = start
                val il = start + count
                while (i < il) {
                    vA = indices[i + 0] * 3
                    vB = indices[i + 1] * 3
                    vC = indices[i + 2] * 3
                    pA.fromArray(vertices, vA)
                    pB.fromArray(vertices, vB)
                    pC.fromArray(vertices, vC)
                    cb.set(pC).sub(pB)
                    ab.set(pA).sub(pB)
                    cb.cross(ab)
                    normals[vA + 0] = normals[vA + 0] + cb.x
                    normals[vA + 1] = normals[vA + 1] + cb.y
                    normals[vA + 2] = normals[vA + 2] + cb.z
                    normals[vB + 0] = normals[vB + 0] + cb.x
                    normals[vB + 1] = normals[vB + 1] + cb.y
                    normals[vB + 2] = normals[vB + 2] + cb.z
                    normals[vC + 0] = normals[vC + 0] + cb.x
                    normals[vC + 1] = normals[vC + 1] + cb.y
                    normals[vC + 2] = normals[vC + 2] + cb.z
                    i += 3
                }
            } else {

                // non-indexed elements (unconnected triangle soup)
                var i = 0
                val il = vertices.size
                while (i < il) {
                    pA.fromArray(vertices, i + 0)
                    pB.fromArray(vertices, i + 3)
                    pC.fromArray(vertices, i + 6)
                    cb.set(pC).sub(pB)
                    ab.set(pA).sub(pB)
                    cb.cross(ab)
                    normals[i + 0] = cb.x
                    normals[i + 1] = cb.y
                    normals[i + 2] = cb.z
                    normals[i + 3] = cb.x
                    normals[i + 4] = cb.y
                    normals[i + 5] = cb.z
                    normals[i + 6] = cb.x
                    normals[i + 7] = cb.y
                    normals[i + 8] = cb.z
                    i += 9
                }
            }
            this.normalizeNormals()
        }
    }

    private fun normalizeNormals() {
        val vector = Vector3f()
        var i = 0
        val il = normals.size / 3
        while (i < il) {
            val index = i * 3
            vector.x = normals[index + 0]
            vector.y = normals[index + 1]
            vector.z = normals[index + 2]
            vector.normalize()
            normals[index + 0] = vector.x
            normals[index + 1] = vector.y
            normals[index + 2] = vector.z
            i++
        }
    }

    fun getVolume(): Float {
        val faces: MutableList<Face> = ArrayList()
        val vertex: MutableList<Vector3f> = ArrayList()

        var i = 0
        while (i < vertices.size) {
            vertex.add(Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]))
            i += 3
        }

        i = 0
        while (i < indices.size) {
            faces.add(Face(indices[i], indices[i + 1], indices[i + 2]))
            i += 3
        }

        return faces.map { f ->
            signedVolumeOfTriangle(vertex[f.a], vertex[f.b], vertex[f.c])
        }.sum()
    }

    private fun signedVolumeOfTriangle(p1: Vector3fc, p2: Vector3fc, p3: Vector3fc): Float {
        val v321 = p3.x() * p2.y() * p1.z()
        val v231 = p2.x() * p3.y() * p1.z()
        val v312 = p3.x() * p1.y() * p2.z()
        val v132 = p1.x() * p3.y() * p2.z()
        val v213 = p2.x() * p1.y() * p3.z()
        val v123 = p1.x() * p2.y() * p3.z()
        return 1.0f / 6.0f * (-v321 + v231 + v312 - v132 - v213 + v123)
    }

    private class Face(
        val a: Int,
        val b: Int,
        val c: Int
    )

}
