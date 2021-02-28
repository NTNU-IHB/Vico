@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.sspgen:dsl:0.5.0")

import no.ntnu.ihb.sspgen.dsl.ssp

val stepSize = 1.0 / 1000

ssp("QuarterTruck") {

    resources {
        file("fmus/chassis.fmu")
        file("fmus/wheel.fmu")
        file("fmus/ground.fmu")
    }

    ssd("QuarterTruck") {

        system("QuarterTruckSystem") {

            elements {
                component("chassis", "resources/chassis.fmu") {
                    connectors {
                        real("p.e", output)
                        real("p.f", input)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("C.mChassis", 400)
                            real("C.kChassis", 15000)
                            real("R.dChassis", 1000)
                        }
                    }
                }

                component("wheel", "resources/wheel.fmu") {
                    connectors {
                        real("p.f", input)
                        real("p1.e", input)
                        real("p.e", output)
                        real("p1.f", output)
                    }
                    parameterBindings {
                        parameterSet("initialValues") {
                            real("C.mWheel", 40)
                            real("C.kWheel", 150000)
                            real("R.dWheel", 0)
                        }
                    }
                }

                component("ground", "resources/ground.fmu") {
                    connectors {
                        real("p.e", input)
                        real("p.f", output)
                    }
                }
            }

            connections {

                "chassis.p.e" to "wheel.p1.e"
                "wheel.p1.f" to "chassis.p.f"
                "wheel.p.e" to "ground.p.e"
                "ground.p.f" to "wheel.p.f"

            }

            annotations {
                annotation("org.openmodelica") {
                    """
                        <oms:SimulationInformation>
                            <oms:FixedStepMaster description="oms-ma" stepSize="$stepSize" absoluteTolerance="0.000100" relativeTolerance="0.000100" />
                        </oms:SimulationInformation>
                    """
                }
            }

        }

        defaultExperiment {
            annotations {
                annotation("org.openmodelica") {
                    """
                        <oms:SimulationInformation resultFile="../results/omsimulator/results.mat" loggingInterval="0.0" bufferSize="8192" signalFilter="" />
                    """
                }
                annotation("com.opensimulationplatform") {
                    """
                        <osp:Algorithm>
                            <osp:FixedStepAlgorithm baseStepSize="$stepSize" />
                        </osp:Algorithm>
                    """
                }
            }
        }

        namespaces {
            namespace("oms", "http://openmodelica.org/oms")
            namespace("osp", "http://opensimulationplatform.com/SSP/OSPAnnotations")
        }

    }

}.build()
