package no.ntnu.ihb.acco.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil
import kotlin.math.max
import java.util.function.Predicate
import kotlin.math.ceil
import kotlin.math.max

private const val DEFAULT_TIME_STEP = 1.0 / 100

class Engine @JvmOverloads constructor(
    startTime: Double? = null,
    baseStepSize: Double? = null
) : EventDispatcher by EventDispatcherImpl(), Closeable {

    val startTime = startTime ?: 0.0
    val baseStepSize = baseStepSize ?: DEFAULT_TIME_STEP

    var currentTime = this.startTime
        private set
    var iterations = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()
    private val taskQueue: Queue<Runnable> = ArrayDeque()
    private val predicateTaskQueue: MutableList<Pair<Runnable, Predicate<Engine>>> = mutableListOf()

    val isInitialized: Boolean
        get() = initialized.get()

    val isClosed: Boolean
        get() = closed.get()

    private val connectionManager = ConnectionManager()
    private val entityManager = EntityManager(connectionManager)
    internal val systemManager = SystemManager()

    val runner by lazy { EngineRunner(this) }

    constructor(baseStepSize: Double) : this(null, baseStepSize)

    fun init() {
        if (!initialized.getAndSet(true)) {
            systemManager.initialize(currentTime)
            connectionManager.update()
        }
    }

    fun step(numSteps: Int = 1) {

        require(numSteps > 0) { "Parameter 'numSteps' must be >= 1! Was: $numSteps" }
        check(!closed.get()) { "Engine has been closed!" }

        if (!initialized.get()) {
            init()
        }

        for (i in 0 until numSteps) {

            systemManager.step(iterations, currentTime, baseStepSize)
            currentTime += baseStepSize
            iterations++

            connectionManager.update()

            digestQueue()
        }

    }

    fun stepUntil(timePoint: Number) {
        val doubleTimePoint = timePoint.toDouble()
        while (doubleTimePoint > currentTime) {
            step()
        }
    }

    fun getEntitiesFor(family: Family): Set<Entity> {
        return entityManager.getEntitiesFor(family)
    }

    fun addEntity(entity: Entity, vararg additionalEntities: Entity) = invokeLater {
        entityManager.addAllEntities(entity, *additionalEntities)
    }

    fun removeEntity(entity: Entity) = invokeLater {
        entityManager.removeEntity(entity)
    }

    fun getEntityByName(name: String) = entityManager.getEntityByName(name)
    fun getEntityByName(name: String, hierarchical: Boolean) = entityManager.getEntityByName(name, hierarchical)
    fun getEntitiesByTag(tag: String) = entityManager.getEntitiesByTag(tag)

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.get(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun addSystem(system: EventSystem) = internalAddSystem(system)
    fun addSystem(system: ManipulationSystem) = internalAddSystem(system)

    private fun internalAddSystem(system: BaseSystem) {
        invokeLater {
            systemManager.add(system)
            entityManager.addEntityListener(system)
            system.addedToEngine(this)
        }
    }

    fun removeSystem(system: Class<out BaseSystem>) {
        invokeLater {
            systemManager.remove(system)
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
        invokeWhen(task) { it.currentTime >= timePoint }
    }

    fun invokeIn(t: Double, task: Runnable) {
        val timeStamp = currentTime
        invokeWhen(task) {
            it.currentTime >= timeStamp + t
        }
    }

    fun invokeWhen(task: Runnable, predicate: Predicate<Engine>) {
        if (predicate.test(this)) {
            invokeLater(task)
        } else {
            predicateTaskQueue.add(task to predicate)
        }
    }

    private fun digestQueue() {
        while (taskQueue.isNotEmpty()) {
            taskQueue.poll().run()
        }
        val toBeRemoved = mutableListOf<Int>()
        for (i in predicateTaskQueue.indices) {
            if (i !in toBeRemoved) {
                val (task, predicate) = predicateTaskQueue[i]
                if (predicate.test(this)) {
                    task.run()
                    toBeRemoved.add(i)
                }
            }
        }
        toBeRemoved.forEach { index -> predicateTaskQueue.removeAt(index) }
    }

    override fun close() {
        if (!closed.getAndSet(true)) {
            systemManager.close()
        }
        LOG.info("Closed engine..")
    }

    companion object {

        private val LOG: Logger = LoggerFactory.getLogger(Engine::class.java)

    }

    fun calculateStepFactor(stepSizeHint: Double): Long {
        return max(1, ceil(stepSizeHint / baseStepSize).toLong())
    }

}
