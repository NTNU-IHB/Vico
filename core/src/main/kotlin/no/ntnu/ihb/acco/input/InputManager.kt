package no.ntnu.ihb.acco.input

class InputManager {

    private val keyStrokes: MutableSet<KeyStroke> = mutableSetOf()

    fun keyPressed(keyStroke: KeyStroke, vararg additionalKeyStrokes: KeyStroke): Boolean {
        for (stroke in additionalKeyStrokes) {
            if (stroke !in keyStrokes) {
                return false
            }
        }
        return keyStroke in keyStrokes
    }

    fun registerKeyPress(keyStroke: KeyStroke) {
        keyStrokes.add(keyStroke)
        println("Key $keyStroke registered")
    }

    internal fun clear() {
        keyStrokes.clear()
    }

}
