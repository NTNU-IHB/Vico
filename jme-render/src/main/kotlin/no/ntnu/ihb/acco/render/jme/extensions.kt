package no.ntnu.ihb.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.input.KeyInput
import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Cylinder
import com.jme3.scene.shape.Sphere
import no.ntnu.ihb.acco.input.KeyStroke
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.JmeCapsule
import no.ntnu.ihb.acco.render.jme.objects.JmeGrid
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import no.ntnu.ihb.acco.shapes.*
import org.joml.Quaterniondc
import org.joml.Vector3d
import org.joml.Vector3dc

internal fun Vector3d.set(v: Vector3f) = apply {
    return set(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
}

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

private fun createBox(shape: BoxShape): Geometry {
    return Geometry(
        "BoxGeometry",
        Box(shape.width.toFloat() * 0.5f, shape.height.toFloat() * 0.5f, shape.depth.toFloat() * 0.5f)
    )
}

private fun createPlane(shape: PlaneShape): Geometry {
    return Geometry(
        "PlaneGeometry",
        JmeGrid(shape.width.toFloat(), shape.height.toFloat())
    )
}

private fun createSphere(shape: SphereShape): Geometry {
    return Geometry(
        "SphereGeometry",
        Sphere(32, 32, shape.radius.toFloat())
    )
}

private fun createCylinder(shape: CylinderShape): Geometry {
    return Geometry(
        "CylinderGeometry",
        Cylinder(32, 32, shape.radius.toFloat(), shape.height.toFloat(), true)
    )
}

private fun createCapsule(shape: CapsuleShape): Node {
    return JmeCapsule(shape.radius.toFloat(), shape.height.toFloat())
}

internal fun GeometryComponent.createGeometry(assetManager: AssetManager): RenderNode {

    return when (val shape = shape) {
        is BoxShape -> {
            RenderNode(assetManager, visible, wireframe, getColor()).apply {
                attachChild(createBox(shape))
            }
        }
        is PlaneShape -> {
            RenderNode(assetManager, visible, wireframe, getColor()).apply {
                attachChild(createPlane(shape))
            }
        }
        is SphereShape -> {
            RenderNode(assetManager, visible, wireframe, getColor()).apply {
                attachChild(createSphere(shape))
            }
        }
        is CylinderShape -> {
            RenderNode(assetManager, visible, wireframe, getColor()).apply {
                attachChild(createCylinder(shape))
            }
        }
        is CapsuleShape -> {
            RenderNode(assetManager, visible, wireframe, getColor()).apply {
                attachChild(createCapsule(shape))
            }
        }
        else -> RenderNode(assetManager)
    }

}

internal fun Int.toKeyStoke() =
    when (this) {
        KeyInput.KEY_0 -> KeyStroke.KEY_0
        KeyInput.KEY_1 -> KeyStroke.KEY_1
        KeyInput.KEY_2 -> KeyStroke.KEY_2
        KeyInput.KEY_3 -> KeyStroke.KEY_3
        KeyInput.KEY_4 -> KeyStroke.KEY_4
        KeyInput.KEY_5 -> KeyStroke.KEY_5
        KeyInput.KEY_6 -> KeyStroke.KEY_6
        KeyInput.KEY_7 -> KeyStroke.KEY_7
        KeyInput.KEY_8 -> KeyStroke.KEY_8
        KeyInput.KEY_9 -> KeyStroke.KEY_9
        KeyInput.KEY_F1 -> KeyStroke.KEY_F1
        KeyInput.KEY_F2 -> KeyStroke.KEY_F2
        KeyInput.KEY_F3 -> KeyStroke.KEY_F3
        KeyInput.KEY_F4 -> KeyStroke.KEY_F4
        KeyInput.KEY_F5 -> KeyStroke.KEY_F5
        KeyInput.KEY_F6 -> KeyStroke.KEY_F6
        KeyInput.KEY_F7 -> KeyStroke.KEY_F7
        KeyInput.KEY_F8 -> KeyStroke.KEY_F8
        KeyInput.KEY_F9 -> KeyStroke.KEY_F9
        KeyInput.KEY_F10 -> KeyStroke.KEY_F10
        KeyInput.KEY_F11 -> KeyStroke.KEY_F11
        KeyInput.KEY_F12 -> KeyStroke.KEY_F12
        KeyInput.KEY_NUMPAD0 -> KeyStroke.NUMPAD_0
        KeyInput.KEY_NUMPAD1 -> KeyStroke.NUMPAD_1
        KeyInput.KEY_NUMPAD2 -> KeyStroke.NUMPAD_2
        KeyInput.KEY_NUMPAD3 -> KeyStroke.NUMPAD_3
        KeyInput.KEY_NUMPAD4 -> KeyStroke.NUMPAD_4
        KeyInput.KEY_NUMPAD5 -> KeyStroke.NUMPAD_5
        KeyInput.KEY_NUMPAD6 -> KeyStroke.NUMPAD_6
        KeyInput.KEY_NUMPAD7 -> KeyStroke.NUMPAD_7
        KeyInput.KEY_NUMPAD8 -> KeyStroke.NUMPAD_8
        KeyInput.KEY_NUMPAD9 -> KeyStroke.NUMPAD_9
        KeyInput.KEY_A -> KeyStroke.KEY_A
        KeyInput.KEY_B -> KeyStroke.KEY_B
        KeyInput.KEY_C -> KeyStroke.KEY_C
        KeyInput.KEY_D -> KeyStroke.KEY_D
        KeyInput.KEY_E -> KeyStroke.KEY_E
        KeyInput.KEY_F -> KeyStroke.KEY_F
        KeyInput.KEY_G -> KeyStroke.KEY_G
        KeyInput.KEY_H -> KeyStroke.KEY_H
        KeyInput.KEY_I -> KeyStroke.KEY_I
        KeyInput.KEY_J -> KeyStroke.KEY_J
        KeyInput.KEY_K -> KeyStroke.KEY_K
        KeyInput.KEY_L -> KeyStroke.KEY_L
        KeyInput.KEY_M -> KeyStroke.KEY_M
        KeyInput.KEY_N -> KeyStroke.KEY_N
        KeyInput.KEY_O -> KeyStroke.KEY_O
        KeyInput.KEY_P -> KeyStroke.KEY_P
        KeyInput.KEY_Q -> KeyStroke.KEY_Q
        KeyInput.KEY_R -> KeyStroke.KEY_R
        KeyInput.KEY_S -> KeyStroke.KEY_S
        KeyInput.KEY_T -> KeyStroke.KEY_T
        KeyInput.KEY_U -> KeyStroke.KEY_U
        KeyInput.KEY_V -> KeyStroke.KEY_V
        KeyInput.KEY_W -> KeyStroke.KEY_W
        KeyInput.KEY_X -> KeyStroke.KEY_X
        KeyInput.KEY_Y -> KeyStroke.KEY_Y
        KeyInput.KEY_Z -> KeyStroke.KEY_Z
        KeyInput.KEY_COMMA -> KeyStroke.KEY_COMMA
        KeyInput.KEY_PERIOD -> KeyStroke.KEY_DOT
        KeyInput.KEY_MINUS -> KeyStroke.KEY_MINUS
        KeyInput.KEY_ADD -> KeyStroke.KEY_PLUS
        KeyInput.KEY_SPACE -> KeyStroke.KEY_SPACE
        KeyInput.KEY_RETURN -> KeyStroke.KEY_ENTER
        KeyInput.KEY_BACK -> KeyStroke.KEY_BACKSPACE
        KeyInput.KEY_LSHIFT -> KeyStroke.KEY_SHIFT_LEFT
        KeyInput.KEY_RSHIFT -> KeyStroke.KEY_SHIFT_RIGHT
        KeyInput.KEY_LCONTROL -> KeyStroke.KEY_CTRL_LEFT
        KeyInput.KEY_RCONTROL -> KeyStroke.KEY_CTRL_RIGHT
        KeyInput.KEY_LEFT -> KeyStroke.ARROW_LEFT
        KeyInput.KEY_RIGHT -> KeyStroke.ARROW_RIGHT
        KeyInput.KEY_UP -> KeyStroke.ARROW_UP
        KeyInput.KEY_DOWN -> KeyStroke.ARROW_DOWN
        else -> KeyStroke.UNDEFINED
    }
