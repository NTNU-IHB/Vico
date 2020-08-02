package no.ntnu.ihb.vico.systems

import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.SimulationSystem

abstract class ParallelIteratingSystem(
    family: Family
) : SimulationSystem(family) {

    override fun step(currentTime: Double, stepSize: Double) {
        entities.parallelStream().forEach { e ->
            processEntity(e, currentTime, stepSize)
        }
    }

    protected abstract fun processEntity(entity: Entity, currentTime: Double, stepSize: Double)
}
