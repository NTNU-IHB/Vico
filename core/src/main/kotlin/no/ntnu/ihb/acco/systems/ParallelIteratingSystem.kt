package no.ntnu.ihb.acco.systems

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.System

abstract class ParallelIteratingSystem(
    family: Family,
    decimationFactor: Long = 1,
    priority: Int = 0
) : System(family, decimationFactor, priority) {

    override fun step(currentTime: Double, stepSize: Double) {
        entities.parallelStream().forEach { e ->
            processEntity(e, currentTime, stepSize)
        }
    }

    protected abstract fun processEntity(entity: Entity, currentTime: Double, stepSize: Double)
}
