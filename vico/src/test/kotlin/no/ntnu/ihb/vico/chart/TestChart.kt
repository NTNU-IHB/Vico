package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestFmus
import no.ntnu.ihb.vico.model.ModelResolver
import java.io.File

private object TestChart {

    @JvmStatic
    fun main(args: Array<String>) {

        Engine().use { engine ->

            Entity("").apply {
                val slave = SlaveComponent(ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu")).instantiate())
                addComponent(slave)
                engine.addEntity(this)
            }

            val system = SlaveSystem()
            engine.addSystem(system)

            val config = File(TestChart::class.java.classLoader.getResource("chartconfig/ChartConfig1.xml")!!.file)
            ChartLoader.load(config).forEach {
                system.addListener(it)
            }

            engine.step(500)

        }

    }

}
