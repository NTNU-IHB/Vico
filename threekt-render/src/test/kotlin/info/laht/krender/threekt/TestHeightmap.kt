package info.laht.krender.threekt

import info.laht.threekt.math.Color
import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.Matrix4f


fun main() {

    ThreektRenderer().apply {

        setCameraTransform(Matrix4f().setTranslation(0f, 0f, 5f))
        setBackGroundColor(ColorConstants.aliceblue)

        val heights = floatArrayOf(
            0f, 0f, 0f,
            0.1f, 0.5f, 0.1f,
            0f, 0f, 0f
        )
        createHeightmap(1f, 1f, 2, 2, heights).apply {
            setColor(Color.green)
            setWireframe(true)
        }

        /*createMesh(TerrainMesh(1f, 1f, 2, 2, heights)).apply {
            setColor(Color.green)
            setWireframe(true)
            setTranslate(Vector3f(2f, 0f, 0f))
        }

        createMesh(PlaneMesh(1f, 1f, 2, 2)).apply {
            setColor(Color.green)
            setWireframe(true)
            setTranslate(Vector3f(-2f, 0f, 0f))
        }*/

    }


}
