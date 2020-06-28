package no.ntnu.ihb.acco.input

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface InputAccess {

    fun isKeyPressed(keyStroke: KeyStroke, vararg additionalKeyStrokes: KeyStroke): Boolean
    fun registerKeyPress(keyStroke: KeyStroke)

}

class InputManager : InputAccess {

    private val keyStrokes: MutableSet<KeyStroke> = mutableSetOf()

    override fun isKeyPressed(keyStroke: KeyStroke, vararg additionalKeyStrokes: KeyStroke): Boolean {
        for (stroke in additionalKeyStrokes) {
            if (stroke !in keyStrokes) {
                return false
            }
        }
        return keyStroke in keyStrokes
    }

    override fun registerKeyPress(keyStroke: KeyStroke) {
        keyStrokes.add(keyStroke)
        LOG.trace("Key $keyStroke registered")
    }

    internal fun clear() {
        keyStrokes.clear()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(InputManager::class.java)
    }

}
