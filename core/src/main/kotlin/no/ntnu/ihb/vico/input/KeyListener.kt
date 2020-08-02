package no.ntnu.ihb.vico.input

import org.joml.Vector3dc

fun interface KeyListener {

    fun onKeyPressed(key: KeyStroke)

}

fun interface ClickListener {

    fun onMousePressed(pos: Vector3dc, dir: Vector3dc)

}
