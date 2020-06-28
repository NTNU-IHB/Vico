package no.ntnu.ihb.acco.input

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InputManagerTest {

    @Test
    fun testInputManager() {

        val inputManager = InputManager()

        assertFalse(inputManager.keyPressed(KeyStroke.KEY_0))
        assertFalse(inputManager.keyPressed(KeyStroke.KEY_1, KeyStroke.KEY_2))

        inputManager.registerKeyPress(KeyStroke.KEY_2)
        assertTrue(inputManager.keyPressed(KeyStroke.KEY_2))

    }

}
