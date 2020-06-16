package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Component
import org.osp.cosim.CosimExecution
import org.osp.cosim.CosimSlave
import java.io.File

class CosimFmuComponent(
    val source: File,
    val instanceName: String
) : Component {

    lateinit var slave: CosimSlave

    fun apply(execution: CosimExecution) {
        execution.addSlave(source, instanceName)
    }

}
