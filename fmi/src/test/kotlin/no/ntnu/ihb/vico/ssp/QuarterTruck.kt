package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.SlaveSystem
import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.chart.ChartLoader
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import org.junit.jupiter.api.Assertions
import java.io.File


fun main() {

    val resultDir = File("build/results/quarter-truck").also {
        it.deleteRecursively()
    }

    val sspDir = TestSsp.get("quarter-truck")
    val structure = SSPLoader(sspDir).load()

    Engine(1e-3).use { engine ->

        structure.apply(engine)

        ChartLoader.load(File(sspDir, "ChartConfig.xml")).forEach {
            engine.addSystem(it)
        }

        engine.addSystem(
            SlaveLoggerSystem(
                File(sspDir, "LogConfig.xml"), resultDir
            )
        )

        engine.init()

        val chassis = engine.getSystem<SlaveSystem>().getSlave("chassis")
        val mChassis = chassis.readRealDirect("C.mChassis").value
        Assertions.assertEquals(400.0, mChassis, 1e-6)

        engine.stepUntil(10.0)

    }
}
