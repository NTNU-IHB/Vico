package no.ntnu.ihb.acco.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ObservableSetTest {

    @Test
    fun testAdd() {

        val set = ObservableSet(mutableSetOf<Int>())

        var observed = false
        set.addObserver = {
            observed = true
        }

        set.add(5)

        assertTrue(observed)

    }

    @Test
    fun testRemove() {

        val set = ObservableSet(mutableSetOf(1, 2, 3))

        var observed = false
        set.removeObserver = {
            observed = true
        }

        set.remove(1)

        assertTrue(observed)

    }

}
