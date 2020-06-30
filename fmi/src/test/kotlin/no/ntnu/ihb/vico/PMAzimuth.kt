package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.chart.TimeSeriesDrawer
import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.structure.ParameterSet
import no.ntnu.ihb.vico.structure.RealParameter
import java.io.File

internal object PMAzimuth {

    @JvmStatic
    fun main(args: Array<String>) {

        val fmu = ModelResolver.resolve(File("E:\\dev\\gunnerus_case\\gunnerus-fmus-bin\\PMAzimuth.fmu"))

        Engine().use { engine ->
            engine.createEntity("PMAzimuth").apply {
                val slave = SlaveComponent(fmu, name).apply {
                    addParameterSet(
                        ParameterSet(
                            "initials",
                            RealParameter("input_t", 0.9),
                            RealParameter("input_torque_loss_factor", 1.0),
                            RealParameter("input_gamma_vo", -2.0),
                            RealParameter("input_gamma_vi", -2.0),
                            RealParameter("input_gamma_ro", -2.0)
                        )
                    )
                }
                addComponent(slave)
            }

            engine.addSystem(SlaveSystem(parameterSet = "initials"))

            TimeSeriesDrawer.Builder("PMAzimuth", "RPM")
                .registerSeries("PMAzimuth", "input_act_revs", "output_revs")
                .build().also { engine.addSystem(it) }

            TimeSeriesDrawer.Builder("PMAzimuth", "RPM")
                .registerSeries("PMAzimuth", "input_act_angle", "output_angle")
                .build().also { engine.addSystem(it) }

            engine.invokeAt(20.0) {

                engine.getEntityByName("PMAzimuth").apply {
                    getRealPropertyOrNull("input_act_revs")?.write(doubleArrayOf(2.0))
                    getRealPropertyOrNull("input_act_angle")?.write(doubleArrayOf(90.0))
                    getRealPropertyOrNull("input_torque_loss_factor")?.read()?.first().also { println(it) }
                }


            }


            engine.stepUntil(100.0)

        }

    }

}
