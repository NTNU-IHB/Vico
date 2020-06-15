package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.Clock
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.atomic.AtomicBoolean


private typealias Predicate = ((Engine) -> Boolean)
private typealias Callback = () -> Unit

class EngineRunner(
    private val engine: Engine
) {

    val paused = AtomicBoolean(false)

    var wallClock = 0.0
        private set
    val simulationClock: Double
        get() = engine.currentTime

    var targetRealTimeFactor = 1.0
    var enableRealTimeTarget = true
    var actualRealTimeFactor = Double.NaN

    private var thread: Thread? = null
    private var stop = AtomicBoolean(false)

    var callback: Callback? = null
    private var predicate: Predicate? = null

    val isStarted: Boolean
        get() {
            return thread != null
        }

    fun start() {
        if (this.thread == null) {
            this.stop.set(false)
            this.thread = Thread(Runner()).apply { start() }
        } else {
            throw IllegalStateException("Start can only be invoked once!")
        }
    }

    fun runWhile(predicate: Predicate): Future<Unit> {
        check(!isStarted && this.predicate == null)
        this.predicate = predicate
        val executor = Executors.newCachedThreadPool()
        return FutureTask<Unit> {
            start()
            this.thread!!.join()
            executor.shutdown()
        }.also {
            executor.submit(it)
        }
    }

    fun runUntil(timePoint: Number): Future<Unit> {
        val doubleTimePoint = timePoint.toDouble()
        return runWhile(
            predicate = { it.currentTime >= doubleTimePoint }
        )
    }

    fun runFor(time: Number): Future<Unit> {
        val doubleTime = time.toDouble()
        return runWhile(
            predicate = { it.currentTime + it.startTime >= doubleTime }
        )
    }

    fun stepEngine(deltaTime: Double): Boolean {

        var stepOccurred = false
        if (paused.get()) {
            return false
        }

        if (enableRealTimeTarget) {
            val diff = (simulationClock / targetRealTimeFactor) - wallClock
            if (diff <= 0) {
                engine.step()
                stepOccurred = true
            } else {
                Thread.sleep(1)
            }
        } else {
            engine.step()
            stepOccurred = true
        }

        wallClock += deltaTime
        actualRealTimeFactor = simulationClock / wallClock

        return stepOccurred

    }

    fun stop() {
        thread?.also {
            stop.set(true)
            it.join()
        }
    }

    private inner class Runner : Runnable {

        override fun run() {

            if (!engine.isInitialized) {
                engine.init()
            }

            val inputThread = ConsoleInputReader().apply { start() }
            val clock = Clock()
            while (!engine.isClosed && !stop.get() && predicate?.invoke(engine) != true) {
                val dt = clock.getDelta()
                if (stepEngine(dt)) {
                    callback?.invoke()
                }
            }
            stop.set(true)
            inputThread.interrupt()
            inputThread.join()

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
                        enableRealTimeTarget = !enableRealTimeTarget
                        if (enableRealTimeTarget) {
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
