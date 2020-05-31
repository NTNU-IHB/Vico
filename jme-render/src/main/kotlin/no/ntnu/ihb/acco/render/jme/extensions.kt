package no.ntnu.ihb.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Sphere
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.JmeGrid
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.PlaneShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import org.joml.Quaterniondc
import org.joml.Vector3dc


internal fun Vector3f.set(v: Vector3dc) = apply {
    set(v.x().toFloat(), v.y().toFloat(), v.z().toFloat())
}

internal fun Quaternion.set(q: Quaterniondc) = apply {
    set(q.x().toFloat(), q.y().toFloat(), q.z().toFloat(), q.w().toFloat())
}

internal fun Node.setLocalTranslation(v: Vector3dc) {
    setLocalTranslation(v.x().toFloat(), v.y().toFloat(), v.z().toFloat())
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
