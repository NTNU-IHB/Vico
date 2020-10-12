@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.2.0")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

    invokeAt(30.0) {
        bool("vesselModel.slaveComponent.reset_position").set(true)
    }
    invokeAt(30.1) {
        bool("vesselModel.slaveComponent.reset_position").set(false)
        bool("trackController.slaveComponent.enable").set(true)
    }

}
