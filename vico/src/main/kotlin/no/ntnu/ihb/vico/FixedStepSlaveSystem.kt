package no.ntnu.ihb.vico

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.System
import no.ntnu.ihb.fmi4j.writeBoolean
import no.ntnu.ihb.fmi4j.writeInteger
import no.ntnu.ihb.fmi4j.writeReal
import no.ntnu.ihb.fmi4j.writeString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

abstract class AbstractSlaveSystem(
    decimationFactor: Int = 1,
    priority: Int = 0,
    family: Family = Family.all(SlaveComponent::class.java).build()
) : System(family, decimationFactor, priority)

class FixedStepSlaveSystem(
    decimationFactor: Int = 1,
    priority: Int = 0
) : AbstractSlaveSystem(decimationFactor, priority) {

    var parameterSet: String = "default"
    private var logger: SlaveLogger? = null

    private val connections: MutableMap<SlaveComponent, MutableList<SlaveConnection<*>>> = mutableMapOf()
    private val groups: SortedMap<Int, MutableList<SlaveComponent>> = TreeMap(Comparator { o1, o2 -> o2.compareTo(o1) })

    private val slaves: List<SlaveComponent>
        get() = groups.flatMap { it.value }

    fun setupLogging(resultDir: File? = null) {
        logger = SlaveLogger(resultDir)
    }

    override fun entityAdded(entity: Entity) {
        val slaveComponent = entity.getComponent(SlaveComponent::class.java)
        val slaveDecimationFactor = calculateStepFactor(slaveComponent, engine.baseStepSize * decimationFactor)

        groups.computeIfAbsent(slaveDecimationFactor) { mutableListOf() }.add(slaveComponent)
        slaveComponent.getParameterSet(parameterSet)?.also {
            it.integerParameters.forEach { p -> slaveComponent.writeInteger(p.name, p.value) }
            it.realParameters.forEach { p -> slaveComponent.writeReal(p.name, p.value) }
            it.booleanParameters.forEach { p -> slaveComponent.writeBoolean(p.name, p.value) }
            it.stringParameters.forEach { p -> slaveComponent.writeString(p.name, p.value) }
        }
        logger?.setup(slaveComponent)

    }

    override fun init(currentTime: Double) {
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

    fun addConnection(connection: SlaveConnection<*>) {
        connections.computeIfAbsent(connection.sourceSlave) { mutableListOf() }
            .add(connection)
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

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(FixedStepSlaveSystem::class.java)

        fun calculateStepFactor(slave: SlaveComponent, baseStepSize: Double): Int {
            val stepSizeHint: Double = slave.stepSizeHint ?: return 1
            val decimationFactor = max(1, ceil(stepSizeHint / baseStepSize).toInt())
            val actualStepSize = baseStepSize * decimationFactor
            if (actualStepSize.compareTo(stepSizeHint) != 0) {
                LOG.warn("Actual step size for ${slave.instanceName} will be $actualStepSize rather than requested value $stepSizeHint.")
            }
            return decimationFactor
        }

    }

}
