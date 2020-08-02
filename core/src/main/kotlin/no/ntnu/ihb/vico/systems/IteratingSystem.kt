package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.SimulationSystem

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
