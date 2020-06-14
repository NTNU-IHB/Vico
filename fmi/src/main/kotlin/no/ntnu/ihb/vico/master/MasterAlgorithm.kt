package no.ntnu.ihb.vico.master

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback
import no.ntnu.ihb.vico.Slaves

abstract class MasterAlgorithm {

    protected lateinit var slaves: List<SlaveComponent>
        private set

    internal open fun slaveAdded(slave: SlaveComponent) {}

    internal open fun slaveRemoved(slave: SlaveComponent) {}

    fun init(currentTime: Double, slaves: List<SlaveComponent>, slaveInitCallback: SlaveInitCallback) {
        this.slaves = slaves
        initialize(currentTime, slaveInitCallback)
    }

    abstract fun initialize(currentTime: Double, slaveInitCallback: SlaveInitCallback)

    abstract fun step(
        currentTime: Double,
        stepSize: Double,
        slaveStepCallback: SlaveStepCallback
    )

    protected companion object {

        fun readAllVariables(slaves: Slaves) {
            runBlocking {
                slaves.forEach {
                    launch {
                        it.asyncRetrieveCachedGets()
                    }
                }
            }
        }

        fun writeAllVariables(slaves: Slaves) {
            runBlocking {
                slaves.forEach { slave ->
                    launch {
                        slave.asyncTransferCachedSets()
                    }
                }
            }
        }

    }

}
