package no.ntnu.ihb.vico.render.mesh

import java.io.File

interface Shape {

    fun toMap(): Map<String, Any>

}

interface SphereShape : Shape {
    val radius: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "sphere",
            "data" to mapOf(
                "radius" to radius
            )
        )
    }

}

interface CircleShape : Shape {
    val radius: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "circle",
            "data" to mapOf(
                "radius" to radius
            )
        )
    }

}


interface PlaneShape : Shape {
    val width: Float
    val height: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "plane",
            "data" to mapOf(
                "width" to width,
                "height" to height
            )
        )
    }

}


interface BoxShape : Shape {
    val width: Float
    val height: Float
    val depth: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "box",
            "data" to mapOf(
                "width" to width,
                "height" to height,
                "depth" to depth
            )
        )
    }

}

interface CylinderShape : Shape {
    val radius: Float
    val height: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "cylinder",
            "data" to mapOf(
                "radius" to radius,
                "height" to height
            )
        )
    }

}


interface CapsuleShape : Shape {
    val radius: Float
    val height: Float

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "capsule",
            "data" to mapOf(
                "radius" to radius,
                "height" to height
            )
        )
    }

}

interface HeightmapShape : Shape {
    val width: Float
    val height: Float
    val widthSegments: Int
    val heightSegments: Int
    val heights: FloatArray

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "capsule",
            "data" to mapOf(
                "width" to width,
                "height" to height,
                "widthSegments" to widthSegments,
                "widthSegments" to widthSegments,
                "heights" to heights
            )
        )
    }

}

interface TrimeshShape : Shape {
    val indices: List<Int>
    val vertices: List<Float>
    val normals: List<Float>
    val uvs: List<Float>

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "type" to "trimesh",
            "data" to mapOf(
                "indices" to indices,
                "vertices" to vertices,
                "normals" to normals,
                "uvs" to uvs,
            )
        )
    }

}

interface TrimeshShapeWithSource : TrimeshShape {
    val source: File?

    override fun toMap(): Map<String, Any> {
        return if (source == null) {
            super.toMap()
        } else {
            val sourcePath = source!!.relativeTo(File(".")).path
            mapOf(
                "type" to "trimeshWithSource",
                "data" to mapOf(
                    "source" to sourcePath
                )
            )
        }
    }

}
