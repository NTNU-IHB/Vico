package no.ntnu.ihb.vico.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class EventDispatcherImplTest {

    @Test
    fun testEventDispatching() {

        val type = "hello"
        val target = ", world!"
        val dispatcher = EventDispatcherImpl()

        var eventReceived = false
        dispatcher.addEventListener(type) {
            eventReceived = true

            assertEquals(type, it.type)
            assertEquals(target, it.target<String>())
        }

        dispatcher.dispatchEvent(type, target)
        assertTrue(eventReceived)

    }

}
