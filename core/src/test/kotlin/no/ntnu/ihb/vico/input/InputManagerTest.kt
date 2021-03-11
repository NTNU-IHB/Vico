package no.ntnu.ihb.vico.input

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InputManagerTest {

    @Test
    fun testInputManager() {

        val inputManager = InputManager()

        assertFalse(inputManager.isKeyPressed("0"))
        assertFalse(inputManager.isKeyPressed("1", "2"))

        inputManager.registerKeyPress("2")
        assertTrue(inputManager.isKeyPressed("2"))

    }

}
