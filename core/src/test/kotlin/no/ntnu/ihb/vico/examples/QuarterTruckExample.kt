package no.ntnu.ihb.vico.examples

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.chart.TimeSeriesDrawer
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.structure.RealParameter
import java.io.File

fun main() {

    val examplesFolder = File("examples/quarter-truck/fmus").apply {
        if (!exists()) throw NoSuchFileException(this)
    }.absolutePath

    execution {

        baseStepSize = 1.0 / 100

        entities {

            entity("ground") {
                component(SlaveComponent("${examplesFolder}/ground.fmu", "ground"))
            }

            entity("chassis") {
                component(SlaveComponent("${examplesFolder}/chassis.fmu", "chassis").apply {
                    addParameterSet(
                        "initialValues",
                        RealParameter("C.mChassis", 400.0),
                        RealParameter("C.kChassis", 15000.0),
                        RealParameter("R.dChassis", 1000.0),
                    )
                })
            }

            entity("wheel") {
                component(SlaveComponent("${examplesFolder}/wheel.fmu", "wheel").apply {
                    addParameterSet(
                        "initialValues",
                        RealParameter("C.mWheel", 40.0),
                        RealParameter("C.kWheel", 150000.0),
                        RealParameter("R.dWheel", 0.0),
                    )
                })
            }

        }

        systems {

            system {
                SlaveSystem(parameterSet = "initialValues")
            }
            system {
                TimeSeriesDrawer.Builder("zWheel", "Displacement[m]")
                    .registerSeries("wheel", "zWheel")
                    .isLive(true)
                    .build()
            }

        }

        connections {

            "chassis.chassis.p.e" to "wheel.wheel.p1.e"
            "wheel.wheel.p1.f" to "chassis.chassis.p.f"
            "wheel.wheel.p.e" to "ground.ground.p.e"
            "ground.ground.p.f" to "wheel.wheel.p.f"

        }

    }.runner.apply {
        targetRealTimeFactor = 0.5
        startAndWait()
    }

}

