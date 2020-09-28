package info.laht.krender.mesh

open class PlaneMesh(
    override val width: Float = 1f,
    override val height: Float = 1f,
    private val widthSegments: Int = 1,
    private val heightSegments: Int = 1
) : TrimeshShape, PlaneShape {

    override val indices: List<Int>
    protected val vertices_: MutableList<Float>
    override val vertices: List<Float>
        get() {
            return vertices_
        }
    override val normals: List<Float>
    override val uvs: List<Float>

    init {
        Helper().also { h ->
            indices = h.indices.toList()
            vertices_ = h.vertices.toMutableList()
            normals = h.normals.toList()
            uvs = h.uvs.toList()
        }
    }

    private inner class Helper {

        val indices: IntArray
        val vertices: FloatArray
        val normals: FloatArray
        val uvs: FloatArray

        init {

            val widthHalf = width / 2
            val heightHalf = height / 2

            val gridX = widthSegments
            val gridY = heightSegments

            val gridX1 = gridX + 1
            val gridY1 = gridY + 1

            val segmentWidth = width / gridX
            val segmentHeight = height / gridY

            vertices = FloatArray(gridX1 * gridY1 * 3)
            normals = FloatArray(gridX1 * gridY1 * 3)
            uvs = FloatArray(gridX1 * gridY1 * 2)

            var offset = 0
            var offset2 = 0

            for (iy in 0 until gridY1) {

                val y = iy * segmentHeight - heightHalf

                for (ix in 0 until gridX1) {

                    val x = ix * segmentWidth - widthHalf

                    vertices[offset + 0] = x
                    vertices[offset + 1] = -y

                    normals[offset + 2] = 1f

                    uvs[offset2 + 0] = (ix / gridX).toFloat()
                    uvs[offset2 + 1] = (1 - iy / gridY).toFloat()

                    offset += 3
                    offset2 += 2

                }

            }

            offset = 0

            indices = IntArray(gridX * gridY * 6)

            for (iy in 0 until gridY) {

                for (ix in 0 until gridX) {

                    val a = ix + gridX1 * iy
                    val b = ix + gridX1 * (iy + 1)
                    val c = ix + 1 + gridX1 * (iy + 1)
                    val d = ix + 1 + gridX1 * iy

                    indices[offset + 0] = a
                    indices[offset + 1] = b
                    indices[offset + 2] = d

                    indices[offset + 3] = b
                    indices[offset + 4] = c
                    indices[offset + 5] = d

                    offset += 6

                }

            }

        }

    }

}

class TerrainMesh(
    override val width: Float = 1f,
    override val height: Float = 1f,
    override val widthSegments: Int = 1,
    override val heightSegments: Int = 1,
    override val heights: FloatArray
) : PlaneMesh(width, height, widthSegments, heightSegments), HeightmapShape {

    init {

        val expectedSize = (widthSegments + 1) * (heightSegments + 1)
        require(heights.size == expectedSize)

        {
            /* var i = 0
             var j = 0
             while (i < vertices_.size) {
                 vertices_[i + 2] = heights[j++]
                 i += 3
             }*/
        }

    }

}
