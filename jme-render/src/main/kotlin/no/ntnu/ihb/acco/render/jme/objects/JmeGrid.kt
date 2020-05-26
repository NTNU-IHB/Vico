package no.ntnu.ihb.acco.render.jme.objects

import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer


class JmeGrid @JvmOverloads constructor(
    private val width: Float,
    private val height: Float,
    private val widthSegments: Int = 10,
    private val heightSegments: Int = 10,
    heights: FloatArray? = null
) : Mesh() {

    private val h: GridHelper

    init {
        h = GridHelper()
        if (heights != null) {
            var i = 0
            var j = 0
            while (i < h.vertices.size) {
                h.vertices[i + 2] = heights[j++]
                i += 3
            }
        }
        setBuffer(VertexBuffer.Type.Index, 1, h.indices)
        setBuffer(VertexBuffer.Type.Position, 3, h.vertices)
        setBuffer(VertexBuffer.Type.Normal, 3, h.normals)
        setBuffer(VertexBuffer.Type.TexCoord, 2, h.uvs)
        mode = Mode.Triangles
        updateBound()
        setDynamic()
    }

    fun setHeights(heights: FloatArray?) {
        if (heights != null) {
            var i = 0
            var j = 0
            while (i < h.vertices.size) {
                h.vertices[i + 2] = heights[j++]
                i += 3
            }
            setBuffer(VertexBuffer.Type.Position, 3, h.vertices)
            updateBound()
        }
    }

    private inner class GridHelper {

        val indices: IntArray
        val vertices: FloatArray
        val normals: FloatArray
        val uvs: IntArray

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
            uvs = IntArray(gridX1 * gridY1 * 2)
            var offset = 0
            var offset2 = 0
            for (iy in 0 until gridY1) {
                val y = iy * segmentHeight - heightHalf
                for (ix in 0 until gridX1) {
                    val x = ix * segmentWidth - widthHalf
                    vertices[offset + 0] = x
                    vertices[offset + 1] = -y
                    normals[offset + 2] = 1f
                    uvs[offset2] = ix / gridX
                    uvs[offset2 + 1] = 1 - iy / gridY
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
