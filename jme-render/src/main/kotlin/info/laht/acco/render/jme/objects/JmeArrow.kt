package info.laht.aco.render.jme.objects

import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Cylinder


class JmeArrow(
    length: Float
) : Node("Arrow") {

    init {
        val bodyRadius = (length * 0.05f)
        val tipRadius = (bodyRadius * 1.5f)
        val bodyLen = (length * 0.8f)
        val tipLen = (length * 0.2f)
        val body = Geometry("ArrowBody", Cylinder(axisSamples, radialSamples, bodyRadius, bodyLen, true))
        val cone = Geometry("ArrowCone", Cylinder(axisSamples, radialSamples, 0.001f, tipRadius, tipLen, true, false))
        body.move(0f, 0f, bodyLen / 2)
        cone.move(0f, 0f, bodyLen + tipLen / 2)
        attachChild(body)
        attachChild(cone)
    }

    private companion object {
        const val axisSamples = 32
        const val radialSamples = 32
    }

}
