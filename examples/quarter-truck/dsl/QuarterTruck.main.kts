#!vico eval

@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")

@file:DependsOn("no.ntnu.ihb.vico:core:0.4.0")
@file:DependsOn("no.ntnu.ihb.vico:fmi:0.4.0")
@file:DependsOn("no.ntnu.ihb.vico:chart:0.4.0")
@file:DependsOn("no.ntnu.ihb.vico:threekt-render:0.4.0")

import info.laht.krender.threekt.ThreektRenderer
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.chart.TimeSeriesDrawer
import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.dsl.execution
import no.ntnu.ihb.vico.log.LogConfigBuilder
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.render.Geometry
import no.ntnu.ihb.vico.render.GeometryRenderer
import no.ntnu.ihb.vico.render.mesh.BoxMesh
import no.ntnu.ihb.vico.render.mesh.PlaneMesh
import no.ntnu.ihb.vico.structure.RealParameter
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.joml.Matrix4f
import java.io.File

execution {

    baseStepSize = 1.0 / 100
//    renderer(JmeRenderEngine().apply {
//        setCameraTransform(Matrix4f().setTranslation(0f, 2f, -10f))
//    })

    entities {

        entity("ground") {
            component(SlaveComponent("../fmus/ground.fmu", "ground"))
            component(Transform())
            component(Geometry(PlaneMesh(5f, 2f), Matrix4f().rotateX(-Math.PI.toFloat() / 2f)))
            component {
                PositionRef(yRef = "zGround")
            }
        }

        entity("chassis") {
            component(SlaveComponent("../fmus/chassis.fmu", "chassis").apply {
                addParameterSet(
                    "initialValues",
                    RealParameter("C.mChassis", 400.0),
                    RealParameter("C.kChassis", 15000.0),
                    RealParameter("R.dChassis", 1000.0),
                )
            })
            component(Transform())
            component(Geometry(BoxMesh(0.75f, 0.3f, 0.5f)).apply {
                color = 0x0000ff
            })

            component {
                PositionRef(yRef = "zChassis")
            }
        }

        entity("wheel") {
            component(SlaveComponent("../fmus/wheel.fmu", "wheel").apply {
                addParameterSet(
                    "initialValues",
                    RealParameter("C.mWheel", 40.0),
                    RealParameter("C.kWheel", 150000.0),
                    RealParameter("R.dWheel", 0.0),
                )
            })
            component(Transform())
            component(Geometry(BoxMesh(1f, 0.35f, 0.75f)).apply {
                color = 0x00ff00
            })

            component {
                PositionRef(yRef = "zWheel")
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
            }.build(), File("results"))
        }
        system {
            TimeSeriesDrawer.Builder("zWheel", "Displacement[m]")
                .registerSeries("wheel", "zWheel")
                .isLive(true)
                .build()
        }
        system {
            ThreektRenderer().apply {
                setCameraTransform(Matrix4f().setTranslation(0f, 2f, -10f))
            }
        }
        system {
            PositionRefSystem()
        }

    }

    connections {

        "chassis.slaveComponent.p.e" to "wheel.slaveComponent.p1.e"
        "wheel.slaveComponent.p1.f" to "chassis.slaveComponent.p.f"
        "wheel.slaveComponent.p.e" to "ground.slaveComponent.p.e"
        "ground.slaveComponent.p.f" to "wheel.slaveComponent.p.f"

    }

}.runner.apply {
    targetRealTimeFactor = 0.5
    startAndWait()
}
