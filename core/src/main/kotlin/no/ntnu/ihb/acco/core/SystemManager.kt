package no.ntnu.ihb.acco.core

import java.io.Closeable
import java.util.*


class SystemManager private constructor(
    private val ctx: EngineContext,
    private val groups: SortedMap<Int, MutableList<System>>
) : Closeable {

    val systems: List<System>
        get() = groups.flatMap { it.value }

    internal constructor(ctx: EngineContext)
            : this(ctx, TreeMap(Comparator<Int> { o1, o2 -> o2.compareTo(o1) }))

    fun initialize(engine: Engine, currentTime: Double) {
        groups.flatMap { it.value }.forEach { system ->
            system.initialize(engine, currentTime)
        }
    }

    fun step(currentTime: Double, baseStepSize: Double): Double {
        val stepSize = baseStepSize * groups.firstKey()
        val endTime = currentTime + stepSize
        groups.forEach { (decimationFactor, systemGroup) ->
            systemGroup.forEach { system ->
                if (system.enabled) {
                    var t = currentTime
                    val dt = baseStepSize * decimationFactor
                    do {
                        system.step(t, dt)
                        t += dt
                    } while (t < endTime)
                }
            }
        }

        return stepSize
    }

    fun add(system: System) {
        ctx.safeContext { internalAdd(system) }
    }

    private fun internalAdd(system: System) {
        groups.computeIfAbsent(system.decimationFactor) {
            mutableListOf()
        }.add(system)
        groups
    }

    fun remove(system: System) {
        ctx.safeContext { internalRemove(system) }
    }

    private fun internalRemove(system: System) {
        groups[system.decimationFactor]?.remove(system)
    }

    override fun close() {
        groups.flatMap { it.value }.forEach { it.close() }
    }

}
