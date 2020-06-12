package no.ntnu.ihb.acco.core

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FamilyTest {

    class ComponentA : Component
    class ComponentB : Component
    class ComponentC : Component
    class ComponentD : Component
    class ComponentE : Component

    @Test
    fun testFamily() {

        Entity("").apply {
            addComponent(ComponentA())
            addComponent(ComponentB())
            assertTrue(Family.all(ComponentA::class.java).build().test(this))
            assertTrue(Family.all(ComponentA::class.java, ComponentB::class.java).build().test(this))
            assertFalse(Family.all(ComponentA::class.java, ComponentC::class.java).build().test(this))
            assertFalse(Family.exclude(ComponentA::class.java).build().test(this))
            assertTrue(Family.all().build().test(this))
        }

        Entity("").apply {
            addComponent(ComponentA())
            addComponent(ComponentB())
            assertTrue(Family.one(ComponentA::class.java, ComponentB::class.java).build().test(this))
            assertFalse(Family.one(ComponentC::class.java, ComponentD::class.java).build().test(this))
        }

        Entity("").apply {
            addComponent(ComponentA())
            addComponent(ComponentB())
            assertTrue(Family.exclude(ComponentC::class.java, ComponentD::class.java).build().test(this))
            assertTrue(
                Family.exclude(ComponentC::class.java, ComponentD::class.java)
                    .all(ComponentA::class.java, ComponentB::class.java).build().test(this)
            )
            assertTrue(
                Family.exclude(ComponentC::class.java, ComponentD::class.java)
                    .one(ComponentE::class.java, ComponentB::class.java).build().test(this)
            )
            assertFalse(
                Family.exclude(ComponentC::class.java, ComponentD::class.java).one(ComponentE::class.java).build()
                    .test(this)
            )
        }

        Entity("").apply {
            assertTrue(Family.exclude(ComponentA::class.java).build().test(this))
            assertTrue(Family.exclude(ComponentA::class.java, ComponentB::class.java).build().test(this))
            assertTrue(
                Family.exclude(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java).build()
                    .test(this)
            )
            assertFalse(
                Family.exclude(ComponentA::class.java, ComponentB::class.java, ComponentC::class.java)
                    .one(ComponentD::class.java).build().test(this)
            )
        }

    }

}
