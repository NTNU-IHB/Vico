package info.laht.krender.jme.proxy

import com.jme3.scene.Node
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.PointCloudProxy

internal class JmePointCloudProxy(
    private val ctx: JmeContext
) : Node("pointCloud"), PointCloudProxy
