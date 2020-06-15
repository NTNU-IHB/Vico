package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.EngineRunner
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File


internal class TestSSPLoader {

    @Test
    fun testControlledDriveTrain() {

        val resultDir = File("build/results/ControlledDriveTrain").also {
            it.deleteRecursively()
        }

        val structure = SSPLoader(TestSsp.get("ControlledDriveTrain.ssp")).load()
        val stopTime = structure.defaultExperiment?.stopTime ?: 0.0
        Assertions.assertEquals(4.0, stopTime, 1e-6)
        Engine(1e-3).use { engine ->

            engine.addSystem(SlaveLoggerSystem(null, resultDir))
            structure.apply(engine)

            EngineRunner(engine).apply {
                runFor(stopTime).get()
            }

        }

        Assertions.assertEquals(3, resultDir.listFiles()?.size ?: 0)

    }

    @Test
    fun testExternalSSV() {
        val structure = SSPLoader(TestSsp.get("bouncingBall/external_ssv")).load()
        val stopTime = structure.defaultExperiment?.stopTime ?: 0.0
        Assertions.assertEquals(10.0, stopTime, 1e-6)
        Engine(1e-3).use { engine ->

            structure.apply(engine)
            engine.init()

            val bb = engine.getSystem<SlaveSystem>().getSlave("bouncingBall")
            val h = bb.readRealDirect("h").value
            Assertions.assertEquals(5.0, h, 1e-6)

        }
    }

}
