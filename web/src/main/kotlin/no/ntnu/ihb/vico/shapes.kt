package no.ntnu.ihb.vico

import no.ntnu.ihb.vico.render.mesh.*
import java.io.File

internal fun PlaneShape.toJsonShape(): JsonShape {
    return JsonShape(
        "plane",
        mapOf(
            "width" to width,
            "height" to height
        )
    )
}

internal fun BoxShape.toJsonShape(): JsonShape {
    return JsonShape(
        "box",
        mapOf(
            "width" to width,
            "height" to height,
            "depth" to depth
        )
    )
}

internal fun SphereShape.toJsonShape(): JsonShape {
    return JsonShape(
        "sphere",
        mapOf(
            "radius" to radius
        )
    )
}

internal fun CylinderShape.toJsonShape(): JsonShape {
    return JsonShape(
        "cylinder",
        mapOf(
            "radius" to radius,
            "height" to height
        )
    )
}

internal fun CapsuleShape.toJsonShape(): JsonShape {
    return JsonShape(
        "capsule",
        mapOf(
            "radius" to radius,
            "height" to height
        )
    )
}

internal fun TrimeshShape.toJsonShape(): JsonShape {
    return if (this is TrimeshShapeWithSource && this.source != null) {
        val sourcePath = source!!.relativeTo(File(".")).path
        JsonShape(
            "trimeshWithSource",
            mapOf(
                "source" to sourcePath
            )
        )
    } else {
        JsonShape(
            "trimesh",
            mapOf(
                "indices" to indices,
                "vertices" to vertices,
                "normals" to normals,
                "uvs" to uvs
            )
        )
    }
}
