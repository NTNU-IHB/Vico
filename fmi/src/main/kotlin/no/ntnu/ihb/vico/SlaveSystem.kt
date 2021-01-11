package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.importer.DirectAccessor
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.VariableType
import no.ntnu.ihb.vico.core.*
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.master.MasterAlgorithm
import no.ntnu.ihb.vico.util.ElementObserver
import no.ntnu.ihb.vico.util.StringArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder


fun interface SlaveInitCallback {
    fun invoke(slave: FmiSlave)
}

fun interface SlaveStepCallback {
    fun invoke(target: Pair<Double, SlaveComponent>)
}


class SlaveSystem @JvmOverloads constructor(
        private val algorithm: MasterAlgorithm = FixedStepMaster(),
        private var parameterSet: String? = null
) : SimulationSystem(Family.all(SlaveComponent::class.java).build()) {

    private val _slaves: MutableMap<Entity, FmiSlave> = mutableMapOf()
    val slaves: Collection<FmiSlave> = _slaves.values

    fun getSlave(name: String): FmiSlave = slaves.first { it.instanceName == name }

    override fun entityAdded(entity: Entity) {
        val c = entity.get<SlaveComponent>()
        val slave = FmiSlave(c.instantiate(), c)
        parameterSet?.also { parameterSet ->
            c.getParameterSet(parameterSet)?.also {
                LOG.info("Applying parameterSet named '$parameterSet' to ${entity.name}!")
                it.integerParameters.forEach { p -> slave.writeInteger(p.name, p.value) }
                it.realParameters.forEach { p -> slave.writeReal(p.name, p.value) }
                it.booleanParameters.forEach { p -> slave.writeBoolean(p.name, p.value) }
                it.stringParameters.forEach { p -> slave.writeString(p.name, p.value) }
            }
        }

        _slaves[entity] = slave
        algorithm.slaveAdded(slave)
    }

    override fun entityRemoved(entity: Entity) {
        val slave = _slaves.remove(entity)!!
        algorithm.slaveRemoved(slave)
        slave.close()
    }

    override fun init(engine: Engine) {
        algorithm.init(engine, slaves) { slave ->
            engine.updateConnection(slave.component)
        }
    }

    override fun step(currentTime: Double, stepSize: Double) {
        algorithm.step(currentTime, stepSize) {
            dispatchEvent(Properties.PROPERTIES_CHANGED, it)
        }
    }

    override fun close() {
        slaves.forEach { slave ->
            slave.terminate()
        }
        slaves.parallelStream().forEach { slave ->
            slave.close()
        }
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SlaveSystem::class.java)
    }

}

