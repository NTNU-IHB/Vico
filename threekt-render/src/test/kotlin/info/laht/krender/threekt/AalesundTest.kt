package info.laht.krender.threekt

import org.joml.Matrix4f
import java.io.File

fun main() {

    ThreektRenderer().apply {

        setCameraTransform(Matrix4f().setTranslation(0f, 50f, 50f))

        val objFile = File(javaClass.classLoader.getResource("obj/aalesund/aalesund.obj")!!.file)
        createMesh(objFile, 0.1f)
    }

}
