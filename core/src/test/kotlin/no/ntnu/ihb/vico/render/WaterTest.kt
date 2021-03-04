package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.KtorServer
import no.ntnu.ihb.vico.core.Engine

fun main() {

    Engine().use { engine ->

        engine.createEntity("water").apply {
            add(Water(100f, 100f))
        }
        engine.addSystem(KtorServer(8000))

        engine.runner.startAndWait()

    }

}
