package no.ntnu.ihb.vico.master

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.acco.core.EventDispatcher
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveConnections
import no.ntnu.ihb.vico.SlaveStepCallback
import no.ntnu.ihb.vico.Slaves

abstract class MasterAlgorithm {

/*    protected var baseStepSize: Double = 0.0
        private set

    internal fun assignedToSystem(system: SlaveSystem) {
        baseStepSize = system.interval
    }*/

    internal lateinit var dispatcher: EventDispatcher

    protected fun dispatchEvent(type: String, target: Any?) = dispatcher.dispatchEvent(type, target)

    abstract fun slaveAdded(slave: SlaveComponent)

    abstract fun slaveRemoved(slave: SlaveComponent)

    abstract fun init(currentTime: Double, connections: SlaveConnections)

    abstract fun step(
        currentTime: Double,
        stepSize: Double,
        connections: SlaveConnections,
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
