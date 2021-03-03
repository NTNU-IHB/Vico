package no.ntnu.ihb.vico

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.extra.SineMoverComponent
import no.ntnu.ihb.vico.extra.SineMoverSystem
import no.ntnu.ihb.vico.render.ColorConstants
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.CylinderMesh
import no.ntnu.ihb.vico.render.mesh.SphereMesh
import org.joml.Matrix4f
import kotlin.system.exitProcess

fun main() {

    EngineBuilder().build().use { engine ->

        val e1 = engine.createEntity("e1")
        e1.add<Transform>()
        e1.add(Geometry(
            BoxMesh(1f, 2f, 1f),
            Matrix4f().setTranslation(5f, 0f, 0f)
        ).apply {
            color = ColorConstants.blue
            wireframe = true
        })
        e1.add<SineMoverComponent>()

        val e2 = engine.createEntity("e2")
        e2.add<Transform>()
        e2.add(Geometry(SphereMesh(0.5f)).apply {
            color = ColorConstants.yellow
            opacity = 0.8f
        })
        e2.add<SineMoverComponent>().apply {
            axis.set(0.0, 0.0, 1.0).normalize()
        }

        val e3 = engine.createEntity("e3")
        e3.add<Transform>().apply {
            setLocalTranslation(-5.0, 0.0, 0.0)
        }
        e3.add(Geometry(CylinderMesh(0.5f, 1f)).apply {
            color = ColorConstants.green
            opacity = 0.5f
        })
        e3.add<SineMoverComponent>().apply {
            axis.set(1.0, 1.0, 1.0).normalize()
        }

        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

        exitProcess(0)

    }

}
