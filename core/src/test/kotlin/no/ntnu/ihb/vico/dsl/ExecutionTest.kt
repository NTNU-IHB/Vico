package no.ntnu.ihb.vico.dsl

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.components.PositionRef
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.structure.RealParameter
import no.ntnu.ihb.vico.systems.PositionRefSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ExecutionTest {

    @Test
    fun testQuarterTruck() {

        val fmuDir = File("../examples/quarter-truck/fmus")
        Assertions.assertTrue(fmuDir.exists())

        execution {

            baseStepSize = 1.0 / 100

            entities {

                entity("ground") {
                    component(SlaveComponent(File(fmuDir, "ground.fmu"), "ground"))
                    component(Transform())
                    component {
                        PositionRef(yRef = "ground.zGround")
                    }
                }

                entity("chassis") {
                    component(SlaveComponent(File(fmuDir, "chassis.fmu"), "chassis").apply {
                        addParameterSet(
                            "initialValues",
                            RealParameter("C.mChassis", 400.0),
                            RealParameter("C.kChassis", 15000.0),
                            RealParameter("R.dChassis", 1000.0),
                        )
                    })
                    component(Transform())
                    component {
                        PositionRef(yRef = "chassis.zChassis")
                    }
                }

                entity("wheel") {
                    component(SlaveComponent(File(fmuDir, "wheel.fmu"), "wheel").apply {
                        addParameterSet(
                            "initialValues",
                            RealParameter("C.mWheel", 40.0),
                            RealParameter("C.kWheel", 150000.0),
                            RealParameter("R.dWheel", 0.0),
                        )
                    })
                    component(Transform())
                    component {
                        PositionRef(yRef = "wheel.zWheel")
                    }
                }

            }

            systems {

                system {
                    SlaveSystem(FixedStepMaster(), parameterSet = "initialValues")
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

        }.use {

            it.init()
            it.step(1)

        }

    }

}
