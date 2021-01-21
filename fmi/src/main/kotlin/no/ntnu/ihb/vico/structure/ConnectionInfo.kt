package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.variables.*
import no.ntnu.ihb.vico.core.RealModifier

sealed class ConnectionInfo<E : ScalarVariable>(
    val source: Component,
    val sourceVariable: E,
    val target: Component,
    var targetVariable: E
)

class IntegerConnectionInfo(
    source: Component,
    sourceVariable: IntegerVariable,
    target: Component,
    targetVariable: IntegerVariable
) : ConnectionInfo<IntegerVariable>(source, sourceVariable, target, targetVariable)

class RealConnectionInfo(
    source: Component,
    sourceVariable: RealVariable,
    target: Component,
    targetVariable: RealVariable,
    val modifier: RealModifier? = null
) : ConnectionInfo<RealVariable>(source, sourceVariable, target, targetVariable)

class StringConnectionInfo(
    source: Component,
    sourceVariable: StringVariable,
    target: Component,
    targetVariable: StringVariable
) : ConnectionInfo<StringVariable>(source, sourceVariable, target, targetVariable)

class BooleanConnectionInfo(
    source: Component,
    sourceVariable: BooleanVariable,
    target: Component,
    targetVariable: BooleanVariable
) : ConnectionInfo<BooleanVariable>(source, sourceVariable, target, targetVariable)
