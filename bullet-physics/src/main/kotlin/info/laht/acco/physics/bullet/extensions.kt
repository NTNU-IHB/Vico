package info.laht.acco.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.collision.btSphereShape
import info.laht.acco.physics.ColliderComponent
import info.laht.acco.render.shape.BoxShape
import info.laht.acco.render.shape.PlaneShape
import info.laht.acco.render.shape.SphereShape

fun ColliderComponent.convert(): btCollisionShape {
    return when (val shape = shape) {
        is BoxShape -> {
            btBoxShape(Vector3(shape.height, shape.height, shape.depth))
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

fun Vector3.convert(): info.laht.acco.math.Vector3 {
    return info.laht.acco.math.Vector3(x.toDouble(), y.toDouble(), z.toDouble())
}

fun Quaternion.convert(): info.laht.acco.math.Quaternion {
    return info.laht.acco.math.Quaternion(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
}

fun Matrix4.copy(m: info.laht.acco.math.Matrix4): Matrix4 {
    val te = this.values
    val me = m.elements

    te[0] = me[0].toFloat(); te[1] = me[1].toFloat(); te[2] = me[2].toFloat(); te[3] = me[3].toFloat()
    te[4] = me[4].toFloat(); te[5] = me[5].toFloat(); te[6] = me[6].toFloat(); te[7] = me[7].toFloat()
    te[8] = me[8].toFloat(); te[9] = me[9].toFloat(); te[10] = me[10].toFloat(); te[11] = me[11].toFloat()
    te[12] = me[12].toFloat(); te[13] = me[13].toFloat(); te[14] = me[14].toFloat(); te[15] = me[15].toFloat()

    return this
}

fun info.laht.acco.math.Matrix4.copy(m: Matrix4): info.laht.acco.math.Matrix4 {
    val te = this.elements
    val me = m.values

    te[0] = me[0].toDouble(); te[1] = me[1].toDouble(); te[2] = me[2].toDouble(); te[3] = me[3].toDouble()
    te[4] = me[4].toDouble(); te[5] = me[5].toDouble(); te[6] = me[6].toDouble(); te[7] = me[7].toDouble()
    te[8] = me[8].toDouble(); te[9] = me[9].toDouble(); te[10] = me[10].toDouble(); te[11] = me[11].toDouble()
    te[12] = me[12].toDouble(); te[13] = me[13].toDouble(); te[14] = me[14].toDouble(); te[15] = me[15].toDouble()

    return this
}
