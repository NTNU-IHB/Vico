package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


internal class EntityManagerTest {

    @Test
    fun getByName() {
        Engine().use { engine ->
            val e1 = engine.createEntity("entity1")
            val e2 = engine.createEntity("entity2")
            val e11 = engine.createEntity("entity1")

            assertSame(e1, engine.getEntityByName(e1.name))
            assertSame(e2, engine.getEntityByName(e2.name))
            assertSame(e11, engine.getEntityByName("${e1.name}(1)"))
            assertThrows<NoSuchElementException> { engine.getEntityByName("") }
        }
    }

    @Test
    fun getByTag() {
        Engine().use { engine ->
            val tag = "aTag"
            val e1 = engine.createEntity("entity1").apply { this.tag = tag }
            val e2 = engine.createEntity("entity2").apply { this.tag = tag }

            assertEquals(listOf(e1, e2), engine.getEntitiesByTag(tag))
            assertTrue(engine.getEntitiesByTag("").isEmpty())
        }
    }

    @Test
    fun getFor() {

        class ComponentA : Component
        class ComponentB : Component
        class ComponentC : Component

        Engine().use { engine ->

            Family.one(ComponentA::class.java, ComponentB::class.java).build().also { family ->
                engine.createEntity("e1", ComponentA())
                assertEquals(1, engine.getEntitiesFor(family).size)

                engine.createEntity("e2", ComponentB())
                assertEquals(2, engine.getEntitiesFor(family).size)

            }
            Family.all(ComponentC::class.java).build().also { family ->
                val entities = engine.getEntitiesFor(family)
                assertTrue(entities.isEmpty())
                engine.createEntity("e1").also { e ->
                    e.addComponent(ComponentC())
                    assertTrue(entities.isNotEmpty())
                    engine.removeEntity(e)
                    assertTrue(entities.isEmpty())
                }

            }

        }

    }

}
