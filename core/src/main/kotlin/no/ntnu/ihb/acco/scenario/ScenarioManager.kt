package no.ntnu.ihb.acco.scenario

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.ObserverSystem

class ScenarioManager(
    val scenario: Scenario
) : ObserverSystem(Family.all) {

    override fun entityAdded(entity: Entity) {


    }

    override fun observe(currentTime: Double) {

    }

}
