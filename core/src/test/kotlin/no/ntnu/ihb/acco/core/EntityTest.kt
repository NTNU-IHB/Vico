package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EntityTest {

    private class ComponentA : Component()
    private class ComponentB : Component()

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

            addComponent<ComponentB>()
            Assertions.assertDoesNotThrow {
                getComponent<ComponentB>()
            }
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                addComponent<ComponentB>()
            }
        }

    }

}
