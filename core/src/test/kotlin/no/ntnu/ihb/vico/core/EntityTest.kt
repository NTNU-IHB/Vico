package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EntityTest {

    private class ComponentA : Component() {
        private val value = "Hello"

        init {
            registerProperties(
                StrLambdaProperty(VALUE_PROP_NAME, 1,
                    getter = { it[0] = value })
            )
        }

        companion object {
            const val VALUE_PROP_NAME = "value"
        }
    }

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

    @Test
    fun testPropertyAccess() {
        val e = Entity().apply {
            addComponent(ComponentA())
        }

        Assertions.assertNotNull(e.getPropertyOrNull(ComponentA.VALUE_PROP_NAME))

        val valueProp = e.getStringProperty(ComponentA.VALUE_PROP_NAME)
        Assertions.assertEquals("Hello", valueProp.read().first())

        Assertions.assertThrows(NoSuchElementException::class.java) {
            e.getIntegerProperty(ComponentA.VALUE_PROP_NAME)
        }
        Assertions.assertThrows(NoSuchElementException::class.java) {
            e.getRealProperty(ComponentA.VALUE_PROP_NAME)
        }
        Assertions.assertThrows(NoSuchElementException::class.java) {
            e.getBooleanProperty(ComponentA.VALUE_PROP_NAME)
        }

    }

}
