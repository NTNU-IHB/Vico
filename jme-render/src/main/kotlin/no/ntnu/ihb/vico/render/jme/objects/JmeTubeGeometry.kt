package no.ntnu.ihb.vico.render.jme.objects

/*import com.jme3.math.FastMath
import com.jme3.scene.Geometry
import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer
import org.joml.*
import kotlin.math.cos
import kotlin.math.sin


class JmeTubeGeometry(
    private val radius: Float,
    points: List<Vector3fc>,
    private val tubularSegments: Int,
    private val radialSegments: Int,
    private val closed: Boolean
) : Geometry() {

    private val h: Helper

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

        private lateinit var path: CatmullRomCurve3
        private lateinit var frames: Curve3.FrenetFrame3
        val indices = mutableListOf<Int>()
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val uvs = mutableListOf<Int>()
        private val vertex = Vector3f()
        private val normal = Vector3f()
        private val uv = Vector2f()

        fun update(points: List<Vector3fc>) {
            indices.clear()
            vertices.clear()
            normals.clear()
            uvs.clear()
            vertex.set(0f, 0f, 0f)
            normal.set(0f, 0f, 0f)
            uv.set(0f, 0f)
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
            val P = path.getPointAt(i.toFloat() / tubularSegments)
            val N = frames.normals[i]
            val B = frames.binormals[i]
            for (j in 0..radialSegments) {
                val v = j.toFloat() / radialSegments * FastMath.PI * 2
                val sin = sin(v)
                val cos = -cos(v)

                // normal
                normal.x = cos * N.x() + sin * B.x()
                normal.y = cos * N.y() + sin * B.y()
                normal.z = cos * N.z() + sin * B.z()
                normal.normalize()
                normals.add(normal.x())
                normals.add(normal.y())
                normals.add(normal.z())

                // vertex
                vertex.x = P.x() + radius * normal.x()
                vertex.y = P.y() + radius * normal.y()
                vertex.z = P.z() + radius * normal.z()
                vertices.add(vertex.x())
                vertices.add(vertex.y())
                vertices.add(vertex.z())
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
                    uv.x = (i.toFloat() / tubularSegments)
                    uv.y = (j.toFloat() / radialSegments)
                    uvs.add(uv.x().toInt())
                    uvs.add(uv.y().toInt())
                }
            }
        }
    }

    init {
        mesh = Mesh()
        h = Helper()
        update(points)
        mesh.mode = Mesh.Mode.Triangles
        mesh.updateBound()
        mesh.setDynamic()
    }
}
*/
