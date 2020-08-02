package no.ntnu.ihb.vico.libcosim

import com.opensimulationplatform.cosim.CosimExecution
import com.opensimulationplatform.cosim.CosimLastValueObserver
import com.opensimulationplatform.cosim.CosimOverrideManipulator
import com.opensimulationplatform.cosim.CosimSlave
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser
import no.ntnu.ihb.vico.core.*
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

        modelVariables.integers.forEach { v ->
            registerProperties(IntLambdaProperty(v.name, 1,
                getter = { it[0] = observer.getInteger(slave.slaveRef, v.valueReference)!! },
                setter = { manipulator.setInteger(slave.slaveRef, v.valueReference, it.first()) }
            ))
        }
        modelVariables.reals.forEach { v ->
            registerProperties(RealLambdaProperty(v.name, 1,
                getter = { it[0] = observer.getReal(slave.slaveRef, v.valueReference)!! },
                setter = { manipulator.setReal(slave.slaveRef, v.valueReference, it.first()) }
            ))
        }
        modelVariables.strings.forEach { v ->
            registerProperties(StrLambdaProperty(v.name, 1,
                getter = { it[0] = observer.getString(slave.slaveRef, v.valueReference)!! },
                setter = { manipulator.setString(slave.slaveRef, v.valueReference, it.first()) }
            ))
        }
        modelVariables.booleans.forEach { v ->
            registerProperties(BoolLambdaProperty(v.name, 1,
                getter = { it[0] = observer.getBoolean(slave.slaveRef, v.valueReference)!! },
                setter = { manipulator.setBoolean(slave.slaveRef, v.valueReference, it.first()) }
            ))
        }
    }

    fun apply(execution: CosimExecution, observer: CosimLastValueObserver, manipulator: CosimOverrideManipulator) {
        this.slave = execution.addSlave(source, instanceName)
        this.observer = observer
        this.manipulator = manipulator
    }

}
