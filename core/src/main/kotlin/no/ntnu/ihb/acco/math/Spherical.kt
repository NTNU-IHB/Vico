package no.ntnu.ihb.acco.math

import org.joml.Vector3dc
import kotlin.math.*

private const val EPS = 0.000001

data class Spherical(
    var radius: Double = 1.0,
    var phi: Double = 0.0,
    var theta: Double = 0.0
) {

    fun set(radius: Double, phi: Double, theta: Double): Spherical {
        this.radius = radius
        this.phi = phi
        this.theta = theta

        return this
    }

    fun copy(source: Spherical): Spherical {
        return set(source.radius, source.phi, source.theta)
    }

    fun makeSafe(): Spherical {

        this.phi = max(EPS, min(PI - EPS, this.phi))

        return this
    }

    fun setFromVector3(v: Vector3dc): Spherical {

        return this.setFromCartesianCoords(v.x(), v.y(), v.z())

    }

    fun setFromCartesianCoords(x: Double, y: Double, z: Double): Spherical {

        this.radius = sqrt(x * x + y * y + z * z)

        if (this.radius == 0.0) {

            this.theta = 0.0
            this.phi = 0.0

        } else {

            this.theta = atan2(x, z)
            this.phi = acos(clamp(y / this.radius, -1.0, 1.0))

        }

        return this

    }

}
