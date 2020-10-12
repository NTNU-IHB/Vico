package info.laht.krender.jme.proxy

import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Node
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeTubeGeometry
import info.laht.krender.jme.extra.JmeUtils
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.proxies.CurveProxy
import org.joml.Vector3fc

internal class JmeCurveProxy(
    private val ctx: JmeContext,
    radius: Float,
    points: List<Vector3fc>
) : Node(), CurveProxy {

    private var isWireframe = false
    private var opacity: Float = 1f
    private var color: Int = ColorConstants.gray
    private val tube: JmeTubeGeometry
    private var material_: Material

    init {
        tube = JmeTubeGeometry(radius, points, 80, 20, false)
        attachChild(tube)
        setMaterial(JmeUtils.getLightingMaterial(ctx.assetManager, color, opacity).also { material_ = it })
        material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
    }

    override fun update(points: List<Vector3fc>) {
        ctx.invokeLater { tube.update(points) }
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
        setColor(color)
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            if (flag && !isWireframe) {
                setMaterial(JmeUtils.getWireFrameMaterial(ctx.assetManager, color).also { material_ = it })
            } else if (!flag && isWireframe) {
                setMaterial(JmeUtils.getLightingMaterial(ctx.assetManager, color, opacity).also { material_ = it })
            }
            material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
            isWireframe = flag
        }
    }

}
