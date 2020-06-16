package no.ntnu.ihb.acco.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ObservableSetTest {

    @Test
    fun testObservableSet() {

        val set = ObservableSet(mutableSetOf<Int>())

        var addObserved = false
        var removeObserved = false

        set.observer = object : SetObserver<Int> {
            override fun onElementAdded(element: Int) {
                addObserved = true
            }

            override fun onElementRemoved(element: Int) {
                removeObserved = true
            }
        }

        set.add(5)
        assertTrue(addObserved)

        set.remove(5)
        assertTrue(removeObserved)

    }

}
