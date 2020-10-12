@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.2.0")

import no.ntnu.ihb.vico.dsl.scenario

val tReset = 50.0

scenario {

    invokeAt(tReset) {
        bool("vesselModel.slaveComponent.reset_position").set(true)
    }
    invokeAt(tReset + 0.1) {
        bool("vesselModel.slaveComponent.reset_position").set(false)
        bool("trackController.slaveComponent.enable").set(true)
    }

}
