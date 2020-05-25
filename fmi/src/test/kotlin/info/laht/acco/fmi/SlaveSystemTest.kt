package info.laht.acco.fmi

import info.laht.acco.ModelResolver
import info.laht.acco.core.Engine
import info.laht.acco.core.Entity
import no.ntnu.ihb.fmi4j.readReal
import no.ntnu.ihb.vico.TestFmus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class SlaveSystemTest {

    @Test
    fun slaveSystemTest() {

        Engine(1.0 / 100).use { engine ->

            val slaveSystem = SlaveSystem()
            slaveSystem.setupLogging()
            engine.addSystem(slaveSystem)

            val slaveEntity = Entity("")
            val model = ModelResolver.resolve(TestFmus.get("fmus/1.0/BouncingBall.fmu"))
            val slaveInstance = model.instantiate("bouncingBall")

            val slave = SlaveComponent(slaveInstance)
            slaveEntity.addComponent(slave)
            engine.addEntity(slaveEntity)

            slave.markForReading("h")
            engine.init()
            Assertions.assertEquals(1.0, slave.readReal("h").value, 1e-6)
            engine.step(100)
            Assertions.assertTrue(slave.readReal("h").value > 0)

        }

    }

}

