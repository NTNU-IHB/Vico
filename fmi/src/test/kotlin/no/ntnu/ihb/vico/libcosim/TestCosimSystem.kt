package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.components.PositionRefComponent
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.JmeRenderSystem
import no.ntnu.ihb.acco.render.shape.SphereShape
import no.ntnu.ihb.acco.systems.PositionRefSystem
import no.ntnu.ihb.vico.TestFmus
import java.io.File

object TestCosimSystem {

    @JvmStatic
    fun main(args: Array<String>) {

        val fmuPath = TestFmus.get("1.0/BouncingBall.fmu")
        val resultDir = File("build/results/cosim/BouncingBall").also {
            it.deleteRecursively()
        }

        Engine().also { engine ->

            engine.addSystem(CosimSystem(CosimLogConfig(resultDir)))

            engine.createEntity("bouncingBall").apply {
                addComponent<TransformComponent>()
                addComponent(CosimFmuComponent(fmuPath, name))
                addComponent(PositionRefComponent(yRef = "h"))
                addComponent(GeometryComponent(SphereShape()))
            }

            engine.addSystem(JmeRenderSystem())
            engine.addSystem(PositionRefSystem())

            engine.runner.start()

        }

    }

}
