package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import java.io.File

object VisualLoaderTest {

    @JvmStatic
    fun main(args: Array<String>) {

        val config = File(VisualLoaderTest.javaClass.classLoader.getResource("VisualConfig.xml")!!.file)

        Engine().use { engine ->

            VisualLoader.load(config).forEach { e ->
                engine.addEntity(e)
            }

            engine.addSystem(JmeRenderSystem())

            engine.runner.apply {

                start()

            }

        }

    }

}
