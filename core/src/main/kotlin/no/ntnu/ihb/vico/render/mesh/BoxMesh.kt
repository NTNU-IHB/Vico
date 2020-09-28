package info.laht.krender.mesh

import org.joml.Vector3f

class BoxMesh(
    override var width: Float = 1f,
    override var height: Float = 1f,
    override var depth: Float = 1f,
    private var widthSegments: Int = 1,
    private var heightSegments: Int = 1,
    private var depthSegments: Int = 1
) : TrimeshShape, BoxShape {

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

        // offset variables
        var vertexBufferOffset = 0
        var uvBufferOffset = 0
        var indexBufferOffset = 0
        var numberOfVertices = 0

        val vertexCount = calculateVertexCount(widthSegments, heightSegments, depthSegments)
        val indexCount = calculateIndexCount(widthSegments, heightSegments, depthSegments)

        val indices = IntArray(indexCount)
        val vertices = FloatArray(vertexCount * 3)
        val normals = FloatArray(vertexCount * 3)
        val uvs = FloatArray(vertexCount * 2)

        init {

            // build each side of the box geometry
            buildPlane(2, 1, 0, -1, -1, depth, height, width, depthSegments, heightSegments) // px
            buildPlane(2, 1, 0, 1, -1, depth, height, -width, depthSegments, heightSegments) // nx
            buildPlane(0, 2, 1, 1, 1, width, depth, height, widthSegments, depthSegments) // py
            buildPlane(0, 2, 1, 1, -1, width, depth, -height, widthSegments, depthSegments) // ny
            buildPlane(0, 1, 2, 1, -1, width, height, depth, widthSegments, heightSegments) // pz
            buildPlane(0, 1, 2, -1, -1, width, height, -depth, widthSegments, heightSegments) // nz

        }

        fun buildPlane(
            u: Int,
            v: Int,
            w: Int,
            udir: Int,
            vdir: Int,
            width: Float,
            height: Float,
            depth: Float,
            gridX: Int,
            gridY: Int
        ) {

            val segmentWidth = width / gridX
            val segmentHeight = height / gridY

            val widthHalf = width / 2
            val heightHalf = height / 2
            val depthHalf = depth / 2

            val gridX1 = gridX + 1
            val gridY1 = gridY + 1

            var vertexCounter = 0

            val vector = Vector3f()

            // generate vertices, normals and uvs
            for (iy in 0 until gridY1) {

                val y = iy * segmentHeight - heightHalf

                for (ix in 0 until gridX1) {

                    val x = ix * segmentWidth - widthHalf

                    // set values to correct vector component
                    vector.setComponent(u, x * udir)
                    vector.setComponent(v, y * vdir)
                    vector.setComponent(w, depthHalf)

                    // now apply vector to vertex buffer
                    vertices[vertexBufferOffset] = vector.x
                    vertices[vertexBufferOffset + 1] = vector.y
                    vertices[vertexBufferOffset + 2] = vector.z

                    // set values to correct vector component
                    vector.setComponent(u, 0f)
                    vector.setComponent(v, 0f)
                    vector.setComponent(w, if (depth > 0) 1f else -1f)

                    // now apply vector to normal buffer
                    normals[vertexBufferOffset] = vector.x
                    normals[vertexBufferOffset + 1] = vector.y
                    normals[vertexBufferOffset + 2] = vector.z

                    // uvs
                    uvs[uvBufferOffset] = ix.toFloat() / gridX
                    uvs[uvBufferOffset + 1] = (1 - iy).toFloat() / gridY

                    // update offsets and counters
                    vertexBufferOffset += 3
                    uvBufferOffset += 2
                    vertexCounter += 1

                }

            }

            // 1. you need three indices to draw a single face
            // 2. a single segment consists of two faces
            // 3. so we need to generate six (2*3) indices per segment
            for (iy in 0 until gridY) {

                for (ix in 0 until gridX) {

                    // indices
                    val a = numberOfVertices + ix + gridX1 * iy
                    val b = numberOfVertices + ix + gridX1 * (iy + 1)
                    val c = numberOfVertices + (ix + 1) + gridX1 * (iy + 1)
                    val d = numberOfVertices + (ix + 1) + gridX1 * iy

                    // face one
                    indices[indexBufferOffset] = a
                    indices[indexBufferOffset + 1] = b
                    indices[indexBufferOffset + 2] = d

                    // face two
                    indices[indexBufferOffset + 3] = b
                    indices[indexBufferOffset + 4] = c
                    indices[indexBufferOffset + 5] = d

                    // update offsets and counters
                    indexBufferOffset += 6

                }

            }

            // update total number of vertices
            numberOfVertices += vertexCounter

        }

        fun calculateIndexCount(w: Int, h: Int, d: Int): Int {

            var index = 0

            // calculate the amount of squares for each side
            index += w * h * 2 // xy
            index += w * d * 2 // xz
            index += d * h * 2 // zy

            return index * 6 // two triangles per square => six vertices per square

        }

        fun calculateVertexCount(w: Int, h: Int, d: Int): Int {

            var vertices = 0

            // calculate the amount of vertices for each side (plane)
            vertices += (w + 1) * (h + 1) * 2 // xy
            vertices += (w + 1) * (d + 1) * 2 // xz
            vertices += (d + 1) * (h + 1) * 2 // zy

            return vertices

        }

    }

}
