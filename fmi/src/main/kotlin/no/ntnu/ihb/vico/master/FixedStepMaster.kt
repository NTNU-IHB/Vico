package no.ntnu.ihb.vico.master

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.ceil
import kotlin.math.max

class FixedStepMaster : MasterAlgorithm() {

    override fun initialize(currentTime: Double, slaveInitCallback: SlaveInitCallback) {
        slaves.parallelStream().forEach { slave ->
            slave.setupExperiment(currentTime)
            slave.enterInitializationMode()
        }
        for (slave in slaves) {
            slave.transferCachedSets()
            slave.retrieveCachedGets()
            slaveInitCallback.invoke(slave)
        }
        slaves.parallelStream().forEach { slave ->
            slave.exitInitializationMode()
        }
        readAllVariables(slaves)
    }

    override fun step(
        currentTime: Double,
        stepSize: Double,
        slaveStepCallback: SlaveStepCallback
    ) {
        val tNext = currentTime + stepSize
        slaves.parallelStream().forEach { slave ->
            slave.transferCachedSets()
            slave.doStep(stepSize)
            slave.retrieveCachedGets()
            slaveStepCallback.invoke(tNext to slave)
        }
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
