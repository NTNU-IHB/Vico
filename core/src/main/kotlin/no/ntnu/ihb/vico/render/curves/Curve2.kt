package no.ntnu.ihb.vico.render.curves

import org.joml.Vector2d

abstract class Curve2 {
    abstract fun getPoint(t: Double): Vector2d
}
