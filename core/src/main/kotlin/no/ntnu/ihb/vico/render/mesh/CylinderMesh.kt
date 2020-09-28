package info.laht.krender.mesh

import no.ntnu.ihb.vico.render.setXY
import no.ntnu.ihb.vico.render.setXYZ
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CylinderMesh(
    override val radius: Float = 0.5f,
    height: Float = 1f
) : CylindricalMesh(radius, radius, height), CylinderShape

class ConeMesh(
    val radius: Float = 0.5f,
    height: Float = 1f,
    radialSegments: Int = 32,
    heightSegments: Int = 8,
    openEnded: Boolean = false,
    thetaStart: Float = 0f,
    thetaLength: Float = (PI * 2).toFloat()
) : CylindricalMesh(0f, radius, height, radialSegments, heightSegments, openEnded, thetaStart, thetaLength)

open class CylindricalMesh(
    val radiusTop: Float = 0.5f,
    val radiusBottom: Float = 0.5f,
    val height: Float = 1f,
    val radialSegments: Int = 32,
    val heightSegments: Int = 8,
    val openEnded: Boolean = false,
    val thetaStart: Float = 0f,
    val thetaLength: Float = (PI * 2).toFloat()
) : TrimeshShape {

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

        val indices: IntArray
        val vertices: FloatArray
        val normals: FloatArray
        val uvs: FloatArray

        var index = 0
        var indexOffset = 0
        val indexArray: MutableList<List<Int>> = mutableListOf()
        val halfHeight = height / 2

        init {
            var nbCap = 0

            if (!openEnded) {
                if (radiusTop > 0) {
                    nbCap++
                }
                if (radiusBottom > 0) {
                    nbCap++
                }
            }

            val vertexCount = calculateVertexCount(nbCap)
            val indexCount = calculateIndexCount(nbCap)

            indices = IntArray(indexCount)
            vertices = FloatArray(vertexCount * 3)
            normals = FloatArray(vertexCount * 3)
            uvs = FloatArray(vertexCount * 2)

            generateTorso()

            if (!openEnded) {
                if (radiusTop > 0) {
                    generateCap(true)
                }
                if (radiusBottom > 0) {
                    generateCap(false)
                }
            }

        }

        private fun calculateVertexCount(nbCap: Int): Int {
            var count = (radialSegments + 1) * (heightSegments + 1)
            if (!openEnded) {
                count += (radialSegments + 1) * nbCap + radialSegments * nbCap
            }
            return count
        }

        private fun calculateIndexCount(nbCap: Int): Int {
            var count = radialSegments * heightSegments * 2 * 3
            if (!openEnded) {
                count += radialSegments * nbCap * 3
            }
            return count
        }

        private fun generateTorso() {

            val normal = Vector3f()
            val vertex = Vector3f()

            // this will be used to calculate the normal
            val slope = (radiusBottom - radiusTop) / height

            // generate vertices, normals and uvs
            for (y in 0..heightSegments) {

                val indexRow = mutableListOf<Int>()

                val v = y.toFloat() / heightSegments

                // calculate the radius of the current row
                val radius = v * (radiusBottom - radiusTop) + radiusTop

                for (x in 0..radialSegments) {

                    val u = x.toFloat() / radialSegments

                    val theta = u * thetaLength + thetaStart

                    val sinTheta = sin(theta)
                    val cosTheta = cos(theta)

                    // vertex
                    vertex.x = radius * sinTheta
                    vertex.y = -v * height + halfHeight
                    vertex.z = radius * cosTheta
                    vertices.setXYZ(index, vertex.x, vertex.y, vertex.z)

                    // normal
                    normal.set(sinTheta, slope, cosTheta).normalize()
                    normals.setXYZ(index, normal.x, normal.y, normal.z)

                    // uv
                    uvs.setXY(index, u, 1 - v)

                    // save index of vertex in respective row
                    indexRow.add(index++)

                }

                // now save vertices of the row in our index array
                indexArray.add(indexRow)

            }

            // generate indices
            for (x in 0 until radialSegments) {

                for (y in 0 until heightSegments) {

                    // we use the index array to access the correct indices
                    val i1 = indexArray[y][x]
                    val i2 = indexArray[y + 1][x]
                    val i3 = indexArray[y + 1][x + 1]
                    val i4 = indexArray[y][x + 1]

                    // face one
                    indices.set(indexOffset, i1)
                    indexOffset++
                    indices.set(indexOffset, i2)
                    indexOffset++
                    indices.set(indexOffset, i4)
                    indexOffset++

                    // face two
                    indices.set(indexOffset, i2)
                    indexOffset++
                    indices.set(indexOffset, i3)
                    indexOffset++
                    indices.set(indexOffset, i4)
                    indexOffset++

                }

            }

        }

        private fun generateCap(top: Boolean) {

            val uv = Vector2f()
            val vertex = Vector3f()

            val radius = if (top) radiusTop else radiusBottom
            val sign = if (top) 1 else -1

            // save the index of the first center vertex
            val centerIndexStart = index

            // first we generate the center vertex data of the cap.
            // because the geometry needs one set of uvs per face,
            // we must generate a center vertex per face/segment
            for (x in 1..radialSegments) {

                // vertex
                vertices.setXYZ(index, 0f, halfHeight * sign, 0f)

                // normal
                normals.setXYZ(index, 0f, sign.toFloat(), 0f)

                // uv
                uv.x = 0.5f
                uv.y = 0.5f

                uvs.setXY(index, uv.x, uv.y)

                // increase index
                index++

            }

            // save the index of the last center vertex
            val centerIndexEnd = index

            // now we generate the surrounding vertices, normals and uvs
            for (x in 0..radialSegments) {

                val u = x.toFloat() / radialSegments
                val theta = u * thetaLength + thetaStart

                val cosTheta = cos(theta)
                val sinTheta = sin(theta)

                // vertex
                vertex.x = radius * sinTheta
                vertex.y = halfHeight * sign
                vertex.z = radius * cosTheta
                vertices.setXYZ(index, vertex.x, vertex.y, vertex.z)

                // normal
                normals.setXYZ(index, 0f, sign.toFloat(), 0f)

                // uv
                uv.x = cosTheta * 0.5f + 0.5f
                uv.y = sinTheta * 0.5f * sign.toFloat() + 0.5f
                uvs.setXY(index, uv.x, uv.y)

                // increase index
                index++

            }

            // generate indices
            for (x in 0 until radialSegments) {

                val c = centerIndexStart + x
                val i = centerIndexEnd + x

                if (top) {

                    // face top
                    indices.set(indexOffset, i)
                    indexOffset++
                    indices.set(indexOffset, i + 1)
                    indexOffset++
                    indices.set(indexOffset, c)
                    indexOffset++

                } else {

                    // face bottom
                    indices.set(indexOffset, i + 1)
                    indexOffset++
                    indices.set(indexOffset, i)
                    indexOffset++
                    indices.set(indexOffset, c)
                    indexOffset++

                }

            }

        }

    }

}
