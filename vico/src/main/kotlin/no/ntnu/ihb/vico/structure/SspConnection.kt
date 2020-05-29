package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.vico.*

sealed class SspConnection<E : ScalarVariable>(
    val source: Component,
    val sourceVariable: E,
    val target: Component,
    var targetVariable: E
) {

    abstract fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<E>

}

class SspIntegerConnection(
    source: Component,
    sourceVariable: IntegerVariable,
    target: Component,
    targetVariable: IntegerVariable
) : SspConnection<IntegerVariable>(source, sourceVariable, target, targetVariable) {

    override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<IntegerVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return IntegerConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }

}

class SspRealConnection(
    source: Component,
    sourceVariable: RealVariable,
    target: Component,
    targetVariable: RealVariable
) : SspConnection<RealVariable>(source, sourceVariable, target, targetVariable) {

    override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<RealVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return RealConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }

}

class SspStringConnection(
    source: Component,
    sourceVariable: StringVariable,
    target: Component,
    targetVariable: StringVariable
) : SspConnection<StringVariable>(source, sourceVariable, target, targetVariable) {

    override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<StringVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return StringConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }

}

class SspBooleanConnection(
    source: Component,
    sourceVariable: BooleanVariable,
    target: Component,
    targetVariable: BooleanVariable
) : SspConnection<BooleanVariable>(source, sourceVariable, target, targetVariable) {

    override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<BooleanVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return BooleanConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }

}
