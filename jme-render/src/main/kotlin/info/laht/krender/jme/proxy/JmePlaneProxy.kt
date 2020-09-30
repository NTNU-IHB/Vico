package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeGrid
import info.laht.krender.proxies.PlaneProxy

internal class JmePlaneProxy(
    ctx: JmeContext,
    width: Float,
    height: Float
) : JmeProxy("plane", ctx), PlaneProxy {

    init {
        attachChild(Geometry("", JmeGrid(1, 1, width, height)))
    }

}
