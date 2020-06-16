package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import org.osp.cosim.CosimExecution

class CosimSystem : SimulationSystem(Family.all(CosimFmuComponent::class.java).build()) {

    lateinit var execution: CosimExecution

    override fun init(currentTime: Double) {
        execution = CosimExecution.create(startTime = currentTime, stepSize = engine.baseStepSize)
    }

    override fun entityAdded(entity: Entity) {
        val fmuComponent = entity.getComponent<CosimFmuComponent>()
        execution.addSlave(fmuComponent.source, fmuComponent.instanceName)
    }

    override fun entityRemoved(entity: Entity) {

    }

    override fun step(currentTime: Double, stepSize: Double) {
        execution.step(1)
    }

    override fun close() {
        execution.close()
    }

}
