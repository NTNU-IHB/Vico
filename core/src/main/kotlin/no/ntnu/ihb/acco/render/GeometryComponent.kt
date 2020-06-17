package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.math.Angle
import no.ntnu.ihb.acco.render.shape.Shape
import org.joml.Matrix4d
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc
import kotlin.properties.Delegates

interface GeometryComponentListener {

    fun onColorChanged()

    fun onVisibilityChanged()

    fun onWireframeChanged()

}

class GeometryComponent(
    val shape: Shape,
    val offsetTransform: Matrix4d = Matrix4d()
) : Component() {

    private val color = Color(Color.white)
    var visible by Delegates.observable(true) { _, _, newValue ->
        dispatchEvent("onVisibilityChanged", newValue)
    }
    var wireframe by Delegates.observable(false) { _, _, newValue ->
        dispatchEvent("onWireframeChanged", newValue)
    }

    fun getColor() = color

    fun setColor(hex: Int) {
        color.set(hex)
        dispatchEvent("onColorChanged", color)
    }

    fun setColor(color: Color) {
        setColor(color.hexValue)
    }

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
