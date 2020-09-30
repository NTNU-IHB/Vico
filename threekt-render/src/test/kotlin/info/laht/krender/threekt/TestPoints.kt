package info.laht.krender.threekt

import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.Matrix4f
import org.joml.Vector3f

fun main() {

    val points = listOf(
        Vector3f(0f, 0f, 0f),
        Vector3f(1f, 0f, 0f),
        Vector3f(1f, 2f, 0f),
        Vector3f(1f, 2f, 5f),
    )

    ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, -5f))
        setBackGroundColor(ColorConstants.aliceblue)
        createPointCloud(0.1f, points)
    }

}
