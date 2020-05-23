package info.laht.acco.core

import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

class Engine(
    val baseStepSize: Double = 1.0 / 100
) : Closeable {

    var currentTime = 0.0
        private set
    var iterations = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()

    private val ctx = EngineContext(initialized)
    val entityManager = EntityManager(ctx)
    val systemManager = SystemManager(ctx)

    private val connections = mutableListOf<Connection<*, *>>()

    fun init() {
        if (!initialized.getAndSet(true)) {
            systemManager.initialize(this)
            connections.forEach {
                it.transferData()
            }
        }
    }

    fun step(numSteps: Int = 1) {
        check(!closed.get()) { "Engine has been closed!" }

        require(numSteps > 0)
        if (!initialized.get()) {
            init()
        }

        currentTime += systemManager.step(currentTime, baseStepSize)
        iterations++

        connections.forEach {
            it.transferData()
        }

        ctx.emptyQueue()
    }

    fun addConnection(connection: Connection<*, *>) {
        connections.add(connection)
    }

    fun getEntityByName(name: String) = entityManager.getEntityByName(name)
    fun getEntitiesFor(family: Family) = entityManager.getEntitiesFor(family)

    fun addSystem(system: System) = systemManager.add(system)
    fun removeSystem(system: System) = systemManager.remove(system)

    fun addEntity(entity: Entity, vararg entities: Entity) = entityManager.addEntity(entity, *entities)
    fun removeEntity(entity: Entity, vararg entities: Entity) = entityManager.removeEntity(entity, *entities)

    /* fun addEntityListener(listener: EntityListener, family: Family = emptyFamily) {
         TODO()
     }*/

    override fun close() {
        if (!closed.getAndSet(true)) {
            systemManager.close()
        }
    }

}
