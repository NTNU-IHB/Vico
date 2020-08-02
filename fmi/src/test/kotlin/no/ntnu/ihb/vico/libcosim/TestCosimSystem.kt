package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.vico.TestFmus
import no.ntnu.ihb.vico.chart.TimeSeriesDrawer
import no.ntnu.ihb.vico.components.PositionRefComponent
import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.render.GeometryComponent
import no.ntnu.ihb.vico.render.jme.JmeRenderSystem
import no.ntnu.ihb.vico.shapes.SphereShape
import no.ntnu.ihb.vico.systems.PositionRefSystem
import java.io.File

object TestCosimSystem {

    @JvmStatic
    fun main(args: Array<String>) {

        val fmuPath = TestFmus.get("1.0/BouncingBall.fmu")
        val resultDir = File("build/results/cosim/BouncingBall").also {
            it.deleteRecursively()
        }

        Engine(1e-2).use { engine ->

            engine.addSystem(CosimSystem(CosimLogConfig(resultDir)))

            engine.createEntity("bouncingBall").apply {
                addComponent<TransformComponent>()
                addComponent(CosimFmuComponent(fmuPath, name))
                addComponent(PositionRefComponent(yRef = "h"))
                addComponent(GeometryComponent(SphereShape()))
            }

            TimeSeriesDrawer.Builder("BouncingBall", "Height[m]")
                .registerSeries("bouncingBall", "h")
                .decimationFactor(1)
                .build().also { engine.addSystem(it) }

            engine.addSystem(JmeRenderSystem())
            engine.addSystem(PositionRefSystem())

            engine.runner.startAndWait()

        }

    }

}
