package no.ntnu.ihb.vico.chart

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestFmus
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.dsl.charts
import no.ntnu.ihb.vico.model.ModelResolver
import java.io.File

private object TestTimeSeriesChart1 {

    private fun getTestResource(name: String): File {
        return File(TestTimeSeriesChart1::class.java.classLoader.getResource(name)!!.file)
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val charts = charts {

            timeSeries("BouncingBall") {

                series("bouncingBall.h") {

                    yLabel = "Height[m]"

                    data {
                        realRef("bouncingBall.h") * 2
                    }

                }

                series("constant") {
                    data {
                        constant(2)
                    }
                }

            }

        }

        Engine.Builder()
            .stopTime(5.0)
            .build().use { engine ->

                engine.createEntity("bouncingBall").apply {
                    val slave = SlaveComponent(
                        ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu")), name
                    )
                    add(slave)
                }

                engine.addSystem(SlaveSystem())

                val config = getTestResource("chartconfig/ChartConfig1.xml")
                ChartLoader.load(config).forEach {
                    engine.addSystem(it)
                }

                ChartLoader2.load(charts).forEach {
                    engine.addSystem(it)
                }

                engine.stepUntil(10.0)

            }

    }

}
