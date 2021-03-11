@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.4.0")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

    init {
        real("headingController.kp").set(2)
        real("headingController.ti").set(0.01)
        real("headingController.td").set(0.001)

        real("speedController.kp").set(8500)
        real("speedController.ti").set(875)
        real("speedController.td").set(0.5)
    }

    invokeAt(49.5) {
        bool("vesselModel.reset_position").set(true)
        bool("vesselModel.reset_velocity").set(true)
    }
    invokeAt(50) {
        bool("vesselModel.reset_position").set(false)
        bool("vesselModel.reset_velocity").set(false)
    }
    invokeAt(50.1) {
        bool("headingController.enabled").set(true)
        bool("speedController.enabled").set(true)
    }

}
