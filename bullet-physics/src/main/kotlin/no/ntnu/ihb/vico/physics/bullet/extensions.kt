package no.ntnu.ihb.vico.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.collision.btCylinderShapeZ
import com.badlogic.gdx.physics.bullet.collision.btSphereShape
import info.laht.krender.mesh.BoxShape
import info.laht.krender.mesh.CylinderShape
import info.laht.krender.mesh.PlaneShape
import info.laht.krender.mesh.SphereShape
import no.ntnu.ihb.vico.physics.Collider
import org.joml.*


fun Collider.convert(): btCollisionShape {
    return when (val shape = shape) {
        is BoxShape -> {
            btBoxShape(Vector3(shape.width, shape.height, shape.depth).scl(0.5f))
        }
        is PlaneShape -> {
            btBoxShape(Vector3(shape.height, shape.height, 0.5f))
        }
        is SphereShape -> {
            btSphereShape(shape.radius)
        }
        is CylinderShape -> {
            btCylinderShapeZ(Vector3(shape.radius, shape.radius, shape.height * 0.5f))
        }
        /*is CapsuleShape -> {
            btCapsuleShapeZ(shape.radius, shape.height * 0.5f)
        }*/
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
