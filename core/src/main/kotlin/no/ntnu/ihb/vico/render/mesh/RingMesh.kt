package no.ntnu.ihb.vico.render.mesh

import no.ntnu.ihb.vico.render.push
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class RingMesh(
    val innerRadius: Float = 0.25f,
    val outerRadius: Float = 0.5f,
    thetaSegments: Int = 20,
    phiSegments: Int = 5,
    val thetaStart: Float = 0f,
    val thetaLength: Float = (PI * 2).toFloat()
) : TrimeshShape {

    val thetaSegments = max(3, thetaSegments)
    val phiSegments = max(1, phiSegments)

    override val indices: List<Int>
    override val vertices: List<Float>
    override val normals: List<Float>
    override val uvs: List<Float>

    init {
        Helper().also { h ->
            indices = h.indices.toList()
            vertices = h.vertices.toList()
            normals = h.normals.toList()
            uvs = h.uvs.toList()
        }
    }

    private inner class Helper {

        val indices = mutableListOf<Int>()
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val uvs = mutableListOf<Float>()

        var radius = innerRadius
        val radiusStep = (outerRadius - innerRadius) / phiSegments
        val vertex = Vector3f()
        val uv = Vector2f()

        init {

            // generate vertices, normals and uvs
            for (j in 0..phiSegments) {
                for (i in 0..thetaSegments) {

                    // values are generate from the inside of the ring to the outside
                    val segment = thetaStart + i / thetaSegments.toFloat() * thetaLength

                    // vertex
                    vertex.x = radius * cos(segment)
                    vertex.y = radius * sin(segment)
                    vertices.push(vertex.x, vertex.y, vertex.z)

                    // normal
                    normals.push(0f, 0f, 1f)

                    // uv
                    uv.x = (vertex.x / outerRadius + 1) / 2f
                    uv.y = (vertex.y / outerRadius + 1) / 2f
                    uvs.push(uv.x, uv.y)
                }

                // increase the radius for next row of vertices
                radius += radiusStep
            }

            // indices
            for (j in 0 until phiSegments) {
                val thetaSegmentLevel = j * (thetaSegments + 1)
                for (i in 0 until thetaSegments) {
                    val segment = (i + thetaSegmentLevel).toFloat()
                    val a = segment.toInt()
                    val b = segment.toInt() + thetaSegments + 1
                    val c = segment.toInt() + thetaSegments + 2
                    val d = segment.toInt() + 1

                    // faces
                    indices.push(a, b, d)
                    indices.push(b, c, d)
                }
            }

        }

    }

}


