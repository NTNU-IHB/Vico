package no.ntnu.ihb.vico.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable


class SystemManager internal constructor() : Closeable {

    val systems: MutableList<BaseSystem> = mutableListOf()
    private val systemMap: MutableMap<Class<out BaseSystem>, BaseSystem> = mutableMapOf()
    private val manipulators: MutableList<ManipulationSystem> = mutableListOf()

    fun <E : SimulationSystem> hasSystem(systemClass: Class<E>): Boolean {
        return getSystemOrNull(systemClass) != null
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : SimulationSystem> getSystemOrNull(systemClass: Class<E>): E? {
        return systemMap[systemClass] as E?
    }

    fun <E : SimulationSystem> getSystem(systemClass: Class<E>): E {
        val registeredSystems by lazy {
            systems.map { it.javaClass }
        }
        return getSystemOrNull(systemClass)
            ?: throw NoSuchElementException(
                "No system of type $systemClass could be located! " +
                        "The following systems are currently registered: $registeredSystems"
            )
    }

    fun initialize(currentTime: Double) {
        systems.forEach { system ->
            LOG.debug("Initializing system ${system::class.java}")
            system.initialize(currentTime)
        }
        systems.forEach { system ->
            system.postInit()
        }
    }

    fun step(iterations: Long, currentTime: Double, engineStepSize: Double) {

        for (system in manipulators) {
            if (system.enabled) {
                if (iterations % system.decimationFactor == 0L) {
                    when (system) {
                        is ObserverSystem -> {
                            system.observe(currentTime)
                        }
                        is SimulationSystem -> {
                            val systemStepSize = engineStepSize * system.decimationFactor
                            system.step(currentTime, systemStepSize)
                        }
                    }
                }
            }
        }

    }

    fun addSystem(system: BaseSystem) {
        if (system is ManipulationSystem) {
            manipulators.add(system)
        }
        systems.add(system)
        systems.sort()
        systemMap[system::class.java] = system
        LOG.info("Added system ${system.javaClass}..")
    }

    fun removeSystem(systemClass: Class<out BaseSystem>) {
        systemMap.remove(systemClass)?.also { system ->
            when (system) {
                is EventSystem -> Unit
                is ManipulationSystem -> manipulators.remove(system)
            }
            systems.remove(system)
            LOG.info("Removed system ${system.javaClass}..")
        }
    }

    override fun close() {
        systems.forEach { system ->
            LOG.debug("Closing system ${system::class.java}")
            system.close()
        }
    }

    private companion object {
        private val LOG: Logger = LoggerFactory.getLogger(SystemManager::class.java)
    }

}
