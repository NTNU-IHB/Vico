package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.HeadlessEngineRunner
import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.slaveSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


internal class TestSSPLoader {

    @Test
    fun testControlledDriveTrain() {
        val structure = SSPLoader(TestSsp.get("ControlledDriveTrain.ssp")).load()
        val stopTime = structure.defaultExperiment?.stopTime ?: 0.0
        Assertions.assertEquals(4.0, stopTime, 1e-6)
        Engine(1e-3).use { engine ->
            structure.apply(engine)
            HeadlessEngineRunner(engine).apply {
                runFor(stopTime).get()
            }

        }
    }

    @Test
    fun testExternalSSV() {
        val structure = SSPLoader(TestSsp.get("bouncingBall/external_ssv")).load()
        val stopTime = structure.defaultExperiment?.stopTime ?: 0.0
        Assertions.assertEquals(10.0, stopTime, 1e-6)
        Engine(1e-3).use { engine ->

            structure.apply(engine)
            engine.init()

            val bb = engine.slaveSystem.getSlave("bouncingBall")
            val h = bb.readRealDirect("h").value
            Assertions.assertEquals(5.0, h, 1e-6)

        }
    }

}
