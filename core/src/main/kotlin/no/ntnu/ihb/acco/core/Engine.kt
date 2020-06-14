package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.Tag
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Engine @JvmOverloads constructor(
    startTime: Double? = null,
    baseStepSize: Double? = null
) : Closeable, EventDispatcher by EventDispatcherImpl() {

    val startTime = startTime ?: 0.0
    val baseStepSize = baseStepSize ?: 1.0 / 100

    var currentTime = this.startTime
        private set
    var iterations = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()
    private val queue: Queue<() -> Unit> = ArrayDeque()

    private val entityManager = EntityManager(this)
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

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.get(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun getEntityByName(name: String) = entityManager.getEntityByName(name)
    fun getEntitiesFor(family: Family) = entityManager.getEntitiesFor(family)

    fun addSystem(system: ManipulationSystem) = systemManager.add(system)
    fun addSystem(system: EventSystem) = systemManager.add(system)

    fun removeSystem(system: Class<out BaseSystem>) = systemManager.remove(system)
    inline fun <reified E : BaseSystem> removeSystem() = removeSystem(E::class.java)

    fun addEntity(entity: Entity, vararg entities: Entity) = entityManager.addEntity(entity, *entities)
    fun removeEntity(entity: Entity, vararg entities: Entity) = entityManager.removeEntity(entity, *entities)

    fun addConnection(connection: Connection) = connectionManager.addConnection(connection)
    fun updateConnection(key: Component) = connectionManager.updateConnection(key)

    internal fun safeContext(task: () -> Unit) {
        if (initialized.get()) {
            queue.add(task)
        } else {
            task.invoke()
        }
    }

    private fun emptyQueue() {
        while (!queue.isEmpty()) {
            queue.poll().invoke()
        }
    }

    override fun close() {
        if (!closed.getAndSet(true)) {
            systemManager.close()
        }
    }

    fun getEntityByTag(tag: String) = getEntityByTag(Tag(tag))
    fun getEntityByTag(tag: Tag) = entityManager.getEntityByTag(tag)

}
