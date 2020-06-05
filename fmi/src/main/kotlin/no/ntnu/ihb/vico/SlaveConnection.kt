package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.core.RealModifier
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

sealed class SlaveConnection<E : ScalarVariable>(
    val sourceSlave: SlaveComponent,
    val sourceVariable: E,
    val targetSlave: SlaveComponent,
    val targetVariable: E
) {

    protected val vr = LongArray(1)

    abstract fun transferData()

}

class IntegerConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: IntegerVariable,
    targetSlave: SlaveComponent,
    targetVariable: IntegerVariable
) : SlaveConnection<IntegerVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = IntArray(1)

    override fun transferData() {
        sourceSlave.readInteger(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeInteger(vr.also { it[0] = targetVariable.valueReference }, values)
    }

}

class RealConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: RealVariable,
    targetSlave: SlaveComponent,
    targetVariable: RealVariable
) : SlaveConnection<RealVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = RealArray(1)
    private val modifiers = mutableListOf<RealModifier>()

    fun addModifier(modifier: RealModifier) = apply {
        modifiers.add(modifier)
    }

    override fun transferData() {
        sourceSlave.readReal(vr.also { it[0] = sourceVariable.valueReference }, values)
        modifiers.forEach { m ->
            values[0] = m.apply(values[0])
        }
        targetSlave.writeReal(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class BooleanConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: BooleanVariable,
    targetSlave: SlaveComponent,
    targetVariable: BooleanVariable
) : SlaveConnection<BooleanVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = BooleanArray(1)

    override fun transferData() {
        sourceSlave.readBoolean(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeBoolean(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class StringConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: StringVariable,
    targetSlave: SlaveComponent,
    targetVariable: StringVariable
) : SlaveConnection<StringVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = StringArray(1) { "" }

    override fun transferData() {
        sourceSlave.readString(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.writeString(vr.also { it[0] = targetVariable.valueReference }, values)
    }

}
