package info.laht.krender.loaders

import info.laht.krender.mesh.Trimesh
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * https://github.com/mrdoob/three.js/blob/master/examples/js/loaders/STLLoader.js
 * (r86)
 *
 * @author laht
 */
class StlLoader {

    val supportedExtension: String
        get() = "stl"

    fun load(source: File): Trimesh {
        //MeshLoader.testExtension(supportedExtension, source.extension)
        return loadBinary(source.readBytes().let {
            ByteBuffer.wrap(it).apply {
                order(ByteOrder.nativeOrder())
            }
        })
    }

    private fun loadBinary(reader: ByteBuffer): Trimesh {
        val faces = reader.getInt(80)
        var r = 0f
        var g = 0f
        var b = 0f
        var defaultR = 0f
        var defaultG = 0f
        var defaultB = 0f
        //var alpha: Float
        var hasColors = false
        var colors: MutableList<Float>? = null
        for (index in 0 until 80 - 10) {
            if (reader.getInt(index) == 0x434F4C4F /*COLO*/
                && reader[index + 4].toInt() == 0x52
                && reader[index + 5].toInt() == 0x3D
            ) {
                hasColors = true
                colors = ArrayList()
                defaultR = reader[index + 6] / 255.toFloat()
                defaultG = reader[index + 7] / 255.toFloat()
                defaultB = reader[index + 8] / 255.toFloat()
                //alpha = reader[index + 9] / 255.toFloat()
            }
        }
        val dataOffset = 84
        val faceLength = 12 * 4 + 2
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        for (face in 0 until faces) {
            val start = dataOffset + face * faceLength
            val normalX = reader.getFloat(start)
            val normalY = reader.getFloat(start + 4)
            val normalZ = reader.getFloat(start + 8)
            if (hasColors) {
                val packedColor = reader.getChar(start + 48).toInt()
                if (packedColor and 0x8000 == 0) {

                    // facet has its own unique color
                    r = (packedColor and 0x1F) / 31.toFloat()
                    g = (packedColor shr 5 and 0x1F) / 31.toFloat()
                    b = (packedColor shr 10 and 0x1F) / 31.toFloat()
                } else {
                    r = defaultR
                    g = defaultG
                    b = defaultB
                }
            }
            for (i in 1..3) {
                val vertexStart = start + i * 12
                vertices.add(reader.getFloat(vertexStart))
                vertices.add(reader.getFloat(vertexStart + 4))
                vertices.add(reader.getFloat(vertexStart + 8))
                normals.add(normalX)
                normals.add(normalY)
                normals.add(normalZ)
                if (hasColors) {
                    colors!!.add(r)
                    colors.add(g)
                    colors.add(b)
                }
            }
        }
        return Trimesh(
            vertices = vertices,
            normals = normals
        ).also {
            it.generateIndices()
        }
    }

}
