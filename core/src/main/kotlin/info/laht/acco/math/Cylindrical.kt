package info.laht.acco.math

import kotlin.math.atan2
import kotlin.math.sqrt

data class Cylindrical(
    var radius: Double = 1.0,
    var theta: Double = 0.0,
    var y: Double = 0.0
) : Cloneable {

    fun set(radius: Double, theta: Double, y: Double): Cylindrical {
        this.radius = radius
        this.theta = theta
        this.y = y

        return this
    }

    fun setFromVector3(v: Vector3): Cylindrical {

        return this.setFromCartesianCoords(v.x, v.y, v.z)

    }

    fun setFromCartesianCoords(x: Double, y: Double, z: Double): Cylindrical {

        this.radius = sqrt(x * x + z * z)
        this.theta = atan2(x, z)
        this.y = y

        return this

    }

    override fun clone() = copy()

    fun copy(source: Cylindrical): Cylindrical {
        return set(source.radius, source.theta, source.y)
    }

}
