package info.laht.krender.jme

import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.Vector3f

fun main() {

    val points = listOf(
        Vector3f(0f, 0f, 0f),
        Vector3f(1f, 0f, 0f),
        Vector3f(1f, 2f, 0f),
        Vector3f(1f, 2f, 5f),
    )

    JmeRenderEngine().apply {

        val curve = createCurve(0.1f, points)

        createArrow(1f).apply {
            setColor(ColorConstants.yellow)
        }

        Thread.sleep(1000)

        curve.setColor(ColorConstants.blue)
        curve.setWireframe(true)

    }

}
