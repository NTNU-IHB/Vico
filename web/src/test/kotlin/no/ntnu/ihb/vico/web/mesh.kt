package no.ntnu.ihb.vico.web

import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.EngineBuilder
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.loaders.ObjLoader
import java.io.File
import kotlin.system.exitProcess


fun main() {

    EngineBuilder().build().use { engine ->

        val e1 = engine.createEntity("e1")
        e1.add<Transform>().apply {
            setLocalTranslation(-5.0, 0.0, 0.0)
        }
        val mesh = ObjLoader().load(File("examples/gunnerus/obj/Gunnerus.obj"))
        e1.add(Geometry(mesh))

        engine.addSystem(SineMoverSystem())
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

        exitProcess(0)

    }

}
