package no.ntnu.ihb.vico.log

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.SlaveComponent
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestFmus
import no.ntnu.ihb.vico.model.ModelResolver
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class TestLogConfig {

    @Test
    fun testLogConfigBuilder() {

        val logConfig = File(
            TestLogConfig::class.java.classLoader
                .getResource("logconfig/LogConfig1.xml")!!.file
        )

        Assertions.assertTrue(logConfig.exists())

        Engine(1.0 / 100).use { engine ->

            engine.addSystem(SlaveSystem())
            val resultDir = File("build/results").also {
                it.deleteRecursively()
                engine.addSystem(SlaveLoggerSystem(logConfig, it))
            }

            for (i in 1..2) {
                Entity("BouncingBall_$i").also { slaveEntity ->
                    val model = ModelResolver.resolve(TestFmus.get("1.0/BouncingBall.fmu"))
                    SlaveComponent(model, "bouncingBall_$i").apply {
                        slaveEntity.addComponent(this)
                    }
                    engine.addEntity(slaveEntity)
                }
            }

            engine.step(5)

            Assertions.assertTrue(resultDir.exists())

        }
    }

}
