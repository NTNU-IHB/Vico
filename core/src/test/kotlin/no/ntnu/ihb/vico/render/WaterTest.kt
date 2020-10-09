package no.ntnu.ihb.vico.render

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.core.EngineBuilder
import org.joml.Matrix4f

fun main() {

    val renderer = ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(50f, 50f, 50f))
    }

    val engine = EngineBuilder().renderer(renderer).build()
    engine.use {

        engine.addSystem(WaterRenderer())

        engine.createEntity("water").apply {
            add(Water(100f, 100f))
        }

        engine.runner.startAndWait()

    }

}
