package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.BoxProxy

internal class JmeBoxProxy(
    ctx: JmeContext,
    width: Float,
    height: Float,
    depth: Float
) : JmeProxy("box", ctx), BoxProxy {

    init {
        attachChild(Geometry("", Box((width * 0.5f), (height * 0.5f), (depth * 0.5f))))
    }

}
