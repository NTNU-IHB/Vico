package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.EventDispatcher
import no.ntnu.ihb.vico.core.EventDispatcherImpl
import no.ntnu.ihb.vico.render.mesh.Shape
import org.joml.Matrix4fc
import kotlin.properties.Delegates

class Geometry(
    val shape: Shape,
    val offset: Matrix4fc? = null
) : Component(), EventDispatcher by EventDispatcherImpl() {
    var color: Int by Delegates.observable(ColorConstants.gray) { _, _, newValue ->
        dispatchEvent("onColorChanged", newValue)
    }
    var opacity: Float by Delegates.observable(1f) { _, _, newValue ->
        dispatchEvent("onOpacityChanged", newValue)
    }
    var visible by Delegates.observable(true) { _, _, newValue ->
        dispatchEvent("onVisibilityChanged", newValue)
    }
    var wireframe by Delegates.observable(false) { _, _, newValue ->
        dispatchEvent("onWireframeChanged", newValue)
    }
}
