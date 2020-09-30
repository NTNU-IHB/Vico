package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeCapsule
import info.laht.krender.proxies.CapsuleProxy

internal class JmeCapsuleProxy(
    ctx: JmeContext,
    private var radius: Float,
    private var height: Float
) : JmeProxy("capsule", ctx), CapsuleProxy {

    init {
        attachChild(
            JmeCapsule(
                32,
                32,
                32,
                32,
                radius,
                height
            )
        )
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
