package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.acco.core.RealModifier
import no.ntnu.ihb.fmi4j.modeldescription.variables.*

sealed class Connection<E : ScalarVariable>(
    val source: Component,
    val sourceVariable: E,
    val target: Component,
    var targetVariable: E
)

class IntegerConnection(
    source: Component,
    sourceVariable: IntegerVariable,
    target: Component,
    targetVariable: IntegerVariable
) : Connection<IntegerVariable>(source, sourceVariable, target, targetVariable)

class RealConnection(
    source: Component,
    sourceVariable: RealVariable,
    target: Component,
    targetVariable: RealVariable
) : Connection<RealVariable>(source, sourceVariable, target, targetVariable) {

    var modifier: RealModifier? = null

}

class StringConnection(
    source: Component,
    sourceVariable: StringVariable,
    target: Component,
    targetVariable: StringVariable
) : Connection<StringVariable>(source, sourceVariable, target, targetVariable)

class BooleanConnection(
    source: Component,
    sourceVariable: BooleanVariable,
    target: Component,
    targetVariable: BooleanVariable
) : Connection<BooleanVariable>(source, sourceVariable, target, targetVariable)
