package no.ntnu.ihb.acco.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.*
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.render.shape.*
import org.joml.*


fun ColliderComponent.convert(): btCollisionShape {
    return when (val shape = shape) {
        is BoxShape -> {
            btBoxShape(Vector3(shape.width, shape.height, shape.depth).scl(0.5f))
        }
        is PlaneShape -> {
            val depth = 0.5f
            btBoxShape(Vector3(shape.height, shape.height, depth))
        }
        is SphereShape -> {
            btSphereShape(shape.radius)
        }
        is CylinderShape -> {
            btCylinderShapeZ(Vector3(shape.radius, shape.radius, shape.height * 0.5f))
        }
        is CapsuleShape -> {
            btCapsuleShapeZ(shape.radius, shape.height * 0.5f)
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
