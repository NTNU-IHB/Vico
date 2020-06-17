package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.ObservableSet
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

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
    private val queue: Queue<Runnable> = ArrayDeque()

    val isInitialized: Boolean
        get() = initialized.get()

    val isClosed: Boolean
        get() = closed.get()

    private val entityManager = EntityManager()
    internal val systemManager = SystemManager(this)
    private val connectionManager = ConnectionManager()

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

            currentTime += systemManager.step(currentTime, baseStepSize)
            iterations++

            connectionManager.update()

            emptyQueue()
        }

    }

    fun stepUntil(timePoint: Double) {
        while (timePoint > currentTime) {
            step()
        }
    }

    fun getEntitiesFor(family: Family): ObservableSet<Entity> {
        return entityManager.getEntitiesFor(family)
    }

    fun addEntity(entity: Entity) = entityManager.addEntity(entity)
    fun addAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        entityManager.addAllEntities(entity, *additionalEntities)

    fun removeEntity(entity: Entity) = entityManager.removeEntity(entity)

    fun getEntityByName(name: String) = entityManager.getEntityByName(name)
    fun getEntitiesByTag(tag: String) = entityManager.getEntitiesByTag(tag)

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.get(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun addSystem(system: EventSystem) = systemManager.add(system)
    fun addSystem(system: ManipulationSystem) = systemManager.add(system)

    fun removeSystem(system: Class<out BaseSystem>) = systemManager.remove(system)
    inline fun <reified E : BaseSystem> removeSystem() = removeSystem(E::class.java)

    fun addConnection(connection: Connection) = connectionManager.addConnection(connection)
    fun updateConnection(key: Component) = connectionManager.updateConnection(key)

    internal fun safeContext(task: Runnable) {
        if (initialized.get()) {
            queue.add(task)
        } else {
            task.run()
        }
    }

    private fun emptyQueue() {
        while (!queue.isEmpty()) {
            queue.poll().run()
        }
    }

    override fun close() {
        if (!closed.getAndSet(true)) {
            systemManager.close()
        }
    }

}
