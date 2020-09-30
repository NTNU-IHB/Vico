package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeAxis
import info.laht.krender.proxies.AxisProxy


internal class JmeAxisProxy(
    ctx: JmeContext,
    size: Float
) : JmeProxy("axis", ctx), AxisProxy {

    private var initialSize = 0f

    init {
        attachChild(JmeAxis(ctx, size.also { initialSize = it }))
    }

    override fun setSize(size: Float) {
        val scale = size / initialSize
        scale(scale.toFloat())
    }

}
