package no.ntnu.ihb.acco.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*


class SystemManager(
    private val engine: Engine
) : Closeable {

    val systems: List<System>
        get() = groups.flatMap { it.value }

    private val groups = TreeMap<Long, MutableList<System>>(Comparator { o1, o2 -> o2.compareTo(o1) })
    private val systemMap: MutableMap<Class<out System>, System> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <E : System> get(systemClass: Class<E>): E {
        return systemMap[systemClass] as E?
            ?: throw IllegalStateException("No system of type $systemClass registered!")
    }

    fun initialize(currentTime: Double) {
        systems.forEach { system ->
            LOG.debug("Initializing system ${system::class.java}")
            system.initialize(currentTime)
        }
    }

    fun step(currentTime: Double, baseStepSize: Double): Double {
        if (groups.isEmpty()) return baseStepSize

        val largestSystemStepSize = baseStepSize * groups.lastKey()
        val endOfStepTime = currentTime + largestSystemStepSize
        groups.forEach { (decimationFactor, systemGroup) ->
            systemGroup.forEach { system ->
                if (system.enabled) {

                    var t = currentTime
                    val dt = baseStepSize * decimationFactor
                    do {
                        system.step(t, dt)
                        t += dt
                    } while (t < endOfStepTime)
                }
            }
        }

        return largestSystemStepSize
    }

    fun add(system: System) {
        engine.safeContext { internalAdd(system) }
    }

    private fun internalAdd(system: System) {
        groups.computeIfAbsent(system.decimationFactor) {
            mutableListOf()
        }.add(system)
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

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SystemManager::class.java)
    }

}
