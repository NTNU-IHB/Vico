package no.ntnu.ihb.acco.core

import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Engine(
    startTime: Double? = null,
    baseStepSize: Double? = null
) : Closeable {

    val startTime = startTime ?: 0.0
    val baseStepSize = baseStepSize ?: 1.0 / 100

    var currentTime = this.startTime
        private set
    var iterations = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()
    private val queue: Queue<() -> Unit> = ArrayDeque()

    val entityManager = EntityManager(this)
    val systemManager = SystemManager(this)

    private val connections = mutableListOf<Connection<*, *>>()

    constructor(baseStepSize: Double) : this(null, baseStepSize)

    fun init() {
        if (!initialized.getAndSet(true)) {
            systemManager.initialize(currentTime)
            connections.forEach {
                it.transferData()
            }
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

            connections.forEach {
                it.transferData()
            }

            emptyQueue()
        }

    }

    fun addConnection(connection: Connection<*, *>) {
        connections.add(connection)
    }

    fun getEntityByName(name: String) = entityManager.getEntityByName(name)
    fun getEntitiesFor(family: Family) = entityManager.getEntitiesFor(family)

    fun addSystem(system: System) = systemManager.add(system)
    fun removeSystem(system: Class<out System>) = systemManager.remove(system)

    fun addEntity(entity: Entity, vararg entities: Entity) = entityManager.addEntity(entity, *entities)
    fun removeEntity(entity: Entity, vararg entities: Entity) = entityManager.removeEntity(entity, *entities)


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

}
