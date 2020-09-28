package info.laht.krender.listeners

import org.joml.Vector3dc

fun interface CloseListener {
    fun onClose()
}

fun interface KeyListener {
    fun onKeyPressed(keyCode: Int)
}

fun interface MouseClickListener {
    fun onMouseClicked(pos: Vector3dc, dir: Vector3dc)
}
