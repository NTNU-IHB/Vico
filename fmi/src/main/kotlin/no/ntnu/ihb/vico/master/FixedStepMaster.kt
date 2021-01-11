package no.ntnu.ihb.vico.master

import no.ntnu.ihb.vico.FmiSlave
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback
import no.ntnu.ihb.vico.core.Engine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.ceil
import kotlin.math.max

class FixedStepMaster @JvmOverloads constructor(
    private val parallel: Boolean = true
) : MasterAlgorithm() {

    private var stepNumber: Long = 0
    private val decimationMap: MutableMap<FmiSlave, Long> = mutableMapOf()

    override fun initialize(engine: Engine, slaveInitCallback: SlaveInitCallback) {
        slaves.forEach { slave ->
            slave.setupExperiment(engine.currentTime)
            slave.enterInitializationMode()
        }
        for (slave in slaves) {
            slave.transferCachedSets()
            slave.retrieveCachedGets()
            slaveInitCallback.invoke(slave)
        }
        slaves.forEach { slave ->
            slave.exitInitializationMode()
        }
        readAllVariables()

        slaves.forEach { slave ->
            val decimationFactor = calculateStepFactor(slave.component, engine.baseStepSize)
            decimationMap[slave] = decimationFactor
        }
    }

    override fun step(
        currentTime: Double,
        stepSize: Double,
        slaveStepCallback: SlaveStepCallback
    ) {
        val tNext = currentTime + stepSize
        val stream = if (parallel) slaves.parallelStream() else slaves.stream()
        stream.forEach { slave ->
            val decimationFactor = decimationMap[slave] ?: 1L
            if (stepNumber % decimationFactor == 0L) {
                slave.transferCachedSets()
                slave.doStep(stepSize * decimationFactor)
                slave.retrieveCachedGets()
                slaveStepCallback.invoke(tNext to slave.component)
            }
        }
        stepNumber++
    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(MasterAlgorithm::class.java)

        fun calculateStepFactor(slave: SlaveComponent, baseStepSize: Double): Long {
            val stepSizeHint: Double = slave.stepSizeHint ?: return 1
            val decimationFactor = max(1, ceil(stepSizeHint / baseStepSize).toLong())
            val actualStepSize = baseStepSize * decimationFactor
            if (actualStepSize.compareTo(stepSizeHint) != 0) {
                LOG.warn("Actual step size for ${slave.instanceName} will be $actualStepSize rather than requested value $stepSizeHint.")
            }
            return decimationFactor
        }

    }

}
