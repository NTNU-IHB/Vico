package no.ntnu.ihb.vico.math

import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.Vector3dc
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

data class Sphere @JvmOverloads constructor(
    val center: Vector3d = Vector3d(),
    var radius: Double = 0.0
) : Cloneable {

    fun set(center: Vector3d, radius: Double): Sphere {
        this.center.set(center)
        this.radius = radius
        return this
    }

    @JvmOverloads
    fun setFromPoints(points: List<Vector3d>, optionalCenter: Vector3d? = null): Sphere {

        val box = Box3()

        val center = this.center

        if (optionalCenter != null) {
            center.set(optionalCenter)
        } else {
            box.setFromPoints(points).getCenter(center)
        }

        var maxRadiusSq = 0.0

        points.forEach { point ->

            maxRadiusSq = max(maxRadiusSq, center.distanceSquared(point))

        }

        this.radius = sqrt(maxRadiusSq)

        return this

    }

    fun empty(): Boolean {
        return (this.radius <= 0)
    }

    fun containsPoint(point: Vector3d): Boolean {
        return (point.distanceSquared(this.center) <= (this.radius * this.radius))
    }

    fun distanceToPoint(point: Vector3d): Double {
        return (point.distance(this.center) - this.radius)
    }

    fun intersectsSphere(sphere: Sphere): Boolean {
        val radiusSum = this.radius + sphere.radius

        return sphere.center.distanceSquared(this.center) <= (radiusSum * radiusSum)
    }

    fun intersectsBox(box: Box3): Boolean {
        return box.intersectsSphere(this)
    }

    fun intersectsPlane(plane: Plane): Boolean {
        return abs(plane.distanceToPoint(this.center)) <= this.radius
    }

    fun clampPoint(point: Vector3d, target: Vector3d = Vector3d()): Vector3d {
        val deltaLengthSq = this.center.distanceSquared(point)

        target.set(point)

        if (deltaLengthSq > (this.radius * this.radius)) {

            target.sub(this.center).normalize()
            target.mul(this.radius).add(this.center)

        }

        return target

    }

    @JvmOverloads
    fun getBoundingBox(target: Box3 = Box3()): Box3 {
        target.set(this.center, this.center)
        target.expandByScalar(this.radius)

        return target
    }

    fun applyMatrix4(matrix: Matrix4dc): Sphere {
        this.center.mulPosition(matrix)
        this.radius = this.radius * matrix.getMaxScaleOnAxis()

        return this
    }

    fun translate(offset: Vector3dc): Sphere {
        this.center.add(offset)

        return this
    }

    public override fun clone(): Sphere {
        return Sphere().copy(this)
    }

    fun copy(sphere: Sphere): Sphere {
        this.center.set(sphere.center)
        this.radius = sphere.radius
        return this
    }

    override fun toString(): String {
        return "Sphere(center=$center, radius=$radius)"
    }

}
