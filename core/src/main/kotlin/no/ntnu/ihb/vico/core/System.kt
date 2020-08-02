package no.ntnu.ihb.vico.core

import java.io.Closeable
import java.util.*

sealed class BaseSystem(
    override val family: Family
) : Comparable<BaseSystem>, EntityListener, Closeable {

    var priority = 0
        protected set

    var enabled = true
    private var _engine: Engine? = null
    protected val engine: Engine
        get() = _engine ?: throw IllegalStateException("System is not affiliated with an Engine!")

    val addedToEngine: Boolean
        get() = _engine != null

    protected lateinit var entities: Set<Entity>

    var initialized = false
        private set

    internal fun addedToEngine(engine: Engine) {
        this._engine = engine
        this.entities = engine.getEntitiesFor(family)
        this.entities.forEach { entity -> entityAdded(entity) }

        assignedToEngine(engine)
    }

    internal fun initialize(currentTime: Double) {
        check(!initialized) {
            "System already initialized!"
        }

        init(currentTime)
        initialized = true
    }

    fun dispatchEvent(type: String, target: Any?) = engine.dispatchEvent(type, target)

    protected open fun assignedToEngine(engine: Engine) {}

    override fun entityAdded(entity: Entity) {}

    override fun entityRemoved(entity: Entity) {}

    protected open fun init(currentTime: Double) {}

    open fun postInit() {}

    override fun close() {}

    override fun compareTo(other: BaseSystem): Int {
        return priority.compareTo(other.priority)
    }
}

abstract class EventSystem(
    family: Family
) : BaseSystem(family), EventListener {

    private val listenQueue = ArrayDeque<String>()

    override fun onEvent(evt: Event) {
        if (enabled) {
            eventReceived(evt)
        }
    }

    protected fun listen(type: String) {
        if (addedToEngine) {
            engine.addEventListener(type, this)
        } else {
            listenQueue.add(type)
        }
    }

    override fun assignedToEngine(engine: Engine) {
        while (!listenQueue.isEmpty()) {
            listen(listenQueue.pop())
        }
    }

    protected abstract fun eventReceived(evt: Event)

}

abstract class ManipulationSystem(
    family: Family
) : BaseSystem(family) {

    var decimationFactor = 1L

    val interval: Double
        get() = engine.baseStepSize * decimationFactor

    override fun compareTo(other: BaseSystem): Int {
        return if (other is ManipulationSystem) {
            val compare = other.decimationFactor.compareTo(decimationFactor)
            if (compare == 0) super.compareTo(other) else compare
        } else {
            super.compareTo(other)
        }
    }

}

abstract class ObserverSystem(
    family: Family
) : ManipulationSystem(family) {

    abstract fun observe(currentTime: Double)

}

abstract class SimulationSystem(
    family: Family
) : ManipulationSystem(family) {

    abstract fun step(currentTime: Double, stepSize: Double)

}
