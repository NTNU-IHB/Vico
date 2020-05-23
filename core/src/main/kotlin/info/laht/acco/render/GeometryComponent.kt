package info.laht.acco.render

import info.laht.acco.core.Component
import info.laht.acco.render.shape.Shape

class GeometryComponent(
    val shape: Shape
) : Component {
    val color = Color(Color.white)
    var visible = true
    var wireframe = false
}
