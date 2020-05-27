package no.ntnu.ihb.vico

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.ssp.SSPLoader
import no.ntnu.ihb.vico.structure.RealParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class SlaveSystemTest {

    @Test
    fun slaveSystemTest() {

        Engine(1.0 / 100).use { engine ->

            val slaveSystem = FixedStepSlaveSystem()
            val resultDir = File("build/results").also {
                it.deleteRecursively()
                slaveSystem.setupLogging(it)
            }

            engine.addSystem(slaveSystem)

            val slaveEntity = Entity("BouncingBall")
            val model = ModelResolver.resolve(TestFmus.get("fmus/1.0/BouncingBall.fmu"))
            SlaveComponent(model, "bouncingBall").apply {
                addParameterSet("default", listOf(RealParameter("h", 2.0)))
                slaveEntity.addComponent(this)
            }
            engine.addEntity(slaveEntity)

            engine.init()

            val slave = slaveSystem.getSlave("bouncingBall")
            slave.markForReading("h")

            Assertions.assertEquals(2.0, slave.readReal("h").value, 1e-6)
            engine.step(100)
            Assertions.assertTrue(slave.readReal("h").value > 0)

            Assertions.assertTrue(resultDir.listFiles()?.size ?: 0 > 0)

        }

    }

    @Test
    fun testSSP() {

        Engine(1.0 / 100).use { engine ->

            SSPLoader(TestSsp.get("ControlledDriveTrain.ssp")).load().apply(engine)
            val resultDir = File("build/results").also {
                it.deleteRecursively()
                engine.systemManager.get(FixedStepSlaveSystem::class.java).setupLogging(it)
            }

            engine.step(100)

            Assertions.assertTrue(resultDir.listFiles()?.size ?: 0 > 0)

        }

    }

}

