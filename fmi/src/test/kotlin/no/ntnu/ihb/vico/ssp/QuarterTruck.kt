package no.ntnu.ihb.vico.ssp

import no.ntnu.ihb.vico.TestSsp
import no.ntnu.ihb.vico.chart.ChartLoader
import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.log.SlaveLoggerSystem
import java.io.File

internal fun measureTime(f: () -> Unit): Double {
    val start = System.currentTimeMillis()
    f()
    val stop = System.currentTimeMillis()
    return ((stop - start).toDouble())/1000
}

fun main() {

    val resultDir = File("build/results/quarter-truck").also {
        it.deleteRecursively()
    }

    val sspDir = TestSsp.get("quarter-truck")
    val structure = SSPLoader(sspDir).load()

    Engine(1e-2).use { engine ->

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
            engine.runner.apply {
                enableRealTimeTarget = false
                runUntil(10).get()
            }

        }.also {
            println("Simulation took ${it}s")
        }

    }
}
