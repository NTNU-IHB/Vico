package no.ntnu.ihb.vico.libcosim

import com.opensimulationplatform.cosim.CosimExecution
import com.opensimulationplatform.cosim.CosimLastValueObserver
import com.opensimulationplatform.cosim.CosimOverrideManipulator
import com.opensimulationplatform.cosim.CosimSlave
import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.RealLambdaProperty
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import java.io.File

class CosimFmuComponent(
    val source: File,
    val instanceName: String
) : Component() {

    lateinit var slave: CosimSlave
    lateinit var observer: CosimLastValueObserver
    lateinit var manipulator: CosimOverrideManipulator

    val modelDescription by lazy {
        ModelDescriptionParser.parseModelDescription(source)
    }

    init {
        val modelVariables = modelDescription.modelVariables

        modelVariables.reals.forEach { v ->
            registerProperties(RealLambdaProperty(v.name, 1,
                getter = { it[0] = observer.getReal(slave.slaveRef, v.valueReference)!! },
                setter = { manipulator.setReal(slave.slaveRef, v.valueReference, it.first()) }
            ))
        }
    }

    fun apply(execution: CosimExecution, observer: CosimLastValueObserver, manipulator: CosimOverrideManipulator) {
        this.slave = execution.addSlave(source, instanceName)
        this.observer = observer
        this.manipulator = manipulator
    }

}
