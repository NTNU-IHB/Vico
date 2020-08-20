@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")

@file:DependsOn("no.ntnu.ihb.vico:core:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:dsl:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:fmi:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:chart:0.1.0")

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.chart.TimeSeriesDrawer
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.log.LogConfigBuilder
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.structure.RealParameter

execution {

    baseStepSize = 1.0 / 100

    entities {

        entity("ground") {
            component(SlaveComponent("ground.fmu", "ground"))
        }

        entity("chassis") {
            component(SlaveComponent("chassis.fmu", "chassis").apply {
                addParameterSet(
                        "initialValues",
                        RealParameter("C.mChassis", 400.0),
                        RealParameter("C.kChassis", 15000.0),
                        RealParameter("R.dChassis", 1000.0),
                )
            })
        }

        entity("wheel") {
            component(SlaveComponent("wheel.fmu", "wheel").apply {
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
            SlaveSystem(FixedStepMaster(), "initialValues")
        }
        system {
            SlaveLoggerSystem(LogConfigBuilder().apply {
                staticFileNames = true
                add("wheel", "zWheel")
                add("chassis", "zChassis")
            }.build())
        }
        system {
            TimeSeriesDrawer.Builder("zWheel", "Displacement[m]")
                    .registerSeries("wheel", "zWheel")
                    .isLive(true)
                    .build()
        }

    }

    connections {

        connection { "chassis.chassis.p.e" to "wheel.wheel.p1.e" }
        connection { "wheel.wheel.p1.f" to "chassis.chassis.p.f" }
        connection { "wheel.wheel.p.e" to "ground.ground.p.e" }
        connection { "ground.ground.p.f" to "wheel.wheel.p.f" }

    }

}.runner.apply {
    startAndWait()
}
