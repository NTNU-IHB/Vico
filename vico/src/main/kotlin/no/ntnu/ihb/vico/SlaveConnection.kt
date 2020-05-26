package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

typealias RealModifier = (Double) -> Double

class ComponentConnection(
    val sourceSlave: SlaveComponent,
    val sourceVariable: ScalarVariable,
    val targetSlave: SlaveComponent,
    val targetVariable: ScalarVariable
) {

    init {
        require(sourceVariable.type == targetVariable.type)
    }

}

sealed class Connection<E : ScalarVariable>(
    val sourceSlave: SlaveWrapper,
    val sourceVariable: E,
    val targetSlave: SlaveWrapper,
    val targetVariable: E
) {

    protected val vr = LongArray(1)

    abstract fun transferData()

}

class IntegerConnection(
    sourceSlave: SlaveWrapper,
    sourceVariable: IntegerVariable,
    targetSlave: SlaveWrapper,
    targetVariable: IntegerVariable
) : Connection<IntegerVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = IntArray(1)

    override fun transferData() {
        sourceSlave.readInteger(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeInteger(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class RealConnection(
    sourceSlave: SlaveWrapper,
    sourceVariable: RealVariable,
    targetSlave: SlaveWrapper,
    targetVariable: RealVariable
) : Connection<RealVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = RealArray(1)
    private val modifiers = mutableListOf<RealModifier>()

    fun addModifier(modifier: RealModifier) = modifiers.add(modifier)

    override fun transferData() {
        sourceSlave.readReal(vr.also { it[0] = sourceVariable.valueReference }, values)
        modifiers.forEach { m ->
            values[0] = m.invoke(values[0])
        }
        targetSlave.writeReal(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class BooleanConnection(
    sourceSlave: SlaveWrapper,
    sourceVariable: BooleanVariable,
    targetSlave: SlaveWrapper,
    targetVariable: BooleanVariable
) : Connection<BooleanVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = BooleanArray(1)

    override fun transferData() {
        sourceSlave.readBoolean(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeBoolean(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class StringConnection(
    sourceSlave: SlaveWrapper,
    sourceVariable: StringVariable,
    targetSlave: SlaveWrapper,
    targetVariable: StringVariable
) : Connection<StringVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = StringArray(1) { "" }

    override fun transferData() {
        sourceSlave.readString(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeString(vr.also { it[0] = targetVariable.valueReference }, values)
    }

}
