package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EntityTest {

    internal class ComponentA : Component()

    @Test
    fun testAddComponent() {

        Entity().apply {
            addComponent(ComponentA())
            Assertions.assertDoesNotThrow {
                getComponent<ComponentA>()
            }
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                addComponent(ComponentA())
            }
        }

    }

    @Test
    fun testAddEntity() {

        val e1 = Entity("e1")
        val e2 = Entity("e2")
        val e3 = Entity("e3")

        e1.addEntity(e2)
        Assertions.assertEquals("${e1.originalName}.${e2.originalName}", e2.name)

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            e2.addEntity(e2)
        }

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            e1.addEntity(e2)
        }

        e2.addEntity(e3)
        Assertions.assertEquals("${e1.originalName}.${e2.originalName}.${e3.originalName}", e3.name)

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            e1.addEntity(e3)
        }

        e2.removeEntity(e3)
        Assertions.assertEquals(e3.originalName, e3.name)

    }

}
