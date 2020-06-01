package no.ntnu.ihb.acco.math

import org.joml.Matrix3d
import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.Vector3dc

data class Plane @JvmOverloads constructor(
    val normal: Vector3d = Vector3d(0.0, 0.0, 1.0),
    var constant: Double = 0.0
) : Cloneable {

    fun set(normal: Vector3dc, constant: Double): Plane {
        this.normal.set(normal)
        this.constant = constant

        return this
    }

    fun setComponents(x: Double, y: Double, z: Double, w: Double): Plane {
        this.normal.set(x, y, z)
        this.constant = w

        return this
    }

    fun setFromNormalAndCoplanarPoint(normal: Vector3dc, point: Vector3dc): Plane {
        this.normal.set(normal)
        this.constant = -point.dot(this.normal)

        return this
    }

    fun setFromCoplanarPoints(a: Vector3d, b: Vector3dc, c: Vector3dc): Plane {

        val v1 = Vector3d()
        val v2 = Vector3d()

        val normal = c.sub(b, v1).cross(a.sub(b, v2)).normalize()

        // Q: should an error be thrown if normal is zero (e.g. degenerate plane)?

        this.setFromNormalAndCoplanarPoint(normal, a)

        return this
    }

    override fun clone(): Plane {
        return Plane().copy(this)
    }

    fun copy(plane: Plane): Plane {
        this.normal.set(plane.normal)
        this.constant = plane.constant

        return this
    }

    fun normalize(): Plane {
        val inverseNormalLength = 1.0 / this.normal.length()
        this.normal.mul(inverseNormalLength)
        this.constant *= inverseNormalLength

        return this
    }

    fun negate(): Plane {
        this.constant *= -1
        this.normal.negate()

        return this
    }

    fun distanceToPoint(point: Vector3dc): Double {
        return this.normal.dot(point) + this.constant
    }

    fun distanceToSphere(sphere: Sphere): Double {
        return this.distanceToPoint(sphere.center) - sphere.radius
    }

    @JvmOverloads
    fun projectPoint(point: Vector3dc, target: Vector3d = Vector3d()): Vector3d {
        return target.set(this.normal).mul(-this.distanceToPoint(point)).add(point)
    }

    @JvmOverloads
    fun intersectLine(line: Line3, target: Vector3d = Vector3d()): Vector3d? {
        val v1 = Vector3d()

        val direction = line.delta(v1)

        val denominator = this.normal.dot(direction)

        if (denominator == 0.0) {

            // line is coplanar, return origin
            if (this.distanceToPoint(line.start) == 0.0) {

                return target.set(line.start)

            }

            // Unsure if this is the correct method to handle this case.
            return null

        }

        val t = -(line.start.dot(this.normal) + this.constant) / denominator

        if (t < 0 || t > 1) {

            return null

        }

        return target.set(direction).mul(t).add(line.start)

    }

    fun intersectsLine(line: Line3): Boolean {
        // Note: this tests if a line intersects the plane, not whether it (or its end-points) are coplanar with it.

        val startSign = this.distanceToPoint(line.start)
        val endSign = this.distanceToPoint(line.end)

        return (startSign < 0 && endSign > 0) || (endSign < 0 && startSign > 0)
    }

    fun intersectsBox(box: Box3): Boolean {
        return box.intersectsPlane(this)
    }

    fun intersectsSphere(sphere: Sphere): Boolean {
        return sphere.intersectsPlane(this)
    }

    @JvmOverloads
    fun coplanarPoint(target: Vector3d = Vector3d()): Vector3d {
        return target.set(this.normal).mul(-this.constant)
    }

    @JvmOverloads
    fun applyMatrix4(matrix: Matrix4dc, optionalNormalMatrix: Matrix3d? = null): Plane {

        val v1 = Vector3d()
        val normalMatrix = optionalNormalMatrix ?: matrix.getNormalMatrix()

        val referencePoint = this.coplanarPoint(v1).mulPosition(matrix)

        val normal = this.normal.mul(normalMatrix).normalize()

        this.constant = -referencePoint.dot(normal)

        return this
    }

    fun translate(offset: Vector3d): Plane {
        this.constant -= offset.dot(this.normal)

        return this
    }

}
