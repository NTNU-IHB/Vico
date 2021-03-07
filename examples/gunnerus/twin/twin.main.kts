@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.sspgen:dsl:0.5.0")
@file:DependsOn("no.ntnu.ihb.fmi4j:fmi-import:0.36.7")

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu
import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.fmi4j.writeString
import no.ntnu.ihb.sspgen.dsl.ssp
import java.net.URL
import kotlin.math.PI

class GunnerusState {

    val initial_north: Double
    val initial_east: Double
    val initial_yaw: Double
    val initial_pitch: Double
    val initial_roll: Double
    val initial_surge: Double
    val initial_sway: Double

    init {

        Fmu.from(URL("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/Gunnerus.fmu")).use { fmu ->
            fmu.asCoSimulationFmu().newInstance().use { slave ->

                slave.writeString("csvPath", "gunnerus_experiments_21_nov.csv")
                slave.simpleSetup(start)

                initial_north = slave.readReal("position.north").value
                initial_east = slave.readReal("position.east").value

                initial_yaw = slave.readReal("rotation.yaw").value
                initial_pitch = slave.readReal("rotation.pitch").value
                initial_roll = slave.readReal("rotation.roll").value

                initial_surge = slave.readReal("velocity.surge").value
                initial_sway = slave.readReal("velocity.sway").value

            }
        }

    }

    companion object {
        const val start = 9300.0
    }

}

val state = GunnerusState()

