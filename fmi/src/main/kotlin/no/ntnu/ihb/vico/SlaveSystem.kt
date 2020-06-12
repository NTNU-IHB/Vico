package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.SimulationSystem
import no.ntnu.ihb.fmi4j.writeBoolean
import no.ntnu.ihb.fmi4j.writeInteger
import no.ntnu.ihb.fmi4j.writeReal
import no.ntnu.ihb.fmi4j.writeString
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.master.MasterAlgorithm

typealias Slaves = List<SlaveComponent>
typealias SlaveInitCallback = (SlaveComponent) -> Unit
typealias SlaveStepCallback = (Pair<Double, SlaveComponent>) -> Unit

val Engine.slaveSystem: SlaveSystem
    get() = this.getSystem(SlaveSystem::class.java)

class SlaveSystem @JvmOverloads constructor(
    private val algorithm: MasterAlgorithm = FixedStepMaster()
) : SimulationSystem(Family.all(SlaveComponent::class.java).build()) {

    var parameterSet: String = "default"

    private val _slaves: MutableList<SlaveComponent> = mutableListOf()
    val slaves: List<SlaveComponent> = _slaves

    private val listeners: MutableList<SlaveSystemListener> = mutableListOf()

    fun getSlave(name: String): SlaveComponent = _slaves.first { it.instanceName == name }
    fun getSlaveNoExcept(name: String): SlaveComponent? = _slaves.firstOrNull { it.instanceName == name }

    override fun entityAdded(entity: Entity) {
        val slave = entity.getComponent(SlaveComponent::class.java)
        slave.getParameterSet(parameterSet)?.also {
            it.integerParameters.forEach { p -> slave.writeInteger(p.name, p.value) }
            it.realParameters.forEach { p -> slave.writeReal(p.name, p.value) }
            it.booleanParameters.forEach { p -> slave.writeBoolean(p.name, p.value) }
            it.stringParameters.forEach { p -> slave.writeString(p.name, p.value) }
        }
        _slaves.add(slave)
        algorithm.slaveAdded(slave)
        listeners.forEach { l -> l.slaveAdded(slave) }
    }

    override fun entityRemoved(entity: Entity) {
        val slave = entity.getComponent(SlaveComponent::class.java)
        _slaves.remove(slave)
        algorithm.slaveRemoved(slave)
        listeners.forEach { l -> l.slaveRemoved(slave) }
    }

    override fun init(currentTime: Double) {
        algorithm.init(currentTime) { slave ->
            engine.updateConnection(slave)
        }
        listeners.forEach { l -> l.postInit(currentTime) }
    }

    override fun step(currentTime: Double, stepSize: Double) {
        algorithm.step(currentTime, stepSize) {
            listeners.forEach { l -> l.postSlaveStep(it.first, it.second) }
            dispatchEvent(SLAVE_STEPPED, it)
        }
        listeners.forEach { l -> l.postStep(currentTime + stepSize) }
    }

    /*fun addConnection(connection: SlaveConnection<*>) = apply {
        connections.computeIfAbsent(connection.sourceSlave) { mutableListOf() }.add(connection)
        connection.sourceSlave.markForReading(connection.sourceVariable.name)
    }*/

    fun addListener(listener: SlaveSystemListener) = apply {
        listeners.add(listener).also {
            listener.addedToSystem(this)
        }
        if (addedToEngine) {
            slaves.forEach { slave ->
                listener.slaveAdded(slave)
            }
        }
    }

    override fun close() {
        _slaves.forEach { slave ->
            slave.terminate()
        }
        listeners.forEach { l -> l.postTerminate() }
        _slaves.forEach { slave ->
            slave.close()
        }
        listeners.forEach { l -> l.close() }
    }

    companion object {

        const val SLAVE_STEPPED = "slaveStepped"

    }

}

/*
class FixedStepSlaveSystem(
    decimationFactor: Long = 1,
    priority: Int = 0
) : SlaveSystem(decimationFactor, priority) {

    var parameterSet: String = "default"
    private var logger: SlaveLogger? = null

    private val connections: MutableMap<SlaveComponent, MutableList<SlaveConnection<*>>> = mutableMapOf()
    private val groups: SortedMap<Int, MutableList<SlaveComponent>> = TreeMap(Comparator { o1, o2 -> o2.compareTo(o1) })

    internal val slaves: List<SlaveComponent>
        get() = groups.flatMap { it.value }

    private val pendingConnections = mutableListOf<Connection>()

    fun setupLogging(resultDir: File? = null) {
        logger = SlaveLogger(resultDir)
    }

    fun getSlave(name: String) = slaves.first { it.instanceName == name }

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

        pendingConnections.forEach { c ->
            val source = slaves.first { it.instanceName == c.source.instanceName }
            val target = slaves.first { it.instanceName == c.target.instanceName }
            when (c.sourceVariable.type) {
                VariableType.INTEGER, VariableType.ENUMERATION -> {
                    IntegerConnection(
                        source,
                        c.sourceVariable as IntegerVariable,
                        target,
                        c.targetVariable as IntegerVariable
                    )
                }
                VariableType.REAL -> {
                    RealConnection(source, c.sourceVariable as RealVariable, target, c.targetVariable as RealVariable)
                }
                else -> TODO()
            }
        }

        slaves.parallelStream().forEach { slave ->
            logger?.setup(slave)
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

    fun addConnection(connection: Connection) {
        pendingConnections.add(connection)
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
*/
