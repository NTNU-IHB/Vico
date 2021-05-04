@file:DependsOn("info.laht.vico:core:0.4.1")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

    invokeAt(1.0) {
        println("Hello from file")
    }

}
