package info.laht.krender.jme.proxy

import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.SceneGraphVisitorAdapter
import com.jme3.scene.Spatial
import com.jme3.texture.Texture
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeUtils
import info.laht.krender.jme.extra.JmeUtils.convert
import info.laht.krender.jme.extra.JmeUtils.getLightingMaterial
import info.laht.krender.jme.extra.JmeUtils.getWireFrameMaterial
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.*
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

internal abstract class JmeProxy constructor(
    name: String?,
    protected val ctx: JmeContext
) : Node(name), RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {

    private val v = Vector3f()
    private val q = Quaternionf()
    private var color: Int = ColorConstants.gray
    private var material_: Material
    private var isWireframe = false
    private val node: Node = Node()
    private var opacity = 1f

    init {
        material_ = getLightingMaterial(ctx.assetManager, color, opacity)
        material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
        super.attachChild(node)
    }

    override fun setVisible(visible: Boolean) {
        ctx.invokeLater { setCullHint(if (visible) CullHint.Never else CullHint.Always) }
    }

    override fun dispose() {
        ctx.invokeLater { removeFromParent() }
    }

    override fun setColor(color: Int) {
        val colorRGBA = ColorRGBA().fromIntARGB(color).also {
            it.a = this.opacity
            this.color = color
        }
        ctx.invokeLater {
            if (isWireframe) {
                material_.setColor("Color", colorRGBA)
            } else {
                material_.setBoolean("UseMaterialColors", true)
                material_.setColor("Ambient", colorRGBA)
                material_.setColor("Diffuse", colorRGBA)
                material_.setColor("Specular", colorRGBA)
                material_.setColor("GlowColor", colorRGBA)
            }
        }
    }

    override fun setOpacity(value: Float) {
        this.opacity = value
        setQueueBucket(RenderQueue.Bucket.Transparent)
        setColor(this.color)
    }

    override fun setTexture(source: ExternalSource) {
        ctx.invokeLater {
            if (!isWireframe) {
                try {
                    val loadTexture: Texture = JmeUtils.loadTexture(ctx.assetManager, source)
                    material_.setTexture("DiffuseMap", loadTexture)
                } catch (ex: IOException) {
                    Logger.getLogger(JmeProxy::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
        }
    }

    override fun setTranslate(v: Vector3fc) {
        ctx.invokeLater {
            localTranslation.set(v.x(), v.y(), v.z())
            setTransformRefresh()
        }
    }

    override fun setRotate(q: Quaternionfc) {
        ctx.invokeLater {
            localRotation.set(q.x(), q.y(), q.z(), q.w())
            setTransformRefresh()
        }
    }

    override fun setOffsetTransform(offset: Matrix4fc) {
        ctx.invokeLater {
            node.localTranslation = convert(offset.getTranslation(Vector3f()))
            node.localRotation = convert(offset.getNormalizedRotation(Quaternionf()))
            setTransformRefresh()
        }
    }

    override fun setTransform(m: Matrix4fc) {
        m.getTranslation(v)
        m.getNormalizedRotation(q)
        ctx.invokeLater {
            localTranslation.set(v.x, v.y, v.z)
            localRotation.set(q.x, q.y, q.z, q.w)
            setTransformRefresh()
        }
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            if (flag && !isWireframe) {
                setMaterial(getWireFrameMaterial(ctx.assetManager, ColorConstants.black).also { material_ = it })
            } else if (!flag && isWireframe) {
                setMaterial(getLightingMaterial(ctx.assetManager, color, opacity).also { material_ = it })
            }
            material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
            isWireframe = flag
        }
    }

    override fun attachChild(child: Spatial): Int {
        return node.attachChild(child).also {
            child.breadthFirstTraversal(object : SceneGraphVisitorAdapter() {
                override fun visit(geom: Geometry) {
                    if (geom.material == null || isWireframe) {
                        geom.material = material_
                    }
                }
            })
        }
    }

}
