package no.ntnu.ihb.acco.core

import java.io.Closeable
import java.util.*


class SystemManager(
    private val engine: Engine
) : Closeable {

    val systems: List<System>
        get() = groups.flatMap { it.value }

    private val groups = TreeMap<Int, MutableList<System>>(Comparator { o1, o2 -> o2.compareTo(o1) })
    private val systemMap: MutableMap<Class<out System>, System> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <E : System> get(systemClass: Class<E>): E {
        return systemMap[systemClass] as E?
            ?: throw IllegalStateException("No system of type $systemClass registered!")
    }

    fun initialize(currentTime: Double) {
        systems.forEach { system ->
            system.initialize(currentTime)
        }
    }

    fun step(currentTime: Double, baseStepSize: Double): Double {
        if (groups.isEmpty()) return baseStepSize

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
        engine.safeContext { internalAdd(system) }
    }

    private fun internalAdd(system: System) {
        val systems = groups.computeIfAbsent(system.decimationFactor) {
            mutableListOf()
        }
        systems.add(system)
        systemMap[system::class.java] = system
        system.addedToEngine(engine)
    }

    fun remove(system: Class<out System>) {
        engine.safeContext { internalRemove(system) }
    }

    private fun internalRemove(system: Class<out System>) {
        systemMap.remove(system)?.also {
            groups[it.decimationFactor]?.remove(it)
        }
    }

    override fun close() {
        systems.forEach { it.close() }
    }

}
