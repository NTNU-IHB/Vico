@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")

@file:DependsOn("no.ntnu.ihb.vico:core:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:dsl:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:fmi:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:chart:0.1.0")
@file:DependsOn("no.ntnu.ihb.vico:jme-render:0.1.0")

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.chart.TimeSeriesDrawer
import no.ntnu.ihb.vico.components.PositionRefComponent
import no.ntnu.ihb.vico.components.TransformComponent
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.log.LogConfigBuilder
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.math.Vector3d_X
import no.ntnu.ihb.vico.render.GeometryComponent
import no.ntnu.ihb.vico.render.jme.JmeRenderSystem
import no.ntnu.ihb.vico.shapes.BoxShape
import no.ntnu.ihb.vico.shapes.PlaneShape
import no.ntnu.ihb.vico.structure.RealParameter
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.joml.Vector3f

execution {

    baseStepSize = 1.0 / 100

    entities {

        entity("ground") {
            component(SlaveComponent("ground.fmu", "ground"))
            component(TransformComponent())
            component(GeometryComponent(PlaneShape(5f, 2f)).apply {
                offsetTransform.rotate(-Math.PI / 2, Vector3d_X, offsetTransform)
            })
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
            component(TransformComponent())
            component(GeometryComponent(BoxShape(Vector3f(0.75f, 0.25f, 0.75f))).setColor(0x0000ff))

            component {
                PositionRefComponent(yRef = "zChassis")
            }
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
            component(TransformComponent())
            component(GeometryComponent(BoxShape(Vector3f(1f, 0.25f, 1f))).setColor(0x0000ff))

            component {
                PositionRefComponent(yRef = "zWheel")
            }
        }

    }

    systems {

        system {
            SlaveSystem(FixedStepMaster(), parameterSet = "initialValues")
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
        system {
            PositionRefSystem()
        }
        system {
            JmeRenderSystem()
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
