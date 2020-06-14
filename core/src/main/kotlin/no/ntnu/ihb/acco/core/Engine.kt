package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.ObservableSet
import no.ntnu.ihb.acco.util.Tag
import no.ntnu.ihb.acco.util.toTag
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Engine @JvmOverloads constructor(
    startTime: Double? = null,
    baseStepSize: Double? = null
) : Entity(), EventDispatcher by EventDispatcherImpl(), Closeable {

    val startTime = startTime ?: 0.0
    val baseStepSize = baseStepSize ?: 1.0 / 100

    var currentTime = this.startTime
        private set
    var iterations = 0L
        private set

    private val initialized = AtomicBoolean()
    private val closed = AtomicBoolean()
    private val queue: Queue<() -> Unit> = ArrayDeque()

    internal val systemManager = SystemManager(this)
    private val connectionManager = ConnectionManager()

    private val families: MutableMap<Family, ObservableSet<Entity>> = mutableMapOf()

    init {
        tag = "root".toTag()
    }

    constructor(baseStepSize: Double) : this(null, baseStepSize)

    override fun descendantAdded(entity: Entity) {
        super.descendantAdded(entity)
        updateFamilyMemberShip(entity)
    }

    override fun descendantRemoved(entity: Entity) {
        super.descendantRemoved(entity)
        families.values.forEach { entities ->
            entities.remove(entity)
        }
    }

    fun getEntitiesFor(family: Family): ObservableSet<Entity> {
        return families.computeIfAbsent(family) {
            findAllInDescendants { family.test(it) }
            ObservableSet(findAllInDescendants { family.test(it) }.toMutableSet())
        }
    }

    private fun updateFamilyMemberShip(entity: Entity) {
        families.forEach { (family, entities) ->
            if (family.test(entity)) {
                entities.add(entity)
            } else {
                entities.remove(entity)
            }
        }
    }

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

    fun getEntityByName(name: String) = findInDescendants { it.name == name }
    fun getEntityByTag(tag: String) = findInDescendants { it.tag?.value == tag }
    fun getEntityByTag(tag: Tag) = findInDescendants { it.tag == tag }

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>) = systemManager.get(systemClass)
    inline fun <reified E : SimulationSystem> getSystem() = getSystem(E::class.java)

    fun addSystem(system: ManipulationSystem) = systemManager.add(system)
    fun addSystem(system: EventSystem) = systemManager.add(system)

    fun removeSystem(system: Class<out BaseSystem>) = systemManager.remove(system)
    inline fun <reified E : BaseSystem> removeSystem() = removeSystem(E::class.java)

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


}
