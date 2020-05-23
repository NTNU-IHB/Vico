package info.laht.acco.core

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.System
import java.util.concurrent.atomic.AtomicBoolean

interface EngineRunner {

    fun start()

    fun stop()

}

abstract class AbstractEngineRunner(
    protected val engine: Engine
) : EngineRunner {

    val paused = AtomicBoolean(false)
    var timePaused = 0.0
        private set

    var wallClock = 0.0
        private set
    val simulationClock: Double
        get() = engine.currentTime

    val targetRealTimeFactor = 1.0
    val enableRealTimeTarget = AtomicBoolean(true)
    var actualRealTimeFactor = Double.NaN

    protected fun stepEngine(deltaTime: Double) {

        if (paused.get()) {
            timePaused += deltaTime
        }

        if (enableRealTimeTarget.get()) {
            val diff = (simulationClock / targetRealTimeFactor) - wallClock
            if (diff <= 0) {
                engine.step()
            }
        } else {
            engine.step()
        }

        wallClock += deltaTime
        actualRealTimeFactor = simulationClock / wallClock

    }

}

class HeadlessEngineRunner(
    engine: Engine
) : AbstractEngineRunner(engine) {

    var stop = AtomicBoolean()

    override fun start() {

    }

    override fun stop() {

    }

    private inner class Runner : Runnable {

        override fun run() {

        }

    }

    //https://stackoverflow.com/questions/4983065/how-to-interrupt-java-util-scanner-nextline-call
    private inner class ConsoleInputReader : Thread() {

        @Throws(IOException::class)
        override fun run() {
            val br = BufferedReader(InputStreamReader(System.`in`))
            var quit = false
            println()
            println("Commandline options:\n")
            println("\t 'r' -> Enable/disable realtime execution  ")
            println("\t 'p' -> Pause/unpause execution  ")
            println("\t 'q' -> Abort execution")
            println()
            do {
                val input: String? = try { // wait until we have data to complete a readLine()
                    while (!br.ready()) {
                        sleep(200)
                    }
                    br.readLine()
                } catch (e: InterruptedException) {
                    null
                }
                when (input) {
                    "r" -> {
                        enableRealTimeTarget.set(!enableRealTimeTarget.get())
                        if (enableRealTimeTarget.get()) {
                            println("Realtime target enabled, rtf=$targetRealTimeFactor")
                        } else {
                            println("Realtime target disabled")
                        }
                    }
                    "p" -> {
                        paused.set(!paused.get())
                        if (paused.get()) {
                            println("Execution paused at t=${engine.currentTime}")
                        } else {
                            println("Execution resumed")
                        }
                    }
                    null, "q" -> quit = true
                }
            } while (!quit)

            if (!stop.getAndSet(true)) {
                println("Manually aborted execution at t=${engine.currentTime}..")
            }

        }

    }

}
