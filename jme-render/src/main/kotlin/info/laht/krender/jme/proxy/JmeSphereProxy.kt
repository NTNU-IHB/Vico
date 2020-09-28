package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Sphere
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.SphereProxy

internal class JmeSphereProxy(
    ctx: JmeContext,
    private var radius: Float
) : JmeProxy("sphere", ctx), SphereProxy {

    init {
        attachChild(Geometry("", Sphere(32, 32, radius)))
    }

    override fun setRadius(radius: Float) {
        val scale = radius / this.radius
        scale(scale)
        this.radius = radius
    }

}
