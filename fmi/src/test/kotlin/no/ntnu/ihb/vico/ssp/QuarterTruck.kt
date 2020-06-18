package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.acco.core.Engine
import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.chart.ChartLoader
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
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
                File(sspDir, "LogConfig_vico.xml"), resultDir
            )
        )

        measureTime {
            engine.init()

            engine.runner.apply {
                enableRealTimeTarget = false
                runUntil(100).get()
            }

        }.also {
            println("simulation took ${it.inSeconds}s")
        }

    }
}
