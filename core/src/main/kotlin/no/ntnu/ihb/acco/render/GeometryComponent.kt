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
) : Component {

    private val color = Color(Color.white)
    var visible by Delegates.observable(true) { _, _, _ ->
        listeners.forEach { it.onVisibilityChanged() }
    }
    var wireframe by Delegates.observable(false) { _, _, _ ->
        listeners.forEach { it.onWireframeChanged() }
    }

    private val listeners = mutableListOf<GeometryComponentListener>()

    fun getColor() = color

    fun setColor(hex: Int) {
        color.set(hex)
        listeners.forEach { it.onColorChanged() }
    }

    fun setColor(color: Color) {
        color.set(color)
        listeners.forEach { it.onColorChanged() }
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

    fun addListener(listener: GeometryComponentListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GeometryComponentListener) {
        listeners.remove(listener)
    }

}
