package info.laht.krender.threekt.objects

import info.laht.threekt.core.Object3DImpl
import info.laht.threekt.geometries.CylinderBufferGeometry
import info.laht.threekt.geometries.SphereBufferGeometry
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.objects.Mesh

internal class Capsule(
    radius: Float,
    height: Float,
    radiusSegments: Int = 16,
    heightSegments: Int = 8,
    material: Material = MeshBasicMaterial()
) : Object3DImpl() {

    init {

        val body = Mesh(CylinderBufferGeometry(radius, radius, height, radiusSegments, heightSegments), material)
        val upper = Mesh(SphereBufferGeometry(radius, radiusSegments, heightSegments), material)
        val lower = Mesh(SphereBufferGeometry(radius, radiusSegments, heightSegments), material)

        upper.position.set(0, height / 2, 0)
        lower.position.set(0, -height / 2, 0)

        add(body, upper, lower)

    }

}
