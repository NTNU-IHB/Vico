package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.Clock
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.System
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
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

    var targetRealTimeFactor = 1.0
    var enableRealTimeTarget = true
    var actualRealTimeFactor = Double.NaN

    protected fun stepEngine(deltaTime: Double) {

        if (paused.get()) {
            timePaused += deltaTime
        }

        if (enableRealTimeTarget) {
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

private typealias Predicate = ((Engine) -> Boolean)
private typealias Callback = () -> Unit

class HeadlessEngineRunner(
    engine: Engine
) : AbstractEngineRunner(engine) {

    private var thread: Thread? = null
    private var stop = AtomicBoolean(false)

    var callback: Callback? = null
    private var predicate: Predicate? = null

    val isStarted: Boolean
        get() {
            return thread != null
        }

    override fun start() {
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

    override fun stop() {
        thread?.also {
            stop.set(true)
            it.join()
        }
    }

    private inner class Runner : Runnable {

        override fun run() {

            val inputThread = ConsoleInputReader().apply { start() }
            val clock = Clock()
            while (!stop.get() && predicate?.invoke(engine) != true) {
                stepEngine(clock.getDelta())
                callback?.invoke()
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
