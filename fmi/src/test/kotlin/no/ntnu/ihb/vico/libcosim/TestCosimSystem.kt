package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.vico.TestFmus
import org.junit.jupiter.api.Test
import java.io.File

class TestCosimSystem {

    @Test
    fun test() {

        val fmuPath = TestFmus.get("1.0/BouncingBall.fmu")
        val resultDir = File("build/results/cosim/BouncingBall").also {
            it.deleteRecursively()
        }

        Engine().use { engine ->

            engine.addEntity(Entity().apply {
                addComponent(CosimFmuComponent(fmuPath, "bouncingBall"))
            })

            engine.addSystem(CosimSystem(CosimLogConfig(resultDir)))

            engine.stepUntil(10.0)

        }

    }

}