ssp("gunnerus-twin") {

    println("Building SSP '$archiveName'..")

    resources {
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/VesselFmu2.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/PMAzimuth-proxy.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/PowerPlant.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/ThrusterDrive2.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/Gunnerus.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/BasicHeadingPID.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/VelocityPID.fmu")
        url("https://github.com/gunnerus-case/gunnerus-fmus-bin/raw/master/VesselFmuObserver.fmu")
    }

    ssd("KPN Twinship Gunnerus case") {

        system("gunnerus-twin") {

            elements {

                component("vesselModel", "resources/VesselFmu2.fmu") {
                    connectors {
                        real("additionalBodyForce[0].force.heave", input)
                        real("additionalBodyForce[0].force.surge", input)
                        real("additionalBodyForce[0].force.sway", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.xpos", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.ypos", input)
                        real("additionalBodyForce[0].pointOfAttackRel2APAndBL.zpos", input)

                        real("additionalBodyForce[1].force.heave", input)
                        real("additionalBodyForce[1].force.surge", input)
                        real("additionalBodyForce[1].force.sway", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.xpos", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.ypos", input)
                        real("additionalBodyForce[1].pointOfAttackRel2APAndBL.zpos", input)

                        real("cg_x_rel_ap", output)
                        real("cg_y_rel_cl", output)
                        real("cg_z_rel_bl", output)
                        real("cgShipMotion.nedDisplacement.north", output)
                        real("cgShipMotion.nedDisplacement.east", output)
                        real("cgShipMotion.linearVelocity.surge", output)
                        real("cgShipMotion.linearVelocity.sway", output)
                        real("cgShipMotion.angularVelocity.yaw", output)
                        real("cgShipMotion.angularDisplacement.yaw", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            string("vesselZipFile", "%fmu%/resources/ShipModel-gunnerus-elongated.zip")
                            boolean("additionalBodyForce[0].enabled", true)
                            boolean("additionalBodyForce[1].enabled", true)
                            boolean("initialize_using_hydrostatics", true)
                            real("initial_north_position", state.initial_north)
                            real("initial_east_position", state.initial_east)
                            real("initial_down_position", 0.0)
                            real("initial_heading", state.initial_yaw)
                            real("initial_pitch", 0.0)
                            real("initial_roll", 0.0)
                            real("initial_surge_velocity", state.initial_surge)
                            real("initial_sway_velocity", state.initial_sway)
                            real("initial_heave_velocity", 0.0)
                            real("initial_roll_velocity", 0.0)
                            real("initial_pitch_velocity", 0.0)
                        }
                    }
                }

                component("azimuth0", "resources/PMAzimuth-proxy.fmu") {
                    connectors {
                        real("input_act_revs", input)
                        real("input_act_angle", input)
                        real("input_cg_x_rel_ap", input)
                        real("input_cg_y_rel_cl", input)
                        real("input_cg_z_rel_bl", input)
                        real("input_cg_surge_vel", input)
                        real("input_cg_sway_vel", input)
                        real("input_yaw_vel", input)

                        real("output_torque", output)
                        real("output_force_heave", output)
                        real("output_force_surge", output)
                        real("output_force_sway", output)
                        real("output_x_rel_ap", output)
                        real("output_y_rel_cl", output)
                        real("output_z_rel_bl", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("input_x_rel_ap", 1.5)
                            real("input_y_rel_cl", -2.7)
                            real("input_z_rel_bl", 0)
                            real("input_prop_diam", 1.9)
                            real("input_distancetohull", 1.5)
                            real("input_bilgeradius", 3)
                            real("input_rho", 1025)
                            real("input_lpp", 33.9)
                        }
                    }
                }

                component("azimuth1", "resources/PMAzimuth-proxy.fmu") {
                    connectors {
                        copyFrom("azimuth0")
                    }
                    parameterBindings {
                        copyFrom("azimuth0", "initialValues") {
                            real("input_y_rel_cl", 2.7)
                        }
                    }
                }

                component("azimuth0_rpmActuator", "resources/ThrusterDrive2.fmu") {
                    connectors {
                        real("d_in.e", input)
                        real("q_in.e", input)
                        real("ThrustCom", input)
                        real("Shaft.e", input)

                        real("d_in.f", output)
                        real("q_in.f", output)
                        real("Shaft.f", output)
                    }
                }

                component("azimuth1_rpmActuator", "resources/ThrusterDrive2.fmu") {
                    connectors {
                        copyFrom("azimuth0_rpmActuator")
                    }
                }

                component("powerPlant", "resources/PowerPlant.fmu") {
                    connectors {
                        real("p1.f[1]", input)
                        real("p1.f[2]", input)
                        real("p2.f[1]", input)
                        real("p2.f[2]", input)

                        real("p1.e[1]", output)
                        real("p1.e[2]", output)
                        real("p2.e[1]", output)
                        real("p2.e[2]", output)
                    }
                }

                component("gunnerus", "resources/Gunnerus.fmu") {
                    connectors {
                        real("position.north", output)
                        real("position.east", output)
                        real("velocity.surge", output)
                        real("velocity.sway", output)
                        real("rotation.yaw", output)

                        real("portAzimuth.rpm", output)
                        real("portAzimuth.angle", output)
                        real("starboardAzimuth.rpm", output)
                        real("starboardAzimuth.angle", output)

                        real("wind.speed", output)
                        real("wind.direction", output)

                        real("current", output)
                        real("trueCourse", output)
                        real("speedOverGround", output)

                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            boolean("interpolate", true)
                            real("startOffset", GunnerusState.start - 50.0)
                            string("csvPath", "gunnerus_experiments_21_nov.csv")
                        }
                    }
                }

                component("headingController", "resources/BasicHeadingPID.fmu") {
                    connectors {
                        real("setPoint", input)
                        real("processOutput", input)
                        real("output", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("kp", 0.4)
                            real("ti", 0.02)
                            real("td", 0.001)
                            real("minOutput", -60)
                            real("maxOutput", 60)
                            real("setPointSmoothing", 1)
                            boolean("filterSetPoint", false)
                            real("windupGuard", 1.0)
                            boolean("windupGuardEnabled", true)
                            boolean("enabled", false)
                        }
                    }
                }

                component("speedController", "resources/VelocityPID.fmu") {
                    connectors {
                        real("setPoint", input)
                        real("processOutput", input)
                        real("output", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("kp", 7500)
                            real("ti", 1875)
                            real("td", 7.5)
                            boolean("enabled", false)
                        }
                    }
                }

                component("vesselModelObserver", "resources/VesselFmuObserver.fmu") {
                    connectors {
                        real("position.north", input)
                        real("position.east", input)
                        real("trueHeading", output)
                    }
                }

            }

            connections(inputsFirst = true) {

                "powerPlant.p1.f[1]" to "azimuth0_rpmActuator.d_in.f"
                "powerPlant.p1.f[2]" to "azimuth0_rpmActuator.q_in.f"
                "powerPlant.p2.f[1]" to "azimuth1_rpmActuator.d_in.f"
                "powerPlant.p2.f[2]" to "azimuth1_rpmActuator.q_in.f"

                "azimuth0_rpmActuator.d_in.e" to "powerPlant.p1.e[1]"
                "azimuth0_rpmActuator.q_in.e" to "powerPlant.p1.e[2]"
                "azimuth0_rpmActuator.ThrustCom" to "speedController.output"
                "azimuth0_rpmActuator.Shaft.e" to "azimuth0.output_torque"

                "azimuth1_rpmActuator.d_in.e" to "powerPlant.p2.e[1]"
                "azimuth1_rpmActuator.q_in.e" to "powerPlant.p2.e[2]"
                "azimuth1_rpmActuator.ThrustCom" to "speedController.output"
                "azimuth1_rpmActuator.Shaft.e" to "azimuth1.output_torque"

                ("azimuth0.input_act_revs" to "azimuth0_rpmActuator.Shaft.f").linearTransformation(factor = 60.0 / (2 * PI))
                "azimuth0.input_act_angle" to "headingController.output"
                "azimuth0.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "azimuth0.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "azimuth0.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "azimuth0.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "azimuth0.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "azimuth0.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                ("azimuth1.input_act_revs" to "azimuth1_rpmActuator.Shaft.f").linearTransformation(factor = 60.0 / (2 * PI))
                "azimuth1.input_act_angle" to "headingController.output"
                "azimuth1.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "azimuth1.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "azimuth1.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "azimuth1.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "azimuth1.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "azimuth1.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                "vesselModel.additionalBodyForce[0].force.heave" to "azimuth0.output_force_heave"
                "vesselModel.additionalBodyForce[0].force.surge" to "azimuth0.output_force_surge"
                "vesselModel.additionalBodyForce[0].force.sway" to "azimuth0.output_force_sway"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.xpos" to "azimuth0.output_x_rel_ap"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.ypos" to "azimuth0.output_y_rel_cl"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.zpos" to "azimuth0.output_z_rel_bl"

                "vesselModel.additionalBodyForce[1].force.heave" to "azimuth1.output_force_heave"
                "vesselModel.additionalBodyForce[1].force.surge" to "azimuth1.output_force_surge"
                "vesselModel.additionalBodyForce[1].force.sway" to "azimuth1.output_force_sway"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.xpos" to "azimuth1.output_x_rel_ap"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.ypos" to "azimuth1.output_y_rel_cl"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.zpos" to "azimuth1.output_z_rel_bl"

                "headingController.setPoint" to "vesselModelObserver.trueHeading"
                "headingController.processOutput" to "gunnerus.trueCourse"

                "speedController.setPoint" to "gunnerus.speedOverGround"
                "speedController.processOutput" to "vesselModel.cgShipMotion.linearVelocity.surge"

                "vesselModelObserver.position.north" to "vesselModel.cgShipMotion.nedDisplacement.north"
                "vesselModelObserver.position.east" to "vesselModel.cgShipMotion.nedDisplacement.east"
            }

        }

        defaultExperiment {
            annotations {
                annotation("com.opensimulationplatform") {
                    """
                        <osp:Algorithm>
                            <osp:FixedStepAlgorithm baseStepSize="0.05" />
                        </osp:Algorithm>
                    """
                }
            }
        }

        namespaces {
            namespace("osp", "http://opensimulationplatform.com/SSP/OSPAnnotations")
        }

    }


}.build().also { println("Done") }
