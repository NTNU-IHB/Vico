package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Cylinder
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.CylinderProxy

internal class JmeCylinderProxy(
    ctx: JmeContext,
    private var radius: Float,
    private var height: Float
) : JmeProxy("cylinder", ctx), CylinderProxy {

    init {
        attachChild(Geometry("", Cylinder(32, 32, radius, height, true)))
    }

    override fun setRadius(radius: Float) {
        val scale = (radius / this.radius)
        scale(scale, scale, 1f)
        this.radius = radius
    }

    override fun setHeight(height: Float) {
        val scale = (height / this.height)
        scale(1f, 1f, scale)
        this.height = height
    }

}
