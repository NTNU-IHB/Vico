package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ComponentClazzTest {

    open class ComponentA : Component
    class ComponentB : ComponentA()

    @Test
    fun testEquals() {

        val a = ComponentA()
        val b = ComponentB()

        val ca = a.javaClass
        val cb = b.javaClass

        assertFalse(cb == ca)

        val caz = ComponentClazz(ca)
        val cbz = ComponentClazz(cb)

        assertTrue(caz == cbz)
        assertFalse(cbz == caz)

        val map1: MutableMap<ComponentClazz, Component> = mutableMapOf(
            caz to a
        )

        assertFalse(cbz in map1)

        val map2: MutableMap<ComponentClazz, Component> = mutableMapOf(
            cbz to b
        )

        assertTrue(caz in map2)

        val list = listOf(cbz)
        assertTrue(list.containsAll(listOf(ComponentClazz(ComponentA::class.java))))

    }
}
