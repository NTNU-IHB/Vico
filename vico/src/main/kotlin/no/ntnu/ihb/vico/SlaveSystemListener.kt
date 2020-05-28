package no.ntnu.ihb.vico

import java.io.Closeable

interface SlaveSystemListener : Closeable {

    fun slaveAdded(slave: SlaveComponent)

    fun slaveRemoved(slave: SlaveComponent)

    fun postInit(currentTime: Double)

    fun postSlaveStep(currentTime: Double, slave: SlaveComponent)

    fun postStep(currentTime: Double)

    fun postTerminate()

}

abstract class SlaveSystemAdapter : SlaveSystemListener {

    override fun slaveAdded(slave: SlaveComponent) {}

    override fun slaveRemoved(slave: SlaveComponent) {}

    override fun postInit(currentTime: Double) {}

    override fun postSlaveStep(currentTime: Double, slave: SlaveComponent) {}

    override fun postStep(currentTime: Double) {}

    override fun postTerminate() {}

}
