package info.laht.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Sphere
import info.laht.acco.math.Vector3
import info.laht.acco.render.Color
import info.laht.acco.render.GeometryComponent
import info.laht.acco.render.jme.objects.JmeGrid
import info.laht.acco.render.jme.objects.RenderNode
import info.laht.acco.render.shape.BoxShape
import info.laht.acco.render.shape.PlaneShape
import info.laht.acco.render.shape.SphereShape


internal fun Vector3f.set(v: Vector3) = apply {
    set(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
}

internal fun Quaternion.set(q: info.laht.acco.math.Quaternion) = apply {
    set(q.x.toFloat(), q.y.toFloat(), q.z.toFloat(), q.w.toFloat())
}

internal fun AssetManager.getLightingMaterial(color: Color? = null): Material {
    return Material(this, "Common/MatDefs/Light/Lighting.j3md").apply {
        additionalRenderState.blendMode = RenderState.BlendMode.Alpha
        if (color != null) {
            ColorRGBA().set(color).also { colorRGBA ->
                setBoolean("UseMaterialColors", true)
                setColor("Ambient", colorRGBA)
                setColor("Diffuse", colorRGBA)
                setColor("Specular", colorRGBA)
                setColor("GlowColor", colorRGBA)
            }
        }
    }
}

internal fun AssetManager.getUnshadedMaterial(): Material {
    return Material(this, "Common/MatDefs/Misc/Unshaded.j3md")
}

internal fun AssetManager.getWireFrameMaterial(color: Color? = null): Material {
    return getUnshadedMaterial().apply {
        additionalRenderState.isWireframe = true
        if (color != null) {
            setColor("Color", ColorRGBA().set(color))
        }
    }
}

internal fun ColorRGBA.set(c: Color, alpha: Float = 1f) = apply {
    set(c.r, c.g, c.b, alpha)
}

internal fun GeometryComponent.createGeometry(assetManager: AssetManager): RenderNode {

    return when (val shape = shape) {
        is BoxShape -> {
            RenderNode(assetManager, visible, wireframe, color).apply {
                attachChild(
                    Geometry(
                        "BoxGeometry",
                        Box(shape.width * 0.5f, shape.height * 0.5f, shape.depth * 0.5f)
                    )
                )
            }
        }
        is PlaneShape -> {
            RenderNode(assetManager, visible, wireframe, color).apply {
                attachChild(
                    Geometry(
                        "PlaneGeometry",
                        JmeGrid(shape.width, shape.height)
                    )
                )
            }
        }
        is SphereShape -> {
            RenderNode(assetManager, visible, wireframe, color).apply {
                attachChild(
                    Geometry(
                        "SphereGeometry",
                        Sphere(32, 32, shape.radius)
                    )
                )
            }
        }
        else -> RenderNode(assetManager)
    }

}
