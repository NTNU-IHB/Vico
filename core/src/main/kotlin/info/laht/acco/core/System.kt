package info.laht.acco.core

import java.io.Closeable

abstract class System(
    private val family: Family,
    val decimationFactor: Int = 1,
    val priority: Int = 0
) : Comparable<System>, Closeable {

    private var nullableEngine: Engine? = null
    protected val engine: Engine
        get() = nullableEngine ?: throw IllegalStateException("System is not affiliated with an Engine!")

    var enabled = true

    val interval: Double
        get() = engine.baseStepSize * decimationFactor

    val entities by lazy {
        engine.entityManager.getEntitiesFor(family)
    }

    internal fun initialize(engine: Engine) {
        this.nullableEngine = engine
        entities.forEach { entityAdded(it) }
        init()
    }

    protected open fun init() {}

    protected open fun entityAdded(entity: Entity) {}

    protected open fun entityRemoved(entity: Entity) {}

    abstract fun step(currentTime: Double, stepSize: Double)

    override fun close() {}

    override fun compareTo(other: System): Int {
        val compare = other.decimationFactor.compareTo(decimationFactor)
        return if (compare == 0) priority.compareTo(other.priority) else compare
    }

}

abstract class IteratingSystem(
    family: Family,
    decimationFactor: Int = 1,
    priority: Int = 0
) : System(family, decimationFactor, priority) {

    override fun step(currentTime: Double, stepSize: Double) {
        entities.forEach { e ->
            processEntity(e, currentTime, stepSize)
        }
    }

    protected abstract fun processEntity(entity: Entity, currentTime: Double, stepSize: Double)

}

abstract class ParallelIteratingSystem(
    family: Family,
    decimationFactor: Int = 1
) : System(family, decimationFactor) {

    override fun step(currentTime: Double, stepSize: Double) {
        entities.parallelStream().forEach { e ->
            processEntity(e, currentTime, stepSize)
        }
    }

    protected abstract fun processEntity(entity: Entity, currentTime: Double, stepSize: Double)
}
