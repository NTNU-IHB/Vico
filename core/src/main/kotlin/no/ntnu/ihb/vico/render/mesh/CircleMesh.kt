package info.laht.krender.mesh

import no.ntnu.ihb.vico.render.push
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class CircleMesh(
    radius: Float
) : CircularMesh(radius), CircleShape

sealed class CircularMesh(
    val radius: Float = 1f,
    segments: Int? = null,
    val thetaStart: Float = 0f,
    val thetaLength: Float = (PI * 2).toFloat()
) : TrimeshShape {

    override val indices: List<Int>
    override val vertices: List<Float>
    override val normals: List<Float>
    override val uvs: List<Float>

    val segments = if (segments != null) max(3, segments) else 16

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

        init {

            val vertex = Vector3f()
            val uv = Vector2f()

            // center point

            vertices.push(0f, 0f, 0f)
            normals.push(0f, 0f, 1f)
            uvs.push(0.5f, 0.5f)

            var s = 0
            var i = 3
            while (s <= segments) {

                val segment = thetaStart + s / segments.toFloat() * thetaLength

                // vertex

                vertex.x = radius * cos(segment)
                vertex.y = radius * sin(segment)

                vertices.push(vertex.x, vertex.y, vertex.z)

                // normal

                normals.push(0f, 0f, 1f)

                // uvs

                uv.x = (vertices[i] / radius + 1) / 2f
                uv.y = (vertices[i + 1] / radius + 1) / 2f

                uvs.push(uv.x, uv.y)

                s++
                i += 3
            }

            // indices

            for (j in 1..segments) {

                indices.push(j, j + 1, 0)

            }

        }

    }

}
