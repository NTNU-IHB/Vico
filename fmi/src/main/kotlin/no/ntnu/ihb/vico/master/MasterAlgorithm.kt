package no.ntnu.ihb.vico.master

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveInitCallback
import no.ntnu.ihb.vico.SlaveStepCallback
import no.ntnu.ihb.vico.Slaves

abstract class MasterAlgorithm {

    abstract fun slaveAdded(slave: SlaveComponent)

    abstract fun slaveRemoved(slave: SlaveComponent)

    abstract fun init(currentTime: Double, slaveInitCallback: SlaveInitCallback)

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
