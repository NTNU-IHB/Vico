package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.RealModifier
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

sealed class Connection<E : ScalarVariable>(
    val source: Component,
    val sourceVariable: E,
    val target: Component,
    var targetVariable: E
) {

    //abstract fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<*>

}

class IntegerConnection(
    source: Component,
    sourceVariable: IntegerVariable,
    target: Component,
    targetVariable: IntegerVariable
) : Connection<IntegerVariable>(source, sourceVariable, target, targetVariable) {

    /*override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<*> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return IntegerConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }*/

}

class RealConnection(
    source: Component,
    sourceVariable: RealVariable,
    target: Component,
    targetVariable: RealVariable
) : Connection<RealVariable>(source, sourceVariable, target, targetVariable) {

    val modifiers = mutableListOf<RealModifier>()

    /* override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<RealVariable> {
         val sourceSlave = slaves.first { it.instanceName == source.instanceName }
         val targetSlave = slaves.first { it.instanceName == target.instanceName }
         return RealConnection(
             sourceSlave, sourceVariable,
             targetSlave, targetVariable
         ).apply {
             modifiers.forEach { addModifier(it) }
         }
     }*/

}

class StringConnection(
    source: Component,
    sourceVariable: StringVariable,
    target: Component,
    targetVariable: StringVariable
) : Connection<StringVariable>(source, sourceVariable, target, targetVariable) {

    /*override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<StringVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return StringConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }*/

}

class BooleanConnection(
    source: Component,
    sourceVariable: BooleanVariable,
    target: Component,
    targetVariable: BooleanVariable
) : Connection<BooleanVariable>(source, sourceVariable, target, targetVariable) {

    /*override fun toSlaveConnection(slaves: List<SlaveComponent>): SlaveConnection<BooleanVariable> {
        val sourceSlave = slaves.first { it.instanceName == source.instanceName }
        val targetSlave = slaves.first { it.instanceName == target.instanceName }
        return BooleanConnection(
            sourceSlave, sourceVariable,
            targetSlave, targetVariable
        )
    }*/

}
