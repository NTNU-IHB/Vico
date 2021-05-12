package no.ntnu.ihb.vico

import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import no.ntnu.ihb.vico.master.FixedStepMaster
import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.ssp.SSPLoader
import no.ntnu.ihb.vico.structure.RealParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.io.File

internal class SlaveSystemTest {

    @Test
    fun slaveSystemTest() {

        Engine(1.0 / 100).use { engine ->

            val slaveSystem = SlaveSystem(FixedStepMaster(), "default")
            engine.addSystem(slaveSystem)

            val resultDir = File("build/results").also {
                it.deleteRecursively()
                engine.addSystem(SlaveLoggerSystem(null, it))
            }

            val slaveEntity = engine.createEntity("BouncingBall")
            val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
            SlaveComponent(model, "bouncingBall").apply {
                addParameterSet("default", listOf(RealParameter("h", 2.0)))
                slaveEntity.add(this)
            }

            engine.init()

            val slave = slaveSystem.getSlave("bouncingBall")
            Assertions.assertEquals(2.0, slave.readReal("h").value, 1e-6)
            engine.step(100)
            Assertions.assertEquals(1.0, slave.readReal("h").value, 0.1)

            Assertions.assertTrue(resultDir.listFiles()?.size ?: 0 > 0)

        }

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    fun testSSP() {

        Engine(1e-3).use { engine ->

            SSPLoader(TestSsp.get("ControlledDriveTrain.ssp")).load().apply(engine, FixedStepMaster(false))
            val resultDir = File("build/results").also {
                it.deleteRecursively()
                engine.addSystem(SlaveLoggerSystem(null, it))
            }

            engine.step(100)

            Assertions.assertTrue(resultDir.listFiles()?.size ?: 0 > 0)

        }

    }

}

