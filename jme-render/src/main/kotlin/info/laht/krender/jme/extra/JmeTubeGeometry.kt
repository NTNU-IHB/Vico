package info.laht.krender.jme.extra

import com.jme3.scene.Geometry
import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer
import info.laht.krender.curves.CatmullRomCurve3
import info.laht.krender.curves.Curve3
import org.joml.Vector2d
import org.joml.Vector3d
import org.joml.Vector3fc

class JmeTubeGeometry(
    private val radius: Float,
    points: List<Vector3fc>,
    private val tubularSegments: Int,
    private val radialSegments: Int,
    private val closed: Boolean
) : Geometry() {

    private val h: Helper

    init {
        mesh = Mesh()
        h = Helper()
        update(points)
        mesh.mode = Mesh.Mode.Triangles
        mesh.updateBound()
        mesh.setDynamic()
    }

    fun update(points: List<Vector3fc>) {
        h.update(points)
        mesh.setBuffer(VertexBuffer.Type.Index, 1, h.indices.toIntArray())
        mesh.setBuffer(VertexBuffer.Type.Position, 3, h.vertices.toFloatArray())
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, h.normals.toFloatArray())
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, h.uvs.toIntArray())
        mesh.updateCounts()
        mesh.updateBound()
        updateModelBound()
    }

    private inner class Helper {

        val indices: MutableList<Int> = mutableListOf()
        val vertices: MutableList<Float> = mutableListOf()
        val normals: MutableList<Float> = mutableListOf()
        val uvs: MutableList<Int> = mutableListOf()

        private lateinit var path: CatmullRomCurve3
        private lateinit var frames: Curve3.FrenetFrame3
        private val vertex = Vector3d()
        private val normal = Vector3d()
        private val uv = Vector2d()

        fun update(points: List<Vector3fc>) {
            indices.clear()
            vertices.clear()
            normals.clear()
            uvs.clear()
            vertex[0.0, 0.0] = 0.0
            normal[0.0, 0.0] = 0.0
            uv[0.0] = 0.0
            path = CatmullRomCurve3(points, CatmullRomCurve3.Type.CENTRIPETAL)
            frames = path.computeFrenetFrames(tubularSegments, closed)
            generateBufferData()
        }

        private fun generateBufferData() {
            for (i in 0 until tubularSegments) {
                generateSegment(i)
            }
            generateSegment(if (!closed) tubularSegments else 0)
            generateUVs()
            generateIndices()
        }

        private fun generateSegment(i: Int) {
            val P: Vector3d = path.getPointAt(i.toDouble() / tubularSegments)
            val N: Vector3d = frames.normals[i]
            val B: Vector3d = frames.binormals[i]
            for (j in 0..radialSegments) {
                val v = j.toDouble() / radialSegments * Math.PI * 2
                val sin = Math.sin(v)
                val cos = -Math.cos(v)

                // normal
                normal.x = cos * N.x() + sin * B.x()
                normal.y = cos * N.y() + sin * B.y()
                normal.z = cos * N.z() + sin * B.z()
                normal.normalize()
                normals.add(normal.x().toFloat())
                normals.add(normal.y().toFloat())
                normals.add(normal.z().toFloat())

                // vertex
                vertex.x = P.x() + radius * normal.x()
                vertex.y = P.y() + radius * normal.y()
                vertex.z = P.z() + radius * normal.z()
                vertices.add(vertex.x().toFloat())
                vertices.add(vertex.y().toFloat())
                vertices.add(vertex.z().toFloat())
            }
        }

        private fun generateIndices() {
            for (j in 1..tubularSegments) {
                for (i in 1..radialSegments) {
                    val a = (radialSegments + 1) * (j - 1) + (i - 1)
                    val b = (radialSegments + 1) * j + (i - 1)
                    val c = (radialSegments + 1) * j + i
                    val d = (radialSegments + 1) * (j - 1) + i

                    // faces
                    indices.add(a)
                    indices.add(b)
                    indices.add(d)
                    indices.add(b)
                    indices.add(c)
                    indices.add(d)
                }
            }
        }

        private fun generateUVs() {
            for (i in 0..tubularSegments) {
                for (j in 0..radialSegments) {
                    uv.x = (i / tubularSegments).toDouble()
                    uv.y = (j / radialSegments).toDouble()
                    uvs.add(uv.x().toInt())
                    uvs.add(uv.y().toInt())
                }
            }
        }
    }

}