class FmiSlave(
    private val slave: SlaveInstance,
    internal val component: SlaveComponent
) : SlaveInstance by slave {

    private var initialized = false

    private val _variablesMarkedForReading: MutableList<ScalarVariable> = mutableListOf()

    private val integerVariablesToFetch = mutableSetOf<ValueReference>()
    private val realVariablesToFetch = mutableSetOf<ValueReference>()
    private val booleanVariablesToFetch = mutableSetOf<ValueReference>()
    private val stringVariablesToFetch = mutableSetOf<ValueReference>()

    private lateinit var intVrs: LongArray
    private lateinit var realVrs: LongArray
    private lateinit var booleanVrs: LongArray
    private lateinit var stringVrs: LongArray

    private var intBuffer: IntArray? = null
    private var realBuffer: DoubleArray? = null
    private var booleanBuffer: BooleanArray? = null
    private var stringBuffer: StringArray? = null


    private var cachedSetVrBuf: ByteBuffer? = null
    private var cachedSetValueBuf: ByteBuffer? = null
    private var cachedGetVrBuf: ByteBuffer? = null
    private var cachedGetValueBuf: ByteBuffer? = null


    init {
        component.variablesMarkedForReading.apply {
            forEach { markForReading(it) }
            addObserver(object : ElementObserver<String> {
                override fun onElementAdded(element: String) {
                    markForReading(element)
                }

                override fun onElementRemoved(element: String) {}
            })
        }
    }

    override fun exitInitializationMode(): Boolean {
        return slave.exitInitializationMode().also {
            initialized = true
        }
    }

    override fun doStep(stepSize: Double): Boolean {
        return slave.doStep(stepSize).also { status ->
            if (status) {
                component.stepCount++
            }
        }
    }

    override fun reset(): Boolean {
        return slave.reset().also {
            component.clearCaches()
            initialized = false
        }
    }

    fun readIntegerDirect(name: String) = slave.readInteger(name)
    fun readRealDirect(name: String) = slave.readReal(name)
    fun readBooleanDirect(name: String) = slave.readBoolean(name)
    fun readStringDirect(name: String) = slave.readString(name)

    fun retrieveCachedGets() {
        if (integerVariablesToFetch.isNotEmpty()) {
            with(component.integerGetCache) {
                val values = getIntBuffer(integerVariablesToFetch.size)
                val refs = intVrs
                slave.readInteger(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
        }
        if (realVariablesToFetch.isNotEmpty()) {
            with(component.realGetCache) {
                if (slave is DirectAccessor) {
                    val (refs, values) = getRealDirectBuffer(realVrs)
                    slave.readRealDirect(refs, values)
                    val bd = values.asDoubleBuffer()
                    for (i in realVrs.indices) {
                        set(realVrs[i], bd.get(i))
                    }
                } else {
                    val values = getRealBuffer(realVariablesToFetch.size)
                    val refs = realVrs
                    slave.readReal(refs, values)
                    for (i in refs.indices) {
                        set(refs[i], values[i])
                    }
                }

            }
        }
        if (booleanVariablesToFetch.isNotEmpty()) {
            with(component.booleanGetCache) {
                val values = getBooleanBuffer(booleanVariablesToFetch.size)
                val refs = booleanVrs
                slave.readBoolean(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
        }
        if (stringVariablesToFetch.isNotEmpty()) {
            with(component.stringGetCache) {
                val values = getStringBuffer(stringVariablesToFetch.size)
                val refs = stringVrs
                slave.readString(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
        }
    }

    fun transferCachedSets(clear: Boolean = true) {
        with(component.integerSetCache) {
            if (!isEmpty()) {
                slave.writeInteger(keys.toLongArray(), values.toIntArray())
                if (clear) clear()
            }
        }
        with(component.realSetCache) {
            if (!isEmpty()) {
                val keyArray = keys.toLongArray()
                val valueArray = values.toDoubleArray()
                if (slave is DirectAccessor) {

                    val vrBuf = if (cachedSetVrBuf?.capacity() == Long.SIZE_BYTES * keyArray.size) {
                        cachedSetVrBuf!!
                    } else {
                        ByteBuffer.allocateDirect(Long.SIZE_BYTES * keyArray.size).apply {
                            order(ByteOrder.nativeOrder())
                        }.also {
                            cachedSetVrBuf = it
                        }
                    }
                    vrBuf!!.asLongBuffer().apply {
                        clear()
                        put(keyArray)
                    }

                    val valueBuf = if (cachedSetValueBuf?.capacity() == Double.SIZE_BYTES * keyArray.size) {
                        cachedSetValueBuf!!
                    } else {
                        ByteBuffer.allocateDirect(Double.SIZE_BYTES * valueArray.size).apply {
                            order(ByteOrder.nativeOrder())
                        }.also {
                            cachedSetValueBuf = it
                        }
                    }
                    cachedSetValueBuf!!.asDoubleBuffer().apply {
                        clear()
                        put(valueArray)
                    }

                    slave.writeRealDirect(vrBuf, valueBuf)
                } else {
                    slave.writeReal(keyArray, valueArray)
                }

                if (clear) clear()
            }
        }
        with(component.booleanSetCache) {
            if (!isEmpty()) {
                slave.writeBoolean(keys.toLongArray(), values.toBooleanArray())
                if (clear) clear()
            }
        }
        with(component.stringSetCache) {
            if (!isEmpty()) {
                slave.writeString(keys.toLongArray(), values.toTypedArray())
                if (clear) clear()
            }
        }
    }

    private fun markForReading(variableName: String) {

        if (variableName in _variablesMarkedForReading.map { it.name }) return

        val v: ScalarVariable = modelDescription.modelVariables.getByName(variableName)
        val added = when (v.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> integerVariablesToFetch.add(v.valueReference).also {
                if (it) intVrs = integerVariablesToFetch.toLongArray()
            }
            VariableType.REAL -> realVariablesToFetch.add(v.valueReference).also {
                if (it) realVrs = realVariablesToFetch.toLongArray()
            }
            VariableType.BOOLEAN -> booleanVariablesToFetch.add(v.valueReference).also {
                if (it) booleanVrs = booleanVariablesToFetch.toLongArray()
            }
            VariableType.STRING -> stringVariablesToFetch.add(v.valueReference).also {
                if (it) stringVrs = stringVariablesToFetch.toLongArray()
            }
        }
        if (added && initialized) {
            when (v.type) {
                VariableType.INTEGER, VariableType.ENUMERATION -> readIntegerDirect(v.name).value.also {
                    component.integerGetCache[v.valueReference] = it
                }
                VariableType.REAL -> readRealDirect(v.name).value.also {
                    component.realGetCache[v.valueReference] = it
                }
                VariableType.BOOLEAN -> readBooleanDirect(v.name).value.also {
                    component.booleanGetCache[v.valueReference] = it
                }
                VariableType.STRING -> readStringDirect(v.name).value.also {
                    component.stringGetCache[v.valueReference] = it
                }
            }
            _variablesMarkedForReading.add(v)
            LOG.debug("${v.type} variable '${instanceName}.${v.name}' marked for reading.")
        }

    }

    private fun getIntBuffer(size: Int): IntArray {
        if (intBuffer?.size != size) {
            intBuffer = IntArray(size)
        }
        return intBuffer!!
    }

    private fun getRealBuffer(size: Int): DoubleArray {
        if (realBuffer?.size != size) {
            realBuffer = DoubleArray(size)
        }
        return realBuffer!!
    }

    private fun getRealDirectBuffer(vrs: ValueReferences): Pair<ByteBuffer, ByteBuffer> {
        val size = vrs.size

        if (cachedGetValueBuf?.capacity() != size * Double.SIZE_BYTES) {
            cachedGetValueBuf = ByteBuffer.allocateDirect(size * Double.SIZE_BYTES).apply {
                order(ByteOrder.nativeOrder())
            }
        }
        if (cachedGetVrBuf?.capacity() != size * Long.SIZE_BYTES) {
            cachedGetVrBuf = ByteBuffer.allocateDirect(size * Long.SIZE_BYTES).apply {
                order(ByteOrder.nativeOrder())
            }
        }
        cachedGetVrBuf!!.apply {
            clear()
            asLongBuffer().put(vrs)
        }
        return cachedGetVrBuf!! to cachedGetValueBuf!!
    }

    private fun getBooleanBuffer(size: Int): BooleanArray {
        if (booleanBuffer?.size != size) {
            booleanBuffer = BooleanArray(size)
        }
        return booleanBuffer!!
    }

    private fun getStringBuffer(size: Int): StringArray {
        if (stringBuffer?.size != size) {
            stringBuffer = StringArray(size)
        }
        return stringBuffer!!
    }


    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(FmiSlave::class.java)
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
