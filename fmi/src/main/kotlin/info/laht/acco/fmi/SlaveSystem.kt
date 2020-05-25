package info.laht.acco.fmi

import info.laht.acco.core.Entity
import info.laht.acco.core.Family
import info.laht.acco.core.System
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*


class SlaveSystem(
    decimationFactor: Int = 1,
    priority: Int = 0
) : System(Family.all(SlaveComponent::class.java).build(), decimationFactor, priority) {

    private var logger: SlaveLogger? = null

    private val connections: MutableMap<SlaveComponent, MutableList<Connection<*>>> = mutableMapOf()
    private val groups: SortedMap<Int, MutableList<SlaveComponent>> = TreeMap(Comparator { o1, o2 -> o2.compareTo(o1) })

    private val slaves: List<SlaveComponent>
        get() = groups.flatMap { it.value }

    fun setupLogging(resultDir: File? = null) {
        logger = SlaveLogger(resultDir)
    }

    override fun entityAdded(entity: Entity) {
        val slave = entity.getComponent(SlaveComponent::class.java)
        groups.computeIfAbsent(slave.decimationFactor) {
            mutableListOf()
        }.add(slave)
        logger?.setup(slave)
    }

    override fun init(currentTime: Double) {
        slaves.parallelStream().forEach { slave ->
            slave.setup(currentTime)
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

        logger?.also { logger ->
            slaves.parallelStream().forEach { slave ->
                logger.postInit(slave, currentTime)
            }
        }
    }

    override fun step(currentTime: Double, stepSize: Double) {
        val biggestStepSize = stepSize * groups.firstKey()
        val endTime = currentTime + biggestStepSize
        groups.forEach { (decimationFactor, slaveGroup) ->
            var t = currentTime
            val dt = stepSize * decimationFactor
            do {
                slaveGroup.parallelStream().forEach { slave ->
                    slave.transferCachedSets()
                    slave.doStep(dt)
                    slave.retrieveCachedGets()
                    logger?.postStep(slave, currentTime)
                }
                slaveGroup.forEach { slave ->
                    connections[slave]?.forEach { c ->
                        c.transferData()
                    }
                }
                t += dt
            } while (t < endTime)
        }
    }

    fun addConnection(connection: Connection<*>) {
        connections.computeIfAbsent(connection.sourceSlave) { mutableListOf(connection) }
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
        logger?.close()
    }

}
