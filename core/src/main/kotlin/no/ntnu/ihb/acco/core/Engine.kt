package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.input.InputAccess
import no.ntnu.ihb.acco.input.InputManager
import no.ntnu.ihb.acco.input.KeyStroke
import no.ntnu.ihb.acco.scenario.ScenarioContext
import no.ntnu.ihb.acco.util.PredicateTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil
import kotlin.math.max

private const val DEFAULT_TIME_STEP = 1.0 / 100

typealias EngineBuilder = Engine.Builder

interface EngineInfo {
    val currentTime: Double
    val iterations: Long
}

class Engine private constructor(
    startTime: Double? = null,
    val stopTime: Double? = null,
    baseStepSize: Double? = null,
    private val inputManager: InputManager = InputManager(),
    private val connectionManager: ConnectionManager = ConnectionManager(),
    private val entityManager: EntityManager = EntityManager(connectionManager),
    private val systemManager: SystemManager = SystemManager(),
) : EventDispatcher by EventDispatcherImpl(), EngineInfo, EntityAccess by entityManager, InputAccess by inputManager,
    Closeable {

    val startTime: Double = startTime ?: 0.0
    val baseStepSize: Double = baseStepSize ?: DEFAULT_TIME_STEP

    override var currentTime: Double = this.startTime
        private set
    override var iterations: Long = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()

    private val taskQueue: Queue<Runnable> = ArrayDeque()
    private val predicateTaskQueue: MutableList<PredicateTask> = mutableListOf()

    val isInitialized: Boolean
        get() = initialized.get()

    val isClosed: Boolean
        get() = closed.get()

    val runner: EngineRunner by lazy { EngineRunner(this) }

    constructor() : this(null, null, null)
    constructor(baseStepSize: Double) : this(null, null, baseStepSize)
    constructor(startTime: Double, baseStepSize: Double) : this(startTime, null, baseStepSize)

    class Builder {
        private var startTime: Double? = null
        private var stopTime: Double? = null
        private var stepSize: Double? = null

        fun startTime(value: Double?) = apply {
            startTime = value
        }

        fun stopTime(value: Double?) = apply {
            stopTime = value
        }

        fun stepSize(value: Double?) = apply {
            stepSize = value
        }

        fun build() = Engine(startTime, stopTime, stepSize)

    }

    fun init() {
        if (!initialized.getAndSet(true)) {
            systemManager.initialize(currentTime)
            connectionManager.update()
        }
        digestQueue()
    }

    @JvmOverloads
    fun step(numSteps: Int = 1) {

        check(!closed.get()) { "Engine has been closed!" }
        require(numSteps > 0) { "Parameter 'numSteps' must be >= 1! Was: $numSteps" }

        if (!initialized.get()) {
            init()
        }

        for (i in 0 until numSteps) {
            systemManager.step(iterations, currentTime, baseStepSize)
            currentTime += baseStepSize
            iterations++

            connectionManager.update()

            digestQueue()

            inputManager.clear()
        }

        stopTime?.also {
            if (currentTime >= it) {
                close()
            }
        }

    }

    fun stepUntil(timePoint: Number) {
        val doubleTimePoint = timePoint.toDouble()
        stopTime?.also {
            if (doubleTimePoint > stopTime) {
                LOG.warn(
                    "Specified timePoint=$doubleTimePoint exceeds configured stopTime=$it. " +
                            "Simulation will end prematurely."
                )
            }
        }
        while (doubleTimePoint > currentTime && !closed.get()) {
            step()
        }
    }

    fun applyScenario(scenarioContext: ScenarioContext) {
        scenarioContext.timedActions.forEach { (timePoint, action) ->
            invokeAt(timePoint) {
                action.invoke(this)
            }
        }
        scenarioContext.predicateActions.forEach { action ->
            invokeWhen(action.invoke(this))
        }
    }

    override fun registerKeyPress(keyStroke: KeyStroke) {
        when (keyStroke) {
            KeyStroke.KEY_E -> runner.togglePause()
            KeyStroke.KEY_R -> runner.toggleEnableRealTime()
            KeyStroke.KEY_Q -> runner.stop()
            else -> inputManager.registerKeyPress(keyStroke)
        }
    }

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.getSystem(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun addSystem(system: BaseSystem) {
        invokeLater {
            systemManager.addSystem(system)
            entityManager.addEntityListener(system)
            system.addedToEngine(this)
        }
    }

    fun removeSystem(system: Class<out BaseSystem>) {
        invokeLater {
            systemManager.removeSystem(system)
            if (system is EntityListener) {
                entityManager.removeEntityListener(system)
            }
        }
    }

    inline fun <reified E : BaseSystem> removeSystem() = removeSystem(E::class.java)

    fun addConnection(connection: Connection) {
        invokeLater {
            connectionManager.addConnection(connection)
        }
    }

    fun updateConnection(key: Component) = connectionManager.updateConnection(key)

    fun invokeLater(task: Runnable) {
        if (initialized.get()) {
            taskQueue.add(task)
        } else {
            task.run()
        }
    }

    fun invokeAt(timePoint: Double, task: Runnable) {
        invokeWhen(object : PredicateTask {

            override fun test(it: Engine): Boolean {
                return it.currentTime >= timePoint
            }

            override fun run() {
                task.run()
            }
        })
    }

    fun invokeIn(t: Double, task: Runnable) {
        val timeStamp = currentTime
        invokeWhen(object : PredicateTask {

            override fun test(it: Engine): Boolean {
                return it.currentTime >= timeStamp + t
            }

            override fun run() {
                task.run()
            }
        })
    }

    fun invokeWhen(predicateTask: PredicateTask) {
        if (predicateTask.test(this)) {
            invokeLater(predicateTask)
        } else {
            predicateTaskQueue.add(predicateTask)
        }
    }

    private fun digestQueue() {
        while (taskQueue.isNotEmpty()) {
            taskQueue.poll().run()
        }
        val toBeRemoved = mutableListOf<PredicateTask>()
        for (i in predicateTaskQueue.indices) {
            val task = predicateTaskQueue[i]
            if (task !in toBeRemoved) {
                if (task.test(this)) {
                    task.run()
                    toBeRemoved.add(task)
                }
            }
        }
        toBeRemoved.forEach { task -> predicateTaskQueue.remove(task) }
    }

    override fun close() {
        if (!closed.getAndSet(true)) {
            systemManager.close()
            LOG.info("Closed engine..")
        }
    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Engine::class.java)

    }

    fun calculateStepFactor(stepSizeHint: Double): Long {
        return max(1, ceil(stepSizeHint / baseStepSize).toLong())
    }

}
