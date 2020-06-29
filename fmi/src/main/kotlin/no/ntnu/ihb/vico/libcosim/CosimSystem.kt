package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import org.osp.cosim.CosimExecution
import org.osp.cosim.CosimFileObserver
import org.osp.cosim.CosimLastValueObserver
import org.osp.cosim.CosimOverrideManipulator
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
    lateinit var observer: CosimLastValueObserver
    lateinit var manipulator: CosimOverrideManipulator
    private val entityQueue = ArrayDeque<Entity>()

    private var fileObserver: CosimFileObserver? = null

    private fun addSlave(entity: Entity) {
        entity.getComponent<CosimFmuComponent>().apply(execution, observer, manipulator)
    }

    override fun init(currentTime: Double) {
        execution = CosimExecution.create(
            startTime = currentTime,
            stepSize = engine.baseStepSize * decimationFactor
        )
        observer = execution.addLastValueObserver()
        manipulator = execution.addOverrideManipulator()

        while (!entityQueue.isEmpty()) {
            addSlave(entityQueue.poll())
        }

        logConfig?.also {
            fileObserver = execution.addFileObserver(it.logDir, it.configFile)
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
        throw UnsupportedOperationException()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        execution.step(1)
    }

    override fun close() {
        fileObserver?.close()
        execution.close()
    }

}
