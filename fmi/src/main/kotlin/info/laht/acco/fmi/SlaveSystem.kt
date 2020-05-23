package info.laht.acco.fmi

import info.laht.acco.core.Entity
import info.laht.acco.core.Family
import info.laht.acco.core.System
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SlaveSystem(
    decimationFactor: Int = 1,
    priority: Int = 0
) : System(Family.all(SlaveComponent::class.java).build(), decimationFactor, priority) {

    private val groups: SortedMap<Int, MutableList<SlaveComponent>> = TreeMap(Comparator { o1, o2 -> o2.compareTo(o1) })

    private val slaves: List<SlaveComponent>
        get() = groups.flatMap { it.value }

    private val connections = mutableListOf<Connection<*>>()

    override fun entityAdded(entity: Entity) {
        val slave = entity.getComponent(SlaveComponent::class.java)
        groups.computeIfAbsent(slave.decimationFactor) {
            mutableListOf()
        }.add(slave)
    }

    override fun init() {
        slaves.forEach { slave ->
            slave.setup()
            slave.enterInitializationMode()
        }
        for (i in slaves.indices) {
            writeAllVariables(slaves)
            readAllVariables(slaves)
            connections.forEach { c -> c.transferData() }
        }
        slaves.forEach { slave ->
            slave.exitInitializationMode()
        }
        readAllVariables(slaves)
    }

    override fun step(currentTime: Double, baseStepSize: Double) {
        val stepSize = baseStepSize * groups.firstKey()
        val endTime = currentTime + stepSize
        groups.forEach { (decimationFactor, slaveGroup) ->
            slaveGroup.forEach { slave ->
                var t = currentTime
                val dt = baseStepSize * decimationFactor
                do {
                    slave.doStep(dt)
                    t += dt
                } while (t < endTime)
            }
        }
        connections.forEach { c -> c.transferData() }
    }

    fun addConnection(connection: Connection<*>) {
        connections.add(connection)
    }

    private fun readAllVariables(slaves: List<SlaveComponent>) {
        runBlocking {
            slaves.forEach {
                launch {
                    it.asyncRetrieveCachedGets()
                }
            }
        }
    }

    private fun writeAllVariables(slaves: List<SlaveComponent>) {
        runBlocking {
            slaves.forEach { slave ->
                launch {
                    slave.asyncTransferCachedSets()
                }
            }
        }
    }

    override fun close() {
        slaves.forEach { slave ->
            slave.terminate()
            slave.close()
        }
    }

}
