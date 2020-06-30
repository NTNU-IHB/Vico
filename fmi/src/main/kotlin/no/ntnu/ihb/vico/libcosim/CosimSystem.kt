package no.ntnu.ihb.vico.libcosim

import com.opensimulationplatform.cosim.CosimExecution
import com.opensimulationplatform.cosim.CosimLastValueObserver
import com.opensimulationplatform.cosim.CosimOverrideManipulator
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.Properties
import no.ntnu.ihb.acco.core.SimulationSystem
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
        throw UnsupportedOperationException()
    }

    override fun step(currentTime: Double, stepSize: Double) {
        execution.step()
        dispatchEvent(Properties.PROPERTIES_CHANGED, Unit)
    }

    override fun close() {
        execution.close()
    }

}
