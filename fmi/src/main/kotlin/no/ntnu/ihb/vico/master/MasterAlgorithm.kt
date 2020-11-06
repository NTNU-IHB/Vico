package no.ntnu.ihb.vico.master

import no.ntnu.ihb.vico.FmiSlave
import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback

abstract class MasterAlgorithm {

    protected lateinit var slaves: Collection<FmiSlave>
        private set

    internal open fun slaveAdded(slave: FmiSlave) {}

    internal open fun slaveRemoved(slave: FmiSlave) {}

    fun init(currentTime: Double, slaves: Collection<FmiSlave>, slaveInitCallback: SlaveInitCallback) {
        this.slaves = slaves
        initialize(currentTime, slaveInitCallback)
    }

    abstract fun initialize(currentTime: Double, slaveInitCallback: SlaveInitCallback)

    abstract fun step(
        currentTime: Double,
        stepSize: Double,
        slaveStepCallback: SlaveStepCallback
    )

    protected fun readAllVariables() {
        slaves.parallelStream().forEach {
            it.retrieveCachedGets()
        }
    }

    protected fun writeAllVariables() {
        slaves.parallelStream().forEach {
            it.transferCachedSets()
        }
    }

}
