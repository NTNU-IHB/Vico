package no.ntnu.ihb.vico.master

import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveConnections
import no.ntnu.ihb.vico.SlaveStepCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.ceil
import kotlin.math.max

class FixedStepMaster : MasterAlgorithm() {

    private var currentTime = 0.0

    private val slaves = mutableListOf<SlaveComponent>()

    override fun slaveAdded(slave: SlaveComponent) {
        slaves.add(slave)
    }

    override fun slaveRemoved(slave: SlaveComponent) {
        TODO("Not yet implemented")
    }

    override fun init(currentTime: Double, connections: SlaveConnections) {
        this.currentTime = currentTime
        slaves.parallelStream().forEach { slave ->
            slave.setupExperiment(currentTime)
            slave.enterInitializationMode()
        }
        for (i in slaves.indices) {
            writeAllVariables(slaves)
            readAllVariables(slaves)
            connections.values.flatten().forEach { c ->
                c.transferData()
            }
        }
        slaves.parallelStream().forEach { slave ->
            slave.exitInitializationMode()
        }
        readAllVariables(slaves)
    }

    override fun step(
        currentTime: Double,
        stepSize: Double,
        connections: SlaveConnections,
        slaveStepCallback: SlaveStepCallback
    ) {
        val tNext = currentTime + stepSize
        slaves.parallelStream().forEach { slave ->
            slave.doStep(stepSize)
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
