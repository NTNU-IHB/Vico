package info.laht.krender.jme.extra

import com.jme3.asset.AssetManager
import com.jme3.asset.TextureKey
import com.jme3.asset.plugins.FileLocator
import com.jme3.asset.plugins.UrlLocator
import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.*
import com.jme3.math.Matrix4f
import com.jme3.math.Vector3f
import com.jme3.texture.Texture
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.FileSource
import info.laht.krender.util.JomlUtil
import info.laht.krender.util.URLSource
import org.joml.*

internal object JmeUtils {

    fun convert(v: Vector3f): Vector3d {
        return Vector3d(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
    }

    @JvmOverloads
    fun convert(v: Vector3fc, store: Vector3f = Vector3f()): Vector3f {
        return store.set(v.x(), v.y(), v.z())
    }

    fun convert(q: Quaternion): Quaterniondc {
        return Quaterniond(q.x.toDouble(), q.y.toDouble(), q.z.toDouble(), q.w.toDouble())
    }

    @JvmOverloads
    fun convert(q: Quaternionfc, store: Quaternion = Quaternion()): Quaternion {
        return store.set(q.x(), q.y(), q.z(), q.w())
    }

    @JvmOverloads
    fun convertM(m: Matrix4fc, store: Matrix4f = Matrix4f()): Matrix4f {
        store.set(JomlUtil.rowMajor(m, FloatArray(16)))
        return store
    }

    fun convertM(m: Matrix4f): Matrix4d {
        val el = FloatArray(16).apply {
            m.get(this)
        }
        return Matrix4d().set(el)
    }

    @JvmOverloads
    fun convertT(m: Matrix4fc, store: Transform = Transform()): Transform {
        store.fromTransformMatrix(convertM(m, Matrix4f()))
        return store
    }

    fun getLightingMaterial(assetManager: AssetManager, color: Int, opacity: Float): Material {
        return Material(
            assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        ).apply {
            additionalRenderState.blendMode = RenderState.BlendMode.Alpha

            val colorRGBA = ColorRGBA().fromIntARGB(color)
            colorRGBA.a = opacity
            setBoolean("UseMaterialColors", true)
            setColor("Ambient", colorRGBA)
            setColor("Diffuse", colorRGBA)
            setColor("Specular", colorRGBA)
            setColor("GlowColor", colorRGBA)
        }
    }

    fun getUnshadedMaterial(assetManager: AssetManager): Material {
        return Material(
            assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md"
        )
    }

    fun getUnshadedMaterial(assetManager: AssetManager, color: Int): Material {
        return Material(
            assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md"
        ).apply {
            setColor("Color", ColorRGBA().fromIntARGB(color))
        }
    }

    fun getWireFrameMaterial(assetManager: AssetManager): Material {
        return getWireFrameMaterial(assetManager, null)
    }

    fun getWireFrameMaterial(assetManager: AssetManager, color: Int?): Material {
        val mat = getUnshadedMaterial(assetManager)
        val state = mat.additionalRenderState
        state.isWireframe = true
        if (color != null) {
            mat.setColor("Color", ColorRGBA().fromIntARGB(color))
        }
        return mat
    }

    fun loadTexture(assetManager: AssetManager, source: ExternalSource): Texture {
        when (source) {
            is FileSource -> {
                assetManager.registerLocator(source.location, FileLocator::class.java)
            }
            is URLSource -> {
                assetManager.registerLocator(source.location, UrlLocator::class.java)
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
        return assetManager.loadTexture(TextureKey.reducePath(source.filename))
    }

    /* fun convertKeycode(keycode: Int): VicoKey {
         return when (keycode) {
             KeyInput.KEY_0 -> VicoKey.KEY_0
             KeyInput.KEY_1 -> VicoKey.KEY_1
             KeyInput.KEY_2 -> VicoKey.KEY_2
             KeyInput.KEY_3 -> VicoKey.KEY_3
             KeyInput.KEY_4 -> VicoKey.KEY_4
             KeyInput.KEY_5 -> VicoKey.KEY_5
             KeyInput.KEY_6 -> VicoKey.KEY_6
             KeyInput.KEY_7 -> VicoKey.KEY_7
             KeyInput.KEY_8 -> VicoKey.KEY_8
             KeyInput.KEY_9 -> VicoKey.KEY_9
             KeyInput.KEY_NUMPAD0 -> VicoKey.NUMPAD_0
             KeyInput.KEY_NUMPAD1 -> VicoKey.NUMPAD_1
             KeyInput.KEY_NUMPAD2 -> VicoKey.NUMPAD_2
             KeyInput.KEY_NUMPAD3 -> VicoKey.NUMPAD_3
             KeyInput.KEY_NUMPAD4 -> VicoKey.NUMPAD_4
             KeyInput.KEY_NUMPAD5 -> VicoKey.NUMPAD_5
             KeyInput.KEY_NUMPAD6 -> VicoKey.NUMPAD_6
             KeyInput.KEY_NUMPAD7 -> VicoKey.NUMPAD_7
             KeyInput.KEY_NUMPAD8 -> VicoKey.NUMPAD_8
             KeyInput.KEY_NUMPAD9 -> VicoKey.NUMPAD_9
             KeyInput.KEY_A -> VicoKey.KEY_A
             KeyInput.KEY_B -> VicoKey.KEY_B
             KeyInput.KEY_C -> VicoKey.KEY_C
             KeyInput.KEY_D -> VicoKey.KEY_D
             KeyInput.KEY_E -> VicoKey.KEY_E
             KeyInput.KEY_F -> VicoKey.KEY_F
             KeyInput.KEY_G -> VicoKey.KEY_G
             KeyInput.KEY_H -> VicoKey.KEY_H
             KeyInput.KEY_I -> VicoKey.KEY_I
             KeyInput.KEY_J -> VicoKey.KEY_J
             KeyInput.KEY_K -> VicoKey.KEY_K
             KeyInput.KEY_L -> VicoKey.KEY_L
             KeyInput.KEY_M -> VicoKey.KEY_M
             KeyInput.KEY_N -> VicoKey.KEY_N
             KeyInput.KEY_O -> VicoKey.KEY_O
             KeyInput.KEY_P -> VicoKey.KEY_P
             KeyInput.KEY_Q -> VicoKey.KEY_Q
             KeyInput.KEY_R -> VicoKey.KEY_R
             KeyInput.KEY_S -> VicoKey.KEY_S
             KeyInput.KEY_T -> VicoKey.KEY_T
             KeyInput.KEY_U -> VicoKey.KEY_U
             KeyInput.KEY_V -> VicoKey.KEY_V
             KeyInput.KEY_W -> VicoKey.KEY_W
             KeyInput.KEY_X -> VicoKey.KEY_X
             KeyInput.KEY_Y -> VicoKey.KEY_Y
             KeyInput.KEY_Z -> VicoKey.KEY_Z
             KeyInput.KEY_COMMA -> VicoKey.KEY_COMMA
             KeyInput.KEY_PERIOD -> VicoKey.KEY_DOT
             KeyInput.KEY_MINUS -> VicoKey.KEY_MINUS
             KeyInput.KEY_ADD -> VicoKey.KEY_PLUS
             KeyInput.KEY_SPACE -> VicoKey.KEY_SPACE
             KeyInput.KEY_RETURN -> VicoKey.KEY_ENTER
             KeyInput.KEY_BACK -> VicoKey.KEY_BACKSPACE
             KeyInput.KEY_LSHIFT -> VicoKey.KEY_SHIFT_LEFT
             KeyInput.KEY_RSHIFT -> VicoKey.KEY_SHIFT_RIGHT
             KeyInput.KEY_LCONTROL -> VicoKey.KEY_CTRL_LEFT
             KeyInput.KEY_RCONTROL -> VicoKey.KEY_CTRL_RIGHT
             KeyInput.KEY_LEFT -> VicoKey.ARROW_LEFT
             KeyInput.KEY_RIGHT -> VicoKey.ARROW_RIGHT
             KeyInput.KEY_UP -> VicoKey.ARROW_UP
             KeyInput.KEY_DOWN -> VicoKey.ARROW_DOWN
             else -> VicoKey.UNDEFINED
         }
     }*/
}
