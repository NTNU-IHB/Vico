@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.3.3")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

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
