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
            btBoxShape(Vector3(shape.width.toFloat(), shape.height.toFloat(), shape.depth.toFloat()).scl(0.5f))
        }
        is PlaneShape -> {
            val depth = 0.5f
            btBoxShape(Vector3(shape.height.toFloat(), shape.height.toFloat(), depth))
        }
        is SphereShape -> {
            btSphereShape(shape.radius.toFloat())
        }
        is CylinderShape -> {
            btCylinderShapeZ(Vector3(shape.radius.toFloat(), shape.radius.toFloat(), shape.height.toFloat() * 0.5f))
        }
        is CapsuleShape -> {
            btCapsuleShapeZ(shape.radius.toFloat(), shape.height.toFloat() * 0.5f)
        }
        else -> TODO("Unsupported shape: $shape")
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
