package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.math.Angle
import no.ntnu.ihb.acco.render.shape.Shape
import org.joml.Matrix4d
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc


class GeometryComponent(
    val shape: Shape,
    val offsetTransform: Matrix4d = Matrix4d()
) : Component() {

    val color = Color(Color.white)
    var visible = true
    var wireframe = false

    fun applyMatrix(m: Matrix4dc) {
        offsetTransform.mul(m)
    }

    fun translate(x: Double, y: Double, z: Double) {
        offsetTransform.translate(x, y, z)
    }

    fun translate(v: Vector3dc) {
        offsetTransform.translate(v)
    }

    fun rotate(q: Quaterniondc) {
        offsetTransform.rotate(q)
    }

    fun rotateX(angle: Angle) {
        offsetTransform.rotateX(angle.inRadians())
    }

    fun rotateY(angle: Angle) {
        offsetTransform.rotateY(angle.inRadians())
    }

    fun rotateZ(angle: Angle) {
        offsetTransform.rotateZ(angle.inRadians())
    }

}
