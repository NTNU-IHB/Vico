package no.ntnu.ihb.vico.master

import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FixedStepMaster @JvmOverloads constructor(
        private val parallel: Boolean = true
) : MasterAlgorithm() {

    override fun initialize(currentTime: Double, slaveInitCallback: SlaveInitCallback) {
        slaves.forEach { slave ->
            slave.setupExperiment(currentTime)
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
    }

    override fun step(
        currentTime: Double,
        stepSize: Double,
        slaveStepCallback: SlaveStepCallback
    ) {
        val tNext = currentTime + stepSize
        val stream = if (parallel) slaves.parallelStream() else slaves.stream()
        stream.forEach { slave ->
            slave.transferCachedSets()
            slave.doStep(stepSize)
            slave.retrieveCachedGets()
            slaveStepCallback.invoke(tNext to slave.component)
        }
    }

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(MasterAlgorithm::class.java)

        /* fun calculateStepFactor(slave: SlaveComponent, baseStepSize: Double): Long {
             val stepSizeHint: Double = slave.stepSizeHint ?: return 1
             val decimationFactor = max(1, ceil(stepSizeHint / baseStepSize).toLong())
             val actualStepSize = baseStepSize * decimationFactor
             if (actualStepSize.compareTo(stepSizeHint) != 0) {
                 LOG.warn("Actual step size for ${slave.instanceName} will be $actualStepSize rather than requested value $stepSizeHint.")
             }
             return decimationFactor
         }*/

    }

}
