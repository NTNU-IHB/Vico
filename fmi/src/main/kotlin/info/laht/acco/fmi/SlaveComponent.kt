package info.laht.acco.fmi

import info.laht.acco.core.Component
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.VariableType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private typealias Cache<E> = HashMap<ValueReference, E>

class SlaveComponent(
    private val slave: SlaveInstance,
    val decimationFactor: Int = 1
) : SlaveInstance by slave, Component, Comparable<SlaveComponent> {

    private var initialized = false

    private val integerSetCache by lazy { Cache<Int>() }
    private val realSetCache by lazy { Cache<Double>() }
    private val booleanSetCache by lazy { Cache<Boolean>() }
    private val stringSetCache by lazy { Cache<String>() }

    private val integerGetCache by lazy { Cache<Int>() }
    private val realGetCache by lazy { Cache<Double>() }
    private val booleanGetCache by lazy { Cache<Boolean>() }
    private val stringGetCache by lazy { Cache<String>() }

    private val integerVariablesToFetch = mutableSetOf<ValueReference>()
    private val realVariablesToFetch = mutableSetOf<ValueReference>()
    private val booleanVariablesToFetch = mutableSetOf<ValueReference>()
    private val stringVariablesToFetch = mutableSetOf<ValueReference>()

    init {
        require(decimationFactor > 0)
    }

    override fun exitInitializationMode(): Boolean {
        return slave.exitInitializationMode().also {
            initialized = true
        }
    }

    override fun reset(): Boolean {
        return slave.reset().also {
            clearCaches()
            initialized = false
        }
    }

    override fun read(vr: ValueReferences, ref: IntArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            integerGetCache.also { cache ->
                check(vr[i], VariableType.INTEGER, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun read(vr: ValueReferences, ref: RealArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            realGetCache.also { cache ->
                check(vr[i], VariableType.REAL, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun read(vr: ValueReferences, ref: StringArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            stringGetCache.also { cache ->
                check(vr[i], VariableType.STRING, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun read(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            booleanGetCache.also { cache ->
                check(vr[i], VariableType.BOOLEAN, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun write(vr: ValueReferences, value: IntArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            integerSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun write(vr: ValueReferences, value: RealArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            realSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun write(vr: ValueReferences, value: StringArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            stringSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun write(vr: ValueReferences, value: BooleanArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            booleanSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    fun retrieveCachedGets() {
        if (integerVariablesToFetch.isNotEmpty()) {
            with(integerGetCache) {
                val values = IntArray(integerVariablesToFetch.size)
                val refs = integerVariablesToFetch.toLongArray()
                slave.read(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            /*overriddenIntegers.forEach { (valueRef, value) ->
                integerGetCache[valueRef] = value
            }*/
        }
        if (realVariablesToFetch.isNotEmpty()) {
            with(realGetCache) {
                val values = DoubleArray(realVariablesToFetch.size)
                val refs = realVariablesToFetch.toLongArray()
                slave.read(refs, values)
                for (i in refs.indices) {
                    val vr = refs[i]
                    val value = values[i]
                    /*realModifiers[vr]?.forEach {
                        value = it.apply(value)
                    }*/
                    set(vr, value)
                }
            }
            /*overriddenReals.forEach { (valueRef, value) ->
                realGetCache[valueRef] = value
            }*/
        }
        if (booleanVariablesToFetch.isNotEmpty()) {
            with(booleanGetCache) {
                val values = BooleanArray(booleanVariablesToFetch.size)
                val refs = booleanVariablesToFetch.toLongArray()
                slave.read(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            /*overriddenBooleans.forEach { (valueRef, value) ->
                booleanGetCache[valueRef] = value
            }*/
        }
        if (stringVariablesToFetch.isNotEmpty()) {
            with(stringGetCache) {
                val values = StringArray(stringVariablesToFetch.size) { "" }
                val refs = stringVariablesToFetch.toLongArray()
                slave.read(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            /*overriddenStrings.forEach { (valueRef, value) ->
                stringGetCache[valueRef] = value
            }*/
        }

    }

    suspend fun asyncRetrieveCachedGets() {
        withContext(Dispatchers.Default) {
            retrieveCachedGets()
        }
    }

    fun transferCachedSets(clear: Boolean = true) {
        with(integerSetCache) {
            if (!isEmpty()) {
                slave.write(keys.toLongArray(), values.toIntArray())
                if (clear) clear()
            }
        }
        with(realSetCache) {
            if (!isEmpty()) {
                slave.write(keys.toLongArray(), values.toDoubleArray())
                if (clear) clear()
            }
        }
        with(booleanSetCache) {
            if (!isEmpty()) {
                slave.write(keys.toLongArray(), values.toBooleanArray())
                if (clear) clear()
            }
        }
        with(stringSetCache) {
            if (!isEmpty()) {
                slave.write(keys.toLongArray(), values.toTypedArray())
                if (clear) clear()
            }
        }
    }

    suspend fun asyncTransferCachedSets() {
        withContext(Dispatchers.Default) {
            transferCachedSets()
        }
    }

    private fun clearCaches() {
        listOf(realGetCache, integerGetCache, booleanGetCache, stringGetCache).forEach { it.clear() }
        listOf(realSetCache, integerSetCache, booleanSetCache, stringSetCache).forEach { it.clear() }
    }

    private fun check(ref: ValueReference, type: VariableType, cache: MutableMap<*, *>) {
        check(ref in cache) {
            val v = modelDescription.modelVariables.getByValueReference(ref, type)
            "Variable with valueReference=$ref " +
                    "has not been marked for reading for slave named '${slave.instanceName}'!"
        }
    }

    fun getVariablesMarkedForReading(): List<ScalarVariable> {
        val modelVariables = modelDescription.modelVariables
        return integerVariablesToFetch.map {
            modelVariables.getByValueReference(it, VariableType.INTEGER).first()
        } + realVariablesToFetch.map {
            modelVariables.getByValueReference(it, VariableType.REAL).first()
        } + booleanVariablesToFetch.map {
            modelVariables.getByValueReference(it, VariableType.BOOLEAN).first()
        } + stringVariablesToFetch.map {
            modelVariables.getByValueReference(it, VariableType.STRING).first()
        }
    }

    fun markForReading(variableName: String) {
        if (variableName.contains("*")) {
            val partialName = variableName.substring(0, variableName.indexOf("*"))

            modelDescription.modelVariables.forEach {
                if (it.name.startsWith(partialName)) {
                    markForReading(it.name)
                }
            }

        } else {
            val v = modelDescription.modelVariables.getByName(variableName)
            val added = when (v.type) {
                VariableType.INTEGER, VariableType.ENUMERATION -> integerVariablesToFetch.add(v.valueReference)
                VariableType.REAL -> realVariablesToFetch.add(v.valueReference)
                VariableType.BOOLEAN -> booleanVariablesToFetch.add(v.valueReference)
                VariableType.STRING -> stringVariablesToFetch.add(v.valueReference)
            }
            if (added) {
                if (initialized) {
                    markedForReadingPostInitAction(v)
                }
                LOG.debug("Variable '${instanceName}.${v.name}' marked for reading.")
            }
        }
    }

    private fun markedForReadingPostInitAction(v: ScalarVariable) {
        when (v.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> integerGetCache[v.valueReference] =
                IntArray(1).also { read(longArrayOf(v.valueReference), it) }[0]
            VariableType.REAL -> realGetCache[v.valueReference] =
                DoubleArray(1).also { read(longArrayOf(v.valueReference), it) }[0]
            VariableType.BOOLEAN -> booleanGetCache[v.valueReference] =
                BooleanArray(1).also { read(longArrayOf(v.valueReference), it) }[0]
            VariableType.STRING -> stringGetCache[v.valueReference] =
                StringArray(1) { "" }.also { read(longArrayOf(v.valueReference), it) }[0]
        }
    }

    override fun compareTo(other: SlaveComponent): Int {
        return other.decimationFactor.compareTo(decimationFactor)
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SlaveComponent::class.java)
    }

}

