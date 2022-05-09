package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Engine

fun main() {

    Engine().use { engine ->

        engine.createEntity("camera").apply {
            add<Camera>()
            add<Transform>().apply {
                setLocalTranslation(50.0, 25.0, 50.0)
            }
        }

        engine.createEntity("water").apply {
            add(Water(100f, 100f))
        }
        engine.addSystem(KtorServer(8000, true))

        engine.runner.startAndWait()

    }

}
