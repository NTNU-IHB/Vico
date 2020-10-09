@file:Repository("https://dl.bintray.com/ntnu-ihb/mvn")
@file:DependsOn("no.ntnu.ihb.vico:core:0.2.0")

import no.ntnu.ihb.vico.dsl.scenario

scenario {

    invokeAt(1.0) {
        println("Hello from file")
    }

}
