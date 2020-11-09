package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class EntityTest {

    private class ComponentA : AbstractComponent() {

        private val value = "Hello"

        init {
            properties.registerProperties(
                    StrScalarProperty(VALUE_PROP_NAME,
                            getter = { value })
            )
        }

        companion object {
            const val VALUE_PROP_NAME = "value"
        }
    }

    private class ComponentB : Component

    @Test
    fun testAddComponent() {

        Entity().apply {
            add(ComponentA())
            Assertions.assertDoesNotThrow {
                get<ComponentA>()
            }
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                add(ComponentA())
            }

            add<ComponentB>()
            Assertions.assertDoesNotThrow {
                get<ComponentB>()
            }
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                add<ComponentB>()
            }
        }

    }

    @Test
    fun testPropertyAccess() {
        val e = Entity().apply {
            add(ComponentA())
        }

        Assertions.assertNotNull(e.getPropertyOrNull(ComponentA.VALUE_PROP_NAME))

        val valueProp = e.getStringProperty(ComponentA.VALUE_PROP_NAME)
        Assertions.assertEquals("Hello", valueProp.read())

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
