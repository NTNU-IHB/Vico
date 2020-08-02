package no.ntnu.ihb.vico.render.jme.objects

import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Cylinder
import com.jme3.scene.shape.Sphere


/**
 *
 * JMonkey has no native capsule geometry
 *
 * @author Lars Ivar Hatledal
 */
class JmeCapsule(
    radius: Float = 0.5f,
    height: Float = 1f,
    axisSamples: Int = 32,
    radialSamples: Int = 32,
    zSamples: Int = 32,
    radialSamples2: Int = 32
) : Node("Capsule") {

    init {
        val body = Geometry(
            "body",
            Cylinder(axisSamples, radialSamples, radius, height, false)
        )
        val upperSphere =
            Geometry("upperSphere", Sphere(zSamples, radialSamples2, radius))
        val lowerSphere =
            Geometry("lowerSphere", Sphere(zSamples, radialSamples2, radius))
        upperSphere.setLocalTranslation(0f, 0f, height / 2)
        lowerSphere.setLocalTranslation(0f, 0f, -height / 2)
        attachChild(body)
        attachChild(upperSphere)
        attachChild(lowerSphere)
    }

}
