package no.ntnu.ihb.vico.structure

import no.ntnu.ihb.fmi4j.modeldescription.variables.ScalarVariable

class Connection(
    val source: Component,
    val sourceVariable: ScalarVariable,
    val target: Component,
    var targetVariable: ScalarVariable
)
