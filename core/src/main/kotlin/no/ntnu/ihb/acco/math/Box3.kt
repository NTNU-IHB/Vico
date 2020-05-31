package no.ntnu.ihb.acco.math

import org.joml.Matrix4dc
import org.joml.Vector3d
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Box3 @JvmOverloads constructor(
    var min: Vector3d = Vector3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
    var max: Vector3d = Vector3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
) : Cloneable {

    private val intersectsTriangleHelper by lazy { IntersectsTriangleHelper() }

    fun set(min: Vector3d, max: Vector3d): Box3 {
        this.min.set(min)
        this.max.set(max)

        return this
    }

    fun setFromArray(array: DoubleArray): Box3 {
        var minX = Double.POSITIVE_INFINITY
        var minY = Double.POSITIVE_INFINITY
        var minZ = Double.POSITIVE_INFINITY

        var maxX = Double.NEGATIVE_INFINITY
        var maxY = Double.NEGATIVE_INFINITY
        var maxZ = Double.NEGATIVE_INFINITY

        for (i in array.indices step 3) {

            val x = array[i]
            val y = array[i + 1]
            val z = array[i + 2]

            if (x < minX) minX = x
            if (y < minY) minY = y
            if (z < minZ) minZ = z

            if (x > maxX) maxX = x
            if (y > maxY) maxY = y
            if (z > maxZ) maxZ = z

        }

        this.min.set(minX, minY, minZ)
        this.max.set(maxX, maxY, maxZ)

        return this
    }

    fun setFromPoints(points: List<Vector3d>): Box3 {
        this.makeEmpty()

        points.forEach {
            this.expandByPoint(it)
        }

        return this
    }

    fun setFromCenterAndSize(center: Vector3d, size: Vector3d): Box3 {
        val halfSize = Vector3d(size).mul(0.5)

        this.min.set(center).sub(halfSize)
        this.max.set(center).add(halfSize)

        return this
    }

    fun makeEmpty(): Box3 {
        this.min.x = Double.POSITIVE_INFINITY
        this.min.y = Double.POSITIVE_INFINITY
        this.min.z = Double.POSITIVE_INFINITY

        this.max.x = Double.NEGATIVE_INFINITY
        this.max.y = Double.NEGATIVE_INFINITY
        this.max.z = Double.NEGATIVE_INFINITY

        return this
    }

    fun isEmpty(): Boolean {
        // this is a more robust check for empty than ( volume <= 0 ) because volume can get positive with two negative axes
        return (this.max.x < this.min.x) || (this.max.y < this.min.y) || (this.max.z < this.min.z)
    }

    @JvmOverloads
    fun getCenter(target: Vector3d = Vector3d()): Vector3d {
        return if (this.isEmpty()) {
            target.set(0.0, 0.0, 0.0)
        } else {
            this.min.add(this.max, target).mul(0.5)
        }
    }

    @JvmOverloads
    fun getSize(target: Vector3d = Vector3d()): Vector3d {
        return if (this.isEmpty()) {
            target.set(0.0, 0.0, 0.0)
        } else {
            this.max.sub(this.min, target)
        }
    }

    fun expandByPoint(point: Vector3d): Box3 {
        this.min.min(point)
        this.max.max(point)

        return this
    }

    fun expandByVector(vector: Vector3d): Box3 {
        this.min.sub(vector)
        this.max.add(vector)

        return this
    }

    fun expandByScalar(scalar: Double): Box3 {
        this.min.add(-scalar, -scalar, -scalar)
        this.max.add(scalar, scalar, scalar)

        return this
    }

    fun containsPoint(point: Vector3d): Boolean {
        return !(point.x < this.min.x || point.x > this.max.x ||
                point.y < this.min.y || point.y > this.max.y ||
                point.z < this.min.z || point.z > this.max.z)
    }

    fun containsBox(box: Box3): Boolean {
        return this.min.x <= box.min.x && box.max.x <= this.max.x &&
                this.min.y <= box.min.y && box.max.y <= this.max.y &&
                this.min.z <= box.min.z && box.max.z <= this.max.z
    }

    @JvmOverloads
    fun getParameter(point: Vector3d, target: Vector3d = Vector3d()): Vector3d {
        return target.set(
            (point.x - this.min.x) / (this.max.x - this.min.x),
            (point.y - this.min.y) / (this.max.y - this.min.y),
            (point.z - this.min.z) / (this.max.z - this.min.z)
        )
    }

    fun intersectsBox(box: Box3): Boolean {
        // using 6 splitting planes to rule out intersections.
        return !(box.max.x < this.min.x || box.min.x > this.max.x ||
                box.max.y < this.min.y || box.min.y > this.max.y ||
                box.max.z < this.min.z || box.min.z > this.max.z)
    }

    fun intersectsSphere(sphere: Sphere): Boolean {
        val closestPoint = Vector3d()
        // Find the point on the AABB closest to the sphere center.
        this.clampPoint(sphere.center, closestPoint)

        // If that point is inside the sphere, the AABB and sphere intersect.
        return closestPoint.distanceSquared(sphere.center) <= (sphere.radius * sphere.radius)
    }

    fun intersectsPlane(plane: Plane): Boolean {
        // We compute the minimum and maximum dot product values. If those values
        // are on the same side (back or front) of the plane, then there is no intersection.

        var min: Double
        var max: Double

        if (plane.normal.x > 0) {

            min = plane.normal.x * this.min.x
            max = plane.normal.x * this.max.x

        } else {

            min = plane.normal.x * this.max.x
            max = plane.normal.x * this.min.x

        }

        if (plane.normal.y > 0) {

            min += plane.normal.y * this.min.y
            max += plane.normal.y * this.max.y

        } else {

            min += plane.normal.y * this.max.y
            max += plane.normal.y * this.min.y

        }

        if (plane.normal.z > 0) {

            min += plane.normal.z * this.min.z
            max += plane.normal.z * this.max.z

        } else {

            min += plane.normal.z * this.max.z
            max += plane.normal.z * this.min.z

        }

        return -plane.constant in min..max
    }

    fun intersectsTriangle(triangle: Triangle): Boolean {

        with(intersectsTriangleHelper) {

            fun satForAxes(axes: DoubleArray): Boolean {

                for (i in 0 until axes.size - 3 step 3) {

                    testAxis.fromArray(axes, i)
                    // project the aabb onto the separating axis
                    val r = extents.x * abs(testAxis.x) + extents.y * abs(testAxis.y) + extents.z * abs(testAxis.z)
                    // project all 3 vertices of the triangle onto the seperating axis
                    val p0 = v0.dot(testAxis)
                    val p1 = v1.dot(testAxis)
                    val p2 = v2.dot(testAxis)
                    // actual test, basically see if either of the most extreme of the triangle points intersects r
                    if (max(-max(p0, max(p1, p2)), min(p0, min(p1, p2))) > r) {

                        // points of the projected triangle are outside the projected half-length of the aabb
                        // the axis is seperating and we can exit
                        return false

                    }

                }

                return true

            }

            if (isEmpty()) {

                return false

            }

            // compute box center and extents
            getCenter(center)
            max.sub(center, extents)

            // translate triangle to aabb origin
            triangle.a.sub(center, v0)
            triangle.b.sub(center, v1)
            triangle.c.sub(center, v2)

            // compute edge vectors for triangle
            v1.sub(v0, f0)
            v2.sub(v1, f1)
            v0.sub(v2, f2)

            // test against axes that are given by cross product combinations of the edges of the triangle and the edges of the aabb
            // make an axis testing of each of the 3 sides of the aabb against each of the 3 sides of the triangle = 9 axis of separation
            // axis_ij = u_i x f_j (u0, u1, u2 = face normals of aabb = x,y,z axes vectors since aabb is axis aligned)
            var axes = doubleArrayOf(
                0.0, -f0.z, f0.y, 0.0, -f1.z, f1.y, 0.0, -f2.z, f2.y,
                f0.z, 0.0, -f0.x, f1.z, 0.0, -f1.x, f2.z, 0.0, -f2.x,
                -f0.y, f0.x, 0.0, -f1.y, f1.x, 0.0, -f2.y, f2.x, 0.0
            )
            if (!satForAxes(axes)) {

                return false

            }

            // test 3 face normals from the aabb
            axes = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0)
            if (!satForAxes(axes)) {

                return false

            }

            // finally testing the face normal of the triangle
            // use already existing triangle edge vectors here
            f0.cross(f1, triangleNormal)
            axes = doubleArrayOf(triangleNormal.x, triangleNormal.y, triangleNormal.z)
            return satForAxes(axes)

        }

    }

    @JvmOverloads
    fun clampPoint(point: Vector3d, target: Vector3d = Vector3d()): Vector3d {
        return target.set(point).clamp(this.min, this.max)
    }

    fun distanceToPoint(point: Vector3d): Double {
        val clampedPoint = Vector3d().set(point).clamp(this.min, this.max)
        return clampedPoint.sub(point).length()
    }

    @JvmOverloads
    fun getBoundingSphere(target: Sphere = Sphere()): Sphere {
        this.getCenter(target.center)

        target.radius = this.getSize(Vector3d()).length() * 0.5f

        return target
    }

    fun intersect(box: Box3): Box3 {
        this.min.max(box.min)
        this.max.min(box.max)

        // ensure that if there is no overlap, the result is fully empty, not slightly empty with non-inf/+inf values that will cause subsequence intersects to erroneously return valid values.
        if (this.isEmpty()) {
            this.makeEmpty()
        }

        return this
    }

    fun union(box: Box3): Box3 {
        this.min.min(box.min)
        this.max.max(box.max)

        return this
    }

    fun applyMatrix4(matrix: Matrix4dc): Box3 {

        // transform of empty box is an empty box.
        if (this.isEmpty()) {
            return this
        }

        // NOTE: I am using a binary pattern to specify all 2^3 combinations below
        points[0].set(this.min.x, this.min.y, this.min.z).mulPosition(matrix) // 000
        points[1].set(this.min.x, this.min.y, this.max.z).mulPosition(matrix) // 001
        points[2].set(this.min.x, this.max.y, this.min.z).mulPosition(matrix) // 010
        points[3].set(this.min.x, this.max.y, this.max.z).mulPosition(matrix) // 011
        points[4].set(this.max.x, this.min.y, this.min.z).mulPosition(matrix) // 100
        points[5].set(this.max.x, this.min.y, this.max.z).mulPosition(matrix) // 101
        points[6].set(this.max.x, this.max.y, this.min.z).mulPosition(matrix) // 110
        points[7].set(this.max.x, this.max.y, this.max.z).mulPosition(matrix) // 111

        this.setFromPoints(points)

        return this
    }

    fun translate(offset: Vector3d): Box3 {
        this.min.add(offset)
        this.max.add(offset)

        return this
    }


    /*fun setFromObject(`object`: Object3D): Box3 {
        this.makeEmpty()

        return this.expandByObject(`object`)
    }*/

    /*fun expandByObject(`object`: Object3D): Box3 {

        val v1 = Vector3d()

        `object`.updateMatrixWorld(true)
        `object`.traverse { node ->

            if (node is Mesh) {

                val geometry = node.geometry

                geometry.attributes.position?.also { attribute ->

                    for (i in 0 until attribute.count) {
                        attribute.toVector3(i, v1).applyMatrix4(node.matrixWorld)
                        expandByPoint(v1)
                    }

                }

            }

        }

        return this
    }*/


    override fun clone(): Box3 {
        return Box3().copy(this)
    }

    fun copy(box: Box3): Box3 {
        this.min.set(box.min)
        this.max.set(box.max)

        return this
    }

    companion object {

        private val points by lazy {
            List(8) { Vector3d() }
        }

    }

    private inner class IntersectsTriangleHelper {

        // triangle centered vertices
        var v0 = Vector3d()
        var v1 = Vector3d()
        var v2 = Vector3d()

        // triangle edge vectors
        var f0 = Vector3d()
        var f1 = Vector3d()
        var f2 = Vector3d()

        var testAxis = Vector3d()

        var center = Vector3d()
        var extents = Vector3d()

        var triangleNormal = Vector3d()

    }

}
