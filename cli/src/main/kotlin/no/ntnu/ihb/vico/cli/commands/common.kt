package no.ntnu.ihb.vico.cli.commands

import no.ntnu.ihb.vico.core.Engine
import no.ntnu.ihb.vico.util.formatForOutput
import org.slf4j.Logger
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal fun getConfigPath(parent: File, config: String): File {
    val file = File(config)
    return if (file.isAbsolute) {
        file
    } else {
        File(parent, config)
    }
}

@ExperimentalTime
internal fun runSimulation(
    engine: Engine,
    startTime: Double,
    stopTime: Double,
    baseStepSize: Double,
    targetRealtimeFactor: Double?,
    LOG: Logger
) {

    val totalSimulationTime = stopTime - startTime
    val numSteps = (totalSimulationTime / baseStepSize).toLong()
    val aTenth = numSteps / 10

    val runner = engine.runner

    targetRealtimeFactor?.also {
        runner.targetRealTimeFactor = it
    } ?: run {
        runner.enableRealTimeTarget = false
    }
    runner.callback = Runnable {
        val i = engine.iterations
        if (i != 0L && i % aTenth == 0L || i == numSteps) {
            val percentComplete = i / numSteps.toDouble() * 100
            LOG.info(
                "{}% complete, simulated {}s in {}s, target RTF={}, actual RTF={}",
                percentComplete,
                runner.simulationClock.formatForOutput(),
                runner.wallClock.formatForOutput(),
                if (runner.enableRealTimeTarget) runner.targetRealTimeFactor else "unbounded",
                runner.actualRealTimeFactor.formatForOutput()
            )
        }
    }


    measureTime {
        runner.runUntil(stopTime).get()
    }.also { t ->
        val targetRTF: Any = if (runner.enableRealTimeTarget) runner.targetRealTimeFactor else "unbounded"
        LOG.info(
            "Simulation finished. " +
                    "Simulated ${totalSimulationTime}s in ${t.inSeconds}s, " +
                    "RTF: target=$targetRTF, actual=${runner.actualRealTimeFactor}"
        )
    }
}
