package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SystemManagerTest {

    class ComponentA : Component

    companion object {
        val family = Family.all(ComponentA::class.java).build()
    }

    abstract class TestSystem(
        family: Family
    ) : SimulationSystem(family) {
        override fun entityAdded(entity: Entity) {
            TODO("Not yet implemented")
        }

        override fun entityRemoved(entity: Entity) {
            TODO("Not yet implemented")
        }
    }

    class SystemA : TestSystem(family) {

        init {
            priority = 1
        }

        override fun step(currentTime: Double, stepSize: Double) {
            println("A")
        }
    }

    class SystemB : TestSystem(family) {

        init {
            priority = 2
        }

        override fun step(currentTime: Double, stepSize: Double) {
            println("B")
        }
    }

    class SystemC : TestSystem(family) {

        init {
            priority = 3
        }

        override fun step(currentTime: Double, stepSize: Double) {
            println("C")
        }
    }

    @Test
    operator fun iterator() {
        Engine().use { engine ->
            val manager = engine.systemManager

            val systems = listOf(
                SystemA(), SystemB(), SystemC()
            )
            systems.forEach { engine.addSystem(it) }

            assertEquals(3, manager.systems.size)
            assertEquals(systems, manager.systems.sorted())

            engine.step(1)

        }
    }
}
