package no.ntnu.ihb.vico

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.ntnu.ihb.acco.core.*
import no.ntnu.ihb.fmi4j.*
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReference
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.modeldescription.variables.Causality
import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable
import no.ntnu.ihb.fmi4j.modeldescription.variables.VariableType
import no.ntnu.ihb.vico.structure.Parameter
import no.ntnu.ihb.vico.structure.ParameterSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private typealias Cache<E> = HashMap<ValueReference, E>

class SlaveComponent(
    private val slave: SlaveInstance,
    val stepSizeHint: Double? = null
) : SlaveInstance by slave, Component() {

    private var initialized = false
    var stepCount = 0L
        private set

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

    private val overriddenIntegers = mutableMapOf<ValueReference, Int>()
    private val overriddenReals = mutableMapOf<ValueReference, Double>()
    private val overriddenBooleans = mutableMapOf<ValueReference, Boolean>()
    private val overriddenStrings = mutableMapOf<ValueReference, String>()

    private val parameterSets = mutableSetOf<ParameterSet>()

    init {

        modelVariables.integers.forEach { int ->
            val vr = longArrayOf(int.valueReference)
            registerVariable(int.name, IntLambdaVar(1,
                getter = { slave.readInteger(vr, it) },
                setter = { slave.writeInteger(vr, it) }
            ))
        }
        modelVariables.reals.forEach { real ->
            val vr = longArrayOf(real.valueReference)
            registerVariable(real.name, RealLambdaVar(1,
                getter = { slave.readReal(vr, it) },
                setter = { slave.writeReal(vr, it) }
            ))
        }
        modelVariables.strings.forEach { real ->
            val vr = longArrayOf(real.valueReference)
            registerVariable(real.name, StrLambdaVar(1,
                getter = { slave.readString(vr, it) },
                setter = { slave.writeString(vr, it) }
            ))
        }
        modelVariables.booleans.forEach { real ->
            val vr = longArrayOf(real.valueReference)
            registerVariable(real.name, BoolLambdaVar(1,
                getter = { slave.readBoolean(vr, it) },
                setter = { slave.writeBoolean(vr, it) }
            ))
        }

    }

    fun getParameterSet(name: String): ParameterSet? {
        return parameterSets.firstOrNull { it.name == name }
    }

    override fun exitInitializationMode(): Boolean {
        return slave.exitInitializationMode().also {
            initialized = true
        }
    }

    override fun doStep(stepSize: Double): Boolean {
        return slave.doStep(stepSize).also { status ->
            if (status) stepCount++
        }
    }

    override fun reset(): Boolean {
        return slave.reset().also {
            clearCaches()
            initialized = false
        }
    }

    fun readIntegerDirect(name: String) = slave.readInteger(name)
    fun readRealDirect(name: String) = slave.readReal(name)
    fun readBooleanDirect(name: String) = slave.readBoolean(name)
    fun readStringDirect(name: String) = slave.readString(name)

    override fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            integerGetCache.also { cache ->
                check(vr[i], VariableType.INTEGER, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun readReal(vr: ValueReferences, ref: RealArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            realGetCache.also { cache ->
                check(vr[i], VariableType.REAL, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            stringGetCache.also { cache ->
                check(vr[i], VariableType.STRING, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        require(vr.size == ref.size)
        for (i in vr.indices) {
            booleanGetCache.also { cache ->
                check(vr[i], VariableType.BOOLEAN, cache)
                ref[i] = cache[vr[i]]!!
            }
        }
        return FmiStatus.OK
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            integerSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            realSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        require(vr.size == value.size)
        for (i in vr.indices) {
            stringSetCache[vr[i]] = value[i]
        }
        return FmiStatus.OK
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
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
                slave.readInteger(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            overriddenIntegers.forEach { (valueRef, value) ->
                integerGetCache[valueRef] = value
            }
        }
        if (realVariablesToFetch.isNotEmpty()) {
            with(realGetCache) {
                val values = DoubleArray(realVariablesToFetch.size)
                val refs = realVariablesToFetch.toLongArray()
                slave.readReal(refs, values)
                for (i in refs.indices) {
                    val vr = refs[i]
                    val value = values[i]
                    /*realModifiers[vr]?.forEach {
                        value = it.apply(value)
                    }*/
                    set(vr, value)
                }
            }
            overriddenReals.forEach { (valueRef, value) ->
                realGetCache[valueRef] = value
            }
        }
        if (booleanVariablesToFetch.isNotEmpty()) {
            with(booleanGetCache) {
                val values = BooleanArray(booleanVariablesToFetch.size)
                val refs = booleanVariablesToFetch.toLongArray()
                slave.readBoolean(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            overriddenBooleans.forEach { (valueRef, value) ->
                booleanGetCache[valueRef] = value
            }
        }
        if (stringVariablesToFetch.isNotEmpty()) {
            with(stringGetCache) {
                val values = StringArray(stringVariablesToFetch.size) { "" }
                val refs = stringVariablesToFetch.toLongArray()
                slave.readString(refs, values)
                for (i in refs.indices) {
                    set(refs[i], values[i])
                }
            }
            overriddenStrings.forEach { (valueRef, value) ->
                stringGetCache[valueRef] = value
            }
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
                slave.writeInteger(keys.toLongArray(), values.toIntArray())
                if (clear) clear()
            }
        }
        with(realSetCache) {
            if (!isEmpty()) {
                slave.writeReal(keys.toLongArray(), values.toDoubleArray())
                if (clear) clear()
            }
        }
        with(booleanSetCache) {
            if (!isEmpty()) {
                slave.writeBoolean(keys.toLongArray(), values.toBooleanArray())
                if (clear) clear()
            }
        }
        with(stringSetCache) {
            if (!isEmpty()) {
                slave.writeString(keys.toLongArray(), values.toTypedArray())
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
            "Variable with valueReference=$ref, type=$type and possible variable names=${v.map { it.name }} " +
                    "has not been marked for reading for slave named '${slave.instanceName}'!"
        }
    }

    fun getVariablesMarkedForReading(): List<ScalarVariable> {
        val modelVariables = modelDescription.modelVariables
        return integerVariablesToFetch.mapNotNull {
            modelVariables.getByValueReference(it, VariableType.INTEGER).firstOrNull()
        } + realVariablesToFetch.mapNotNull {
            modelVariables.getByValueReference(it, VariableType.REAL).firstOrNull()
        } + booleanVariablesToFetch.mapNotNull {
            modelVariables.getByValueReference(it, VariableType.BOOLEAN).firstOrNull()
        } + stringVariablesToFetch.mapNotNull {
            modelVariables.getByValueReference(it, VariableType.STRING).firstOrNull()
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
                LOG.trace("Variable '${instanceName}.${v.name}' marked for reading.")
            }
        }
    }

    private fun markedForReadingPostInitAction(v: ScalarVariable) {
        when (v.type) {
            VariableType.INTEGER, VariableType.ENUMERATION -> integerGetCache[v.valueReference] =
                IntArray(1).also { readInteger(longArrayOf(v.valueReference), it) }[0]
            VariableType.REAL -> realGetCache[v.valueReference] =
                DoubleArray(1).also { readReal(longArrayOf(v.valueReference), it) }[0]
            VariableType.BOOLEAN -> booleanGetCache[v.valueReference] =
                BooleanArray(1).also { readBoolean(longArrayOf(v.valueReference), it) }[0]
            VariableType.STRING -> stringGetCache[v.valueReference] =
                StringArray(1) { "" }.also { readString(longArrayOf(v.valueReference), it) }[0]
        }
    }

    private fun checkCausalityForOverride(vr: ValueReference, type: VariableType) {
        val causality = modelDescription.modelVariables.getByValueReference(vr, type).first().causality
        check(causality == Causality.CALCULATED_PARAMETER || causality == Causality.OUTPUT)
        { "Override can only be performed on variables with causality OUTPUT or CALCULATED_PARAMETER" }
    }

    fun overrideInteger(vr: ValueReference, value: Int) {
        checkCausalityForOverride(vr, VariableType.INTEGER)
        overriddenIntegers[vr] = value
    }

    fun stopOverrideInteger(vr: ValueReference) {
        overriddenIntegers.remove(vr)
    }

    fun overrideReal(vr: ValueReference, value: Double) {
        checkCausalityForOverride(vr, VariableType.REAL)
        overriddenReals[vr] = value
    }

    fun stopOverrideReal(vr: ValueReference) {
        overriddenReals.remove(vr)
    }

    fun overrideBoolean(vr: ValueReference, value: Boolean) {
        checkCausalityForOverride(vr, VariableType.BOOLEAN)
        overriddenBooleans[vr] = value
    }

    fun stopOverrideBoolean(vr: ValueReference) {
        overriddenBooleans.remove(vr)
    }

    fun overrideString(vr: ValueReference, value: String) {
        checkCausalityForOverride(vr, VariableType.STRING)
        overriddenStrings[vr] = value
    }

    fun stopOverrideString(vr: ValueReference) {
        overriddenStrings.remove(vr)
    }

    fun addParameterSet(name: String, parameters: List<Parameter<*>>) = apply {
        addParameterSet(ParameterSet(name, parameters))
    }

    fun addParameterSet(parameterSet: ParameterSet) = apply {
        check(parameterSets.add(parameterSet))
    }

    override fun toString(): String {
        return "SlaveComponent(instanceName=$instanceName)"
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SlaveComponent::class.java)
    }

}
