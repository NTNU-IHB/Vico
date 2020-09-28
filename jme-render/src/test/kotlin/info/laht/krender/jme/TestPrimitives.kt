package info.laht.krender.jme

import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.Vector3f

fun main() {

    JmeRenderEngine().apply {

        createBox(1f, 1f, 1f).apply {
            setTranslate(Vector3f(-2f, 0f, 0f))
            setColor(ColorConstants.bisque)
        }

        createSphere(0.5f).apply {
            setTranslate(Vector3f(0f, 0f, 0f))
            setColor(ColorConstants.firebrick)
            setOpacity(0.5f)
        }

        createCapsule(0.5f, 1f).apply {
            setTranslate(Vector3f(2f, 0f, 0f))
            setColor(ColorConstants.antiquewhite)
        }

        createCylinder(0.5f, 1f).apply {
            setTranslate(Vector3f(4f, 0f, 0f))
            setColor(ColorConstants.rebeccapurple)
        }

    }

}
