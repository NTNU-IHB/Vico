package info.laht.krender.jme

import info.laht.krender.loaders.ObjLoader
import no.ntnu.ihb.vico.render.ColorConstants
import org.joml.Matrix4f
import org.joml.Vector3f
import java.io.File

fun main() {

    JmeRenderEngine().apply {

        setBackGroundColor(0xF0F8FF)
        setCameraTransform(Matrix4f().setTranslation(0f, 0f, -5f))

        val sphere = createSphere(0.1f).apply {
            setColor(ColorConstants.black)
        }

        val source = File(javaClass.classLoader.getResource("obj/bunny.obj")!!.file)
        val load = ObjLoader().load(source).apply {
            computeVertexNormals()
            scale(5f)
        }
        val bunny = createMesh(load).apply {
            setOffsetTransform(Matrix4f().setTranslation(2f, 0f, 0f))
        }
        createMesh(source, 2f)

        Thread.sleep(1000)

        sphere.setColor(ColorConstants.blue)
        bunny.setWireframe(true)

        var stop = false
        Thread {

            val transform = Matrix4f()

            while (!stop) {

                transform.rotate(0.1f * 0.1f, Vector3f(0f, 1f, 0f))
                bunny.setTransform(transform)
                Thread.sleep(10)
            }

        }.start()

        registerCloseListener {
            stop = true
        }

    }

}


