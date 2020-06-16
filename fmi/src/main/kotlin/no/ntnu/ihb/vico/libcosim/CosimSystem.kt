package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import org.osp.cosim.CosimExecution
import java.io.File
import java.util.*

class CosimLogConfig @JvmOverloads constructor(
    val logDir: File,
    val configFile: File? = null
)

class CosimSystem @JvmOverloads constructor(
    private val logConfig: CosimLogConfig? = null
) : SimulationSystem(Family.all(CosimFmuComponent::class.java).build()) {

    lateinit var execution: CosimExecution

    private val entityQueue = ArrayDeque<Entity>()

    private fun addSlave(entity: Entity) {
        val fmuComponent = entity.getComponent<CosimFmuComponent>()
        execution.addSlave(fmuComponent.source, fmuComponent.instanceName)
    }

    override fun init(currentTime: Double) {
        execution = CosimExecution.create(
            startTime = currentTime,
            stepSize = engine.baseStepSize * decimationFactor
        )

        while (!entityQueue.isEmpty()) {
            addSlave(entityQueue.poll())
        }

        logConfig?.also {
            execution.addFileObserver(it.logDir, it.configFile)
        }
    }

    override fun entityAdded(entity: Entity) {
        if (initialized) {
            addSlave(entity)
        } else {
            entityQueue.add(entity)
        }
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
