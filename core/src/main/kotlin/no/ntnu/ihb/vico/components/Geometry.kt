package no.ntnu.ihb.vico.components

import info.laht.krender.ColorConstants
import info.laht.krender.mesh.Shape
import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.EventDispatcher
import no.ntnu.ihb.vico.core.EventDispatcherImpl
import kotlin.properties.Delegates

class Geometry(
    val shape: Shape
) : Component(), EventDispatcher by EventDispatcherImpl() {
    val color: Int by Delegates.observable(ColorConstants.gray) { _, _, newValue ->
        dispatchEvent("onColorChanged", newValue)
    }
    val opacity: Float by Delegates.observable(1f) { _, _, newValue ->
        dispatchEvent("onOpacityChanged", newValue)
    }
    var visible by Delegates.observable(true) { _, _, newValue ->
        dispatchEvent("onVisibilityChanged", newValue)
    }
    var wireframe by Delegates.observable(false) { _, _, newValue ->
        dispatchEvent("onWireframeChanged", newValue)
    }
}
