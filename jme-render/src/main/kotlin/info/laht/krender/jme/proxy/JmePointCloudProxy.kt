package info.laht.krender.jme.proxy

import com.jme3.scene.Node
import info.laht.krender.jme.JmeContext
import no.ntnu.ihb.vico.render.proxies.PointCloudProxy

internal class JmePointCloudProxy(
    private val ctx: JmeContext
) : Node("pointCloud"), PointCloudProxy
