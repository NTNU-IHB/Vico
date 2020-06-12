package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem

abstract class IteratingSystem(
    family: Family
) : SimulationSystem(family) {

    override fun step(currentTime: Double, stepSize: Double) {
        entities.forEach { e ->
            processEntity(e, currentTime, stepSize)
        }
    }

    protected abstract fun processEntity(entity: Entity, currentTime: Double, stepSize: Double)

}
