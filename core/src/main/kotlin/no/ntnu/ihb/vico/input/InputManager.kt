package no.ntnu.ihb.vico.input

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface InputAccess {

    fun isKeyPressed(key: String, vararg additionalKeys: String): Boolean
    fun registerKeyPress(key: String)

}

class InputManager : InputAccess {

    private val keyStrokes: MutableSet<String> = mutableSetOf()

    override fun isKeyPressed(key: String, vararg additionalKeys: String): Boolean {
        for (stroke in additionalKeys) {
            if (stroke !in keyStrokes) {
                return false
            }
        }
        return key in keyStrokes
    }

    override fun registerKeyPress(key: String) {
        keyStrokes.add(key)
        LOG.trace("Key $key registered")
    }

    internal fun clear() {
        keyStrokes.clear()
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(InputManager::class.java)
    }

}
