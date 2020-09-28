package info.laht.krender.proxies

import info.laht.krender.util.ExternalSource
import org.joml.Matrix4fc
import org.joml.Quaternionfc
import org.joml.Vector3fc

interface ArrowProxy : RenderProxy, ColorProxy, SpatialProxy {
    fun setLength(length: Float)
}

interface AxisProxy : RenderProxy, SpatialProxy {
    fun setSize(size: Float)
}

interface BoxProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy

interface CapsuleProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
    fun setHeight(height: Float)
}

interface ColorProxy {
    fun setColor(color: Int)
    fun setOpacity(value: Float)
}

interface CurveProxy : RenderProxy, ColorProxy, WireframeProxy {
    fun update(points: List<Vector3fc>)
}

interface CylinderProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
    fun setHeight(height: Float)
}

interface RenderProxy {
    fun setVisible(visible: Boolean)
    fun dispose()
}

interface SpatialProxy {
    fun setTranslate(v: Vector3fc)
    fun setRotate(q: Quaternionfc)
    fun setTransform(m: Matrix4fc)
    fun setOffsetTransform(offset: Matrix4fc)
}

interface SphereProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
}

interface HeightmapProxy : RenderProxy, SpatialProxy, WireframeProxy, ColorProxy, TextureProxy

interface TextureProxy {
    fun setTexture(source: ExternalSource)
}

interface WaterProxy : RenderProxy, WireframeProxy {
    fun setTranslate(v: Vector3fc)
}

interface WireframeProxy {
    fun setWireframe(flag: Boolean)
}

interface LineProxy : RenderProxy, ColorProxy {
    fun update(points: List<Vector3fc>)
}

interface MeshProxy : RenderProxy, WireframeProxy, SpatialProxy, TextureProxy, ColorProxy

interface PlaneProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy

interface PointCloudProxy
