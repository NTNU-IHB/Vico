package info.laht.krender.threekt

import info.laht.krender.mesh.BoxMesh
import info.laht.krender.mesh.RingMesh
import info.laht.krender.mesh.SphereMesh
import info.laht.threekt.math.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import java.io.File

fun main() {

    ThreektRenderer().apply {

        setCameraTransform(Matrix4f().setTranslation(0f, 5f, 50f))
        createMesh(SphereMesh(5f)).apply {
            setColor(Color.red)
            setWireframe(true)
        }
        createMesh(BoxMesh()).apply {
            setColor(Color.blue)
            setOpacity(0.5f)
            setTranslate(Vector3f().setComponent(0, 5f))
        }

        createMesh(RingMesh(2f, 10f)).apply {
            setColor(Color.purple)
            setTranslate(Vector3f().setComponent(0, -10f))
        }

        val objFile = File(javaClass.classLoader.getResource("obj/female02/female02.obj")!!.file)
        createMesh(objFile, 0.1f)
    }

}
