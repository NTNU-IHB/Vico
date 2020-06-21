package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.EventDispatcher
import no.ntnu.ihb.acco.core.EventDispatcherImpl
import no.ntnu.ihb.acco.render.shape.Shape
import org.joml.Matrix4d
import kotlin.properties.Delegates


class GeometryComponent(
    val shape: Shape,
    val offsetTransform: Matrix4d = Matrix4d()
) : Component(), EventDispatcher by EventDispatcherImpl() {

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

}
