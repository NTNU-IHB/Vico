package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EntityTest {

    internal class ComponentA : Component

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

}
