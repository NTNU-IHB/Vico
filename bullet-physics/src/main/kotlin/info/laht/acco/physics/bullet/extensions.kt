package info.laht.acco.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape
import com.badlogic.gdx.physics.bullet.collision.btSphereShape
import info.laht.acco.physics.ColliderComponent
import info.laht.acco.render.shape.BoxShape
import info.laht.acco.render.shape.PlaneShape
import info.laht.acco.render.shape.SphereShape
import org.joml.Matrix4d
import org.joml.Matrix4dc

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

fun Matrix4dc.convert(): Matrix4 {
    return Matrix4(
        floatArrayOf(
            m00().toFloat(), m01().toFloat(), m02().toFloat(), m03().toFloat(),
            m10().toFloat(), m11().toFloat(), m12().toFloat(), m13().toFloat(),
            m20().toFloat(), m21().toFloat(), m22().toFloat(), m23().toFloat(),
            m30().toFloat(), m31().toFloat(), m32().toFloat(), m33().toFloat()
        )
    )
}

fun Matrix4.convert(store: Matrix4d = Matrix4d()): Matrix4d {
    return store.set(
        values[0].toDouble(), values[1].toDouble(), values[2].toDouble(), values[3].toDouble(),
        values[4].toDouble(), values[5].toDouble(), values[6].toDouble(), values[7].toDouble(),
        values[8].toDouble(), values[9].toDouble(), values[10].toDouble(), values[11].toDouble(),
        values[12].toDouble(), values[13].toDouble(), values[14].toDouble(), values[15].toDouble()
    )
}
