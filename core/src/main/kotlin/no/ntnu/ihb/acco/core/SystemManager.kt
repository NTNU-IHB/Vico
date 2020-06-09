package no.ntnu.ihb.acco.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*


class SystemManager(
    private val engine: Engine
) : Closeable {

    val systems = mutableListOf<BaseSystem>()
    private val systemMap: MutableMap<Class<out BaseSystem>, BaseSystem> = mutableMapOf()
    private val manipulators = TreeMap<Long, MutableList<ManipulationSystem>>(Comparator { o1, o2 -> o2.compareTo(o1) })

    @Suppress("UNCHECKED_CAST")
    fun <E : SimulationSystem> get(systemClass: Class<E>): E {
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
        if (manipulators.isEmpty()) return baseStepSize

        val largestSystemStepSize = baseStepSize * manipulators.lastKey()
        val endOfStepTime = currentTime + largestSystemStepSize
        manipulators.forEach { (decimationFactor, systemGroup) ->
            systemGroup.forEach { system ->
                if (system.enabled) {
                    var t = currentTime
                    val dt = baseStepSize * decimationFactor
                    do {
                        when (system) {
                            is ObserverSystem -> system.internalObserve(currentTime)
                            is SimulationSystem -> system.internalStep(currentTime, dt)
                        }
                        t += dt
                    } while (t < endOfStepTime)
                }
            }
        }

        return largestSystemStepSize
    }

    fun add(system: ManipulationSystem) {
        engine.safeContext { internalAdd(system) }
    }

    fun add(system: EventSystem) {
        engine.safeContext { internalAdd(system) }
    }

    private fun internalAdd(system: BaseSystem) {

        when (system) {
            is EventSystem -> Unit
            is ManipulationSystem -> {
                manipulators.computeIfAbsent(system.decimationFactor) {
                    mutableListOf()
                }.add(system)
            }
        }

        systems.add(system)
        systems.sort()
        systemMap[system::class.java] = system
        system.addedToEngine(engine)
    }

    fun remove(system: Class<out BaseSystem>) {
        engine.safeContext { internalRemove(system) }
    }

    private fun internalRemove(systemClass: Class<out BaseSystem>) {
        systemMap.remove(systemClass)?.also { system ->
            when (system) {
                is EventSystem -> Unit
                is ManipulationSystem -> manipulators[system.decimationFactor]?.remove(system)
            }
            systems.remove(system)
        }
    }

    override fun close() {
        systems.forEach { it.close() }
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SystemManager::class.java)
    }

}
