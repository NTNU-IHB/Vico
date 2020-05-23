package info.laht.acco.fmi

import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

sealed class Connection<E : ScalarVariable>(
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
) : Connection<IntegerVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = IntArray(1)

    override fun transferData() {
        sourceSlave.read(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.write(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class RealConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: RealVariable,
    targetSlave: SlaveComponent,
    targetVariable: RealVariable
) : Connection<RealVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = RealArray(1)

    override fun transferData() {
        sourceSlave.read(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.write(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class BooleanConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: BooleanVariable,
    targetSlave: SlaveComponent,
    targetVariable: BooleanVariable
) : Connection<BooleanVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = RealArray(1)

    override fun transferData() {
        sourceSlave.read(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.write(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}

class StringConnection(
    sourceSlave: SlaveComponent,
    sourceVariable: StringVariable,
    targetSlave: SlaveComponent,
    targetVariable: StringVariable
) : Connection<StringVariable>(
    sourceSlave, sourceVariable, targetSlave, targetVariable
) {

    private val values = StringArray(1) { "" }

    override fun transferData() {
        sourceSlave.read(vr.also { it[0] = sourceVariable.valueReference }, values)
        targetSlave.write(vr.also { it[0] = targetVariable.valueReference }, values)
    }
}
