package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.input.KeyStroke
import no.ntnu.ihb.vico.util.Clock
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Predicate


class EngineRunner internal constructor(
    private val engine: Engine
) {

    val paused = AtomicBoolean(false)

    private var wallClock = 0.0
    val simulationClock: Double
        get() = engine.currentTime

    var targetRealTimeFactor = 1.0
    var enableRealTimeTarget = true
    var actualRealTimeFactor = Double.NaN
    var timeSpentSimulating = 0.0
        private set

    private var thread: Thread? = null
    private var stop = AtomicBoolean(false)

    var callback: Runnable? = null
    private var predicate: Predicate<Engine>? = null

    val started: Boolean
        get() {
            return thread != null
        }

    @JvmOverloads
    fun start(paused: Boolean? = null) = apply {
        if (this.thread == null) {
            this.stop.set(false)
            paused?.also { this.paused.set(it) }
            this.thread = Thread(Runner()).apply { start() }
        } else {
            throw IllegalStateException("Start can only be invoked once!")
        }
    }

    @JvmOverloads
    fun startAndWait(paused: Boolean? = null) {
        paused?.also { this.paused.set(it) }
        runWhile { true }.get()
    }

    fun runWhile(predicate: Predicate<Engine>): Future<Unit> {
        check(!started && this.predicate == null)
        this.predicate = predicate
        val executor = Executors.newSingleThreadExecutor()
        return FutureTask {
            start()
            this.thread!!.join()
            executor.shutdown()
        }.also {
            executor.submit(it)
        }
    }

    fun runWhileAndWait(predicate: Predicate<Engine>) {
        runWhile(predicate).get()
    }

    fun runUntil(timePoint: Number): Future<Unit> {
        val doubleTimePoint = timePoint.toDouble()
        return runWhile(
            predicate = { it.currentTime + engine.baseStepSize < doubleTimePoint }
        )
    }

    fun runUntilAndWait(time: Number) {
        runUntil(time).get()
    }

    fun runFor(time: Number): Future<Unit> {
        val doubleTime = time.toDouble()
        return runWhile(
            predicate = { (it.currentTime + engine.baseStepSize + it.startTime) <= doubleTime }
        )
    }

    fun runForAndWait(time: Number) {
        runFor(time).get()
    }

    fun togglePause() {
        paused.set(!paused.get())
    }

    fun toggleEnableRealTime() {
        enableRealTimeTarget = !enableRealTimeTarget
    }

    private fun stepEngine(deltaTime: Double): Boolean {

        if (paused.get()) {
            return false
        }

        var stepOccurred = false
        if (enableRealTimeTarget) {
            val diff = (simulationClock / targetRealTimeFactor) - wallClock
            if (diff <= 0) {
                engine.step()
                stepOccurred = true
            } else {
                Thread.sleep(1)
            }
            wallClock += deltaTime
        } else {
            engine.step()
            stepOccurred = true
        }

        timeSpentSimulating += deltaTime
        actualRealTimeFactor = simulationClock / timeSpentSimulating

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

            val inputThread: ConsoleInputReader = ConsoleInputReader().apply { start() }
            val clock = Clock()
            while (!engine.isClosed && !stop.get() && predicate?.test(engine) == true) {
                val dt = clock.getDelta()
                if (stepEngine(dt)) {
                    callback?.run()
                }
            }
            stop.set(true)
            inputThread.interrupt()
            inputThread.join()

        }

    }

    //https://stackoverflow.com/questions/4983065/how-to-interrupt-java-util-scanner-nextline-call
    private inner class ConsoleInputReader : Thread() {

        override fun run() {
            val br = System.`in`.bufferedReader()
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

                input?.toCharArray()?.forEach { c ->
                    when (c) {
                        'e' -> engine.registerKeyPress(KeyStroke.KEY_E)
                        'r' -> engine.registerKeyPress(KeyStroke.KEY_R)
                        'w' -> engine.registerKeyPress(KeyStroke.KEY_W)
                        'a' -> engine.registerKeyPress(KeyStroke.KEY_A)
                        's' -> engine.registerKeyPress(KeyStroke.KEY_S)
                        'd' -> engine.registerKeyPress(KeyStroke.KEY_D)
                        'q' -> quit = true
                    }
                } ?: kotlin.run {
                    quit = true
                }

                sleep(10)

            } while (!quit)

            if (!stop.getAndSet(true)) {
                println("Manually aborted execution at t=${engine.currentTime}..")
                engine.close()
            }

        }

    }

}
