package info.laht.krender.jme

import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.mesh.ConeMesh
import org.joml.Vector3f

fun main() {

    JmeRenderEngine().apply {

        createMesh(ConeMesh()).apply {
            setColor(ColorConstants.orange)
            setOpacity(0.5f)
        }

        createMesh(ConeMesh()).apply {
            setColor(ColorConstants.red)
            setTranslate(Vector3f(0f, 0f, -1f))
        }

    }

}
