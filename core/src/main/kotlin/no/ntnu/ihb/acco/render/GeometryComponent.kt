package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.render.shape.Shape

class GeometryComponent(
    val shape: Shape
) : Component {
    val color = Color(Color.white)
    var visible = true
    var wireframe = false
}
