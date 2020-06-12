package no.ntnu.ihb.acco.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.collision.btSphereShape
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.render.shape.BoxShape
import no.ntnu.ihb.acco.render.shape.PlaneShape
import no.ntnu.ihb.acco.render.shape.SphereShape
import org.joml.*

fun ColliderComponent.convert(): btCollisionShape {
    return when (val shape = shape) {
        is BoxShape -> {
            btBoxShape(Vector3(shape.width, shape.height, shape.depth))
        }
        is PlaneShape -> {
            btBoxShape(Vector3(shape.height, shape.height, 0.1f))
        }
        is SphereShape -> {
            btSphereShape(shape.radius)
        }
        else -> TODO()
    }
}

fun Vector3d.set(v: Vector3) = apply {
    x = v.x.toDouble()
    y = v.y.toDouble()
    z = v.z.toDouble()
}

fun Vector3.convert(): Vector3dc {
    return Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
}

fun Quaternion.convert(): Quaterniond {
    return Quaterniond(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
}

fun Matrix4.copy(m: Matrix4dc) = apply {
    m.get(this.values)
}

fun Matrix4d.copy(m: Matrix4) = apply {
    set(m.values)
}
