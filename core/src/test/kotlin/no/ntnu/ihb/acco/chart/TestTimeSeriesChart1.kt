package no.ntnu.ihb.acco.chart

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestFmus
import no.ntnu.ihb.vico.model.ModelResolver
import java.io.File

private object TestTimeSeriesChart1 {

    private fun getTestResource(name: String): File {
        return File(TestTimeSeriesChart1::class.java.classLoader.getResource(name)!!.file)
    }

    @JvmStatic
    fun main(args: Array<String>) {

        Engine.Builder()
            .stopTime(5.0)
            .build().use { engine ->

                Entity("bouncingBall").apply {
                    val slave = SlaveComponent(
                        ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu")), name
                    )
                    addComponent(slave)
                    engine.addEntity(this)
                }

                engine.addSystem(SlaveSystem())

                val config = getTestResource("chartconfig/ChartConfig1.xml")
                ChartLoader.load(config).forEach {
                    engine.addSystem(it)
                }

                engine.stepUntil(10.0)

            }

    }

}
