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

ssp("gunnerus-twin2") {

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
                        real("cg_x_rel_ap", output)
                        real("cg_y_rel_cl", output)
                        real("cg_z_rel_bl", output)

                        real("input_global_wind_dir", input)
                        real("input_global_wind_vel", input)

                        real("cgShipMotion.nedDisplacement.north", output)
                        real("cgShipMotion.nedDisplacement.east", output)
                        real("cgShipMotion.linearVelocity.surge", output)
                        real("cgShipMotion.linearVelocity.sway", output)
                        real("cgShipMotion.angularVelocity.yaw", output)
                        real("cgShipMotion.angularDisplacement.yaw", output)

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
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            string("vesselZipFile", "%fmu%/resources/ShipModel-gunnerus-elongated.zip")
                            boolean("additionalBodyForce[0].enabled", true)
                            boolean("additionalBodyForce[1].enabled", true)
                            boolean("initialize_using_hydrostatics", false)
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

                component("portAzimuth", "resources/PMAzimuth-proxy.fmu") {
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
                            real("input_x_rel_ap", 0)
                            real("input_y_rel_cl", -2.7)
                            real("input_z_rel_bl", 0.55)
                            real("input_prop_diam", 1.9)
                            real("input_distancetohull", 1.5)
                            real("input_bilgeradius", 3)
                            real("input_rho", 1025)
                            real("input_lpp", 33.9)
                        }
                    }
                }

                component("starboardAzimuth", "resources/PMAzimuth-proxy.fmu") {
                    connectors {
                        copyFrom("portAzimuth")
                    }
                    parameterBindings {
                        copyFrom("portAzimuth", "initialValues") {
                            real("input_y_rel_cl", 2.7)
                        }
                    }
                }

                component("portAzimuth_rpmActuator", "resources/ThrusterDrive2.fmu") {
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

                component("starboardAzimuth_rpmActuator", "resources/ThrusterDrive2.fmu") {
                    connectors {
                        copyFrom("portAzimuth_rpmActuator")
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
                            real("kp", 1)
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

                "powerPlant.p1.f[1]" to "portAzimuth_rpmActuator.d_in.f"
                "powerPlant.p1.f[2]" to "portAzimuth_rpmActuator.q_in.f"
                "powerPlant.p2.f[1]" to "starboardAzimuth_rpmActuator.d_in.f"
                "powerPlant.p2.f[2]" to "starboardAzimuth_rpmActuator.q_in.f"

                "portAzimuth_rpmActuator.d_in.e" to "powerPlant.p1.e[1]"
                "portAzimuth_rpmActuator.q_in.e" to "powerPlant.p1.e[2]"
                "portAzimuth_rpmActuator.ThrustCom" to "speedController.output"
                "portAzimuth_rpmActuator.Shaft.e" to "portAzimuth.output_torque"

                "starboardAzimuth_rpmActuator.d_in.e" to "powerPlant.p2.e[1]"
                "starboardAzimuth_rpmActuator.q_in.e" to "powerPlant.p2.e[2]"
                "starboardAzimuth_rpmActuator.ThrustCom" to "speedController.output"
                "starboardAzimuth_rpmActuator.Shaft.e" to "starboardAzimuth.output_torque"

                ("portAzimuth.input_act_revs" to "portAzimuth_rpmActuator.Shaft.f").linearTransformation(factor = 60.0 / (2 * PI))
                "portAzimuth.input_act_angle" to "headingController.output"
                "portAzimuth.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "portAzimuth.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "portAzimuth.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "portAzimuth.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "portAzimuth.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "portAzimuth.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                ("starboardAzimuth.input_act_revs" to "starboardAzimuth_rpmActuator.Shaft.f").linearTransformation(factor = 60.0 / (2 * PI))
                "starboardAzimuth.input_act_angle" to "headingController.output"
                "starboardAzimuth.input_cg_x_rel_ap" to "vesselModel.cg_x_rel_ap"
                "starboardAzimuth.input_cg_y_rel_cl" to "vesselModel.cg_y_rel_cl"
                "starboardAzimuth.input_cg_z_rel_bl" to "vesselModel.cg_z_rel_bl"
                "starboardAzimuth.input_cg_surge_vel" to "vesselModel.cgShipMotion.linearVelocity.surge"
                "starboardAzimuth.input_cg_sway_vel" to "vesselModel.cgShipMotion.linearVelocity.sway"
                "starboardAzimuth.input_yaw_vel" to "vesselModel.cgShipMotion.angularVelocity.yaw"

                "vesselModel.additionalBodyForce[0].force.heave" to "portAzimuth.output_force_heave"
                "vesselModel.additionalBodyForce[0].force.surge" to "portAzimuth.output_force_surge"
                "vesselModel.additionalBodyForce[0].force.sway" to "portAzimuth.output_force_sway"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.xpos" to "portAzimuth.output_x_rel_ap"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.ypos" to "portAzimuth.output_y_rel_cl"
                "vesselModel.additionalBodyForce[0].pointOfAttackRel2APAndBL.zpos" to "portAzimuth.output_z_rel_bl"

                "vesselModel.additionalBodyForce[1].force.heave" to "starboardAzimuth.output_force_heave"
                "vesselModel.additionalBodyForce[1].force.surge" to "starboardAzimuth.output_force_surge"
                "vesselModel.additionalBodyForce[1].force.sway" to "starboardAzimuth.output_force_sway"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.xpos" to "starboardAzimuth.output_x_rel_ap"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.ypos" to "starboardAzimuth.output_y_rel_cl"
                "vesselModel.additionalBodyForce[1].pointOfAttackRel2APAndBL.zpos" to "starboardAzimuth.output_z_rel_bl"

                "headingController.setPoint" to "vesselModelObserver.trueHeading"
                "headingController.processOutput" to "gunnerus.trueCourse"

                "speedController.setPoint" to "gunnerus.speedOverGround"
                "speedController.processOutput" to "vesselModel.cgShipMotion.linearVelocity.surge"

                "vesselModelObserver.position.north" to "vesselModel.cgShipMotion.nedDisplacement.north"
                "vesselModelObserver.position.east" to "vesselModel.cgShipMotion.nedDisplacement.east"

                ("vesselModel.input_global_wind_vel" to "gunnerus.wind.speed")
                ("vesselModel.input_global_wind_dir" to "gunnerus.wind.direction").linearTransformation(offset = -180)
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
