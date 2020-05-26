package no.ntnu.ihb.acco.render.jme.objects

import com.jme3.asset.AssetManager
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.SceneGraphVisitorAdapter
import com.jme3.scene.Spatial
import no.ntnu.ihb.acco.render.Color
import no.ntnu.ihb.acco.render.jme.getLightingMaterial
import no.ntnu.ihb.acco.render.jme.getWireFrameMaterial


class RenderNode(
    private val assetManager: AssetManager,
    private var visible: Boolean = true,
    private var wireframe: Boolean = false,
    private val color: Color = Color()
) : Node() {

    private val mainMaterial = assetManager.getLightingMaterial(color)
    private val wireframeMaterial = assetManager.getWireFrameMaterial()

    init {
        //this.mainMaterial.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
    }


    override fun attachChild(child: Spatial): Int {
        return super.attachChild(child).also {
            child.breadthFirstTraversal(object : SceneGraphVisitorAdapter() {
                override fun visit(geom: Geometry) {
                    if (geom.material == null || wireframe) {
                        geom.material = mainMaterial
                    }
                }
            })
        }
    }

    fun setWireframe(flag: Boolean) {
        if (flag != wireframe) {
            if (flag && !wireframe) {
                setMaterial(wireframeMaterial)
            } else if (!flag && wireframe) {
                setMaterial(mainMaterial)
            }
        }
        this.wireframe = flag
    }

    fun setVisible(flag: Boolean) {
        if (flag != visible) {
            setCullHint(if (flag) CullHint.Never else CullHint.Always)
        }
        this.visible = flag
    }

}
