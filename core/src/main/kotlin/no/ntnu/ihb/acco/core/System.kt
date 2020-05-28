package no.ntnu.ihb.acco.core

import java.io.Closeable

abstract class System(
    private val family: Family,
    decimationFactor: Long? = null,
    priority: Int? = null
) : Comparable<System>, Closeable {

    val priority = priority ?: 0
    val decimationFactor = decimationFactor ?: 1

    private var _engine: Engine? = null
    protected val engine: Engine
        get() = _engine ?: throw IllegalStateException("System is not affiliated with an Engine!")

    val addedToEngine: Boolean
        get() = _engine != null

    var enabled = true
    var initialized = false
        private set
    val interval: Double
        get() = engine.baseStepSize * decimationFactor

    protected lateinit var entities: Set<Entity>

    constructor(family: Family) : this(family, null, null)

    fun addedToEngine(engine: Engine) {
        this._engine = engine
        entities = engine.entityManager.getEntitiesFor(family).apply {
            addObserver = {
                entityAdded(it)
            }
            removeObserver = {
                entityRemoved(it)
            }
            forEach { entityAdded(it) }
        }
    }

    internal fun initialize(currentTime: Double) {
        if (initialized) throw IllegalStateException()
        init(currentTime)
        initialized = true
    }


    protected open fun entityAdded(entity: Entity) {}

    protected open fun entityRemoved(entity: Entity) {}

    protected open fun init(currentTime: Double) {}

    abstract fun step(currentTime: Double, stepSize: Double)

    override fun close() {}

    override fun compareTo(other: System): Int {
        val compare = other.decimationFactor.compareTo(decimationFactor)
        return if (compare == 0) priority.compareTo(other.priority) else compare
    }

}
