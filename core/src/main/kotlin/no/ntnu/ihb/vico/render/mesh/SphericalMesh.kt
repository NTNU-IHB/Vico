package no.ntnu.ihb.vico.render.mesh

import no.ntnu.ihb.vico.render.setXY
import no.ntnu.ihb.vico.render.setXYZ
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SphereMesh(
    radius: Float = 0.5f
) : SphericalMesh(radius), SphereShape

open class SphericalMesh(
    val radius: Float = 0.5f,
    val widthSegments: Int = 32,
    val heightSegments: Int = 32,
    val phiStart: Float = 0f,
    val phiLength: Float = (PI * 2).toFloat(),
    val thetaStart: Float = 0f,
    val thetaLength: Float = PI.toFloat()
) : TrimeshShape {

    override val indices: List<Int>
    override val vertices: List<Float>
    override val normals: List<Float>
    override val uvs: List<Float>

    init {
        Helper().also { h ->
            indices = h.indices
            vertices = h.vertices.toList()
            normals = h.normals.toList()
            uvs = h.uvs.toList()
        }
    }

    private inner class Helper {

        var indices: MutableList<Int>
        var vertices: FloatArray
        var normals: FloatArray
        var uvs: FloatArray

        init {
            val thetaEnd = thetaStart + thetaLength

            val vertexCount = (widthSegments + 1) * (heightSegments + 1)

            vertices = FloatArray(vertexCount * 3)
            normals = FloatArray(vertexCount * 3)
            uvs = FloatArray(vertexCount * 2)

            var index = 0
            val normal = Vector3f()

            val vertices = mutableListOf<List<Int>>()

            for (y in 0..heightSegments) {

                val verticesRow = mutableListOf<Int>()
                val v = y.toFloat() / heightSegments

                for (x in 0..widthSegments) {

                    val u = x.toFloat() / widthSegments

                    val px =
                        -radius * cos(phiStart + u * phiLength) * sin(thetaStart + v * thetaLength)
                    val py = radius * cos(thetaStart + v * thetaLength)
                    val pz =
                        radius * sin(phiStart + u * phiLength) * sin(thetaStart + v * thetaLength)

                    normal.set(px, py, pz).normalize()

                    this.vertices.setXYZ(index, px, py, pz)
                    normals.setXYZ(index, normal.x, normal.y, normal.z)
                    uvs.setXY(index, u, 1 - v)

                    verticesRow.add(index)

                    index++

                }

                vertices.add(verticesRow)

            }

            indices = mutableListOf()

            for (y in 0 until heightSegments) {

                for (x in 0 until widthSegments) {

                    val v1 = vertices[y][x + 1]
                    val v2 = vertices[y][x]
                    val v3 = vertices[y + 1][x]
                    val v4 = vertices[y + 1][x + 1]

                    if (y != 0 || thetaStart > 0) {
                        indices.add(v1)
                        indices.add(v2)
                        indices.add(v4)
                    }
                    if (y != heightSegments - 1 || thetaEnd < PI) {
                        indices.add(v2)
                        indices.add(v3)
                        indices.add(v4)
                    }

                }

            }
        }

    }

}
