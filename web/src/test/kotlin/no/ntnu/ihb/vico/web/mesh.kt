package no.ntnu.ihb.vico.web

import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.render.Camera
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.Water
import no.ntnu.ihb.vico.render.loaders.ObjLoader
import java.io.File
import kotlin.system.exitProcess


fun main() {

    Engine().use { engine ->

        engine.createEntity("e1").apply {
            val mesh = ObjLoader().load(File("examples/gunnerus/obj/Gunnerus.obj"))
            add(Geometry(mesh))
        }

        engine.createEntity("water", Water(500)).apply {
            add<Transform>().localTranslateY(-1.0)
        }
        engine.createEntity("camera", Camera()).apply {
            add<Transform>().setLocalTranslation(-50.0, 50.0, -75.0)
        }

        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

        exitProcess(0)

    }

}
