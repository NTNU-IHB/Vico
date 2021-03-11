@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.4.0")

import no.ntnu.ihb.vico.dsl.scenario

val dt = 0.05
val tReset = 49.95

scenario {

    invokeAt(tReset) {
        bool("vesselModel.reset_position").set(true)
    }
    invokeAt(tReset + dt) {
        bool("vesselModel.reset_position").set(false)
        bool("trackController.enable").set(true)
    }

}
