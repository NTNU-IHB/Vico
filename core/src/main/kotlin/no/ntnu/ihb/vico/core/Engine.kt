package no.ntnu.ihb.vico.core

import no.ntnu.ihb.vico.input.InputAccess
import no.ntnu.ihb.vico.input.InputManager
import no.ntnu.ihb.vico.input.KeyStroke
import no.ntnu.ihb.vico.math.doublesAreEqual
import no.ntnu.ihb.vico.render.RenderEngine
import no.ntnu.ihb.vico.util.PredicateTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

private const val DEFAULT_TIME_STEP = 1.0 / 100

typealias EngineBuilder = Engine.Builder

class Engine private constructor(
    startTime: Double? = null,
    val stopTime: Double? = null,
    baseStepSize: Double? = null,
    private val renderEngine: RenderEngine? = null,
    private val inputManager: InputManager = InputManager(),
    private val connectionManager: ConnectionManager = ConnectionManager(),
    private val entityManager: EntityManager = EntityManager(connectionManager),
    private val systemManager: SystemManager = SystemManager(),
) : EventDispatcher by EventDispatcherImpl(), EntityAccess by entityManager, InputAccess by inputManager, Closeable {

    val startTime: Double = startTime ?: 0.0
    val baseStepSize: Double = baseStepSize ?: DEFAULT_TIME_STEP

    var currentTime: Double = this.startTime
        private set
    var iterations: Long = 0L
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

    init {

        renderEngine?.apply {
            registerCloseListener { this@Engine.close() }
            registerKeyListener {
                when (it) {
                    69 -> runner.togglePause()
                    82 -> runner.toggleEnableRealTime()
                }
            }
            registerClickListener { _, _ ->
            }
        }

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
                LOG.warn("Specified timePoint=$doubleTimePoint exceeds configured stopTime=$it. " +
                        "Simulation will end prematurely."
                )
            }
        }
        while (doubleTimePoint > currentTime && !doublesAreEqual(
                doubleTimePoint,
                currentTime,
                baseStepSize / 2
            ) && !closed.get()
        ) {
            step()
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

    fun <E : SimulationSystem> hasSystem(systemClazz: Class<E>) = systemManager.hasSystem(systemClazz)
    inline fun <reified E : SimulationSystem> hasSystem() = hasSystem(E::class.java)

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.getSystem(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun addSystem(system: BaseSystem) {

        if (renderEngine != null) {

            system.javaClass.declaredFields.firstOrNull {
                it.getAnnotation(InjectRenderer::class.java) != null && RenderEngine::class.java.isAssignableFrom(it.type)
            }?.also { field ->
                field.isAccessible = true
                field.set(system, renderEngine)
            }

        }

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

                return doublesAreEqual(it.currentTime, timePoint, baseStepSize / 5).also { predicateIsTrue ->
                    if (predicateIsTrue) {
                        println("Task invoked at ${it.currentTime}")
                    }
                }

            }

            override fun run() {
                task.run()
            }
        })
    }

    fun invokeIn(t: Double, task: Runnable) {
        return invokeAt(currentTime + t, task)
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
            renderEngine?.close()
            LOG.info("Closed engine..")
        }
    }

    /* fun calculateStepFactor(stepSizeHint: Double): Long {
         return max(1, ceil(stepSizeHint / baseStepSize).toLong())
     }*/

    private companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Engine::class.java)

    }

    class Builder(
            private var startTime: Double? = null,
            private var stopTime: Double? = null,
            private var stepSize: Double? = null,
            private var renderEngine: RenderEngine? = null
    ) {

        constructor() : this(null, null, null, null)

        fun startTime(value: Double?) = apply {
            startTime = value
        }

        fun stopTime(value: Double?) = apply {
            stopTime = value
        }

        fun stepSize(value: Double?) = apply {
            stepSize = value
        }

        fun renderer(renderEngine: RenderEngine?) = apply {
            this.renderEngine = renderEngine
        }

        fun build() = Engine(startTime, stopTime, stepSize, renderEngine)

    }


}
