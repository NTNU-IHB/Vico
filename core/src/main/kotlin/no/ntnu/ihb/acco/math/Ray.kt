package no.ntnu.ihb.acco.math

import org.joml.Matrix4dc
import org.joml.Vector3d
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Ray @JvmOverloads constructor(
    var origin: Vector3d = Vector3d(),
    val direction: Vector3d = Vector3d()
) {

    private val v = Vector3d()

    private var segCenter = Vector3d()
    private var segDir = Vector3d()
    private var diff = Vector3d()

    private var edge1 = Vector3d()
    private var edge2 = Vector3d()
    private var normal = Vector3d()


    fun set(origin: Vector3d, direction: Vector3d): Ray {

        this.origin.set(origin)
        this.direction.set(direction)

        return this

    }

    fun clone(): Ray {

        return Ray().copy(this)

    }

    fun copy(ray: Ray): Ray {

        this.origin.set(ray.origin)
        this.direction.set(ray.direction)

        return this

    }

    @JvmOverloads
    fun at(t: Double, target: Vector3d = Vector3d()): Vector3d {

        return target.set(this.direction).mul(t).add(this.origin)

    }

    fun lookAt(v: Vector3d): Ray {

        this.direction.set(v).sub(this.origin).normalize()

        return this
    }

    fun recast(t: Double): Ray {

        this.origin.set(this.at(t, v))

        return this

    }

    @JvmOverloads
    fun closestPointToPoint(point: Vector3d, target: Vector3d = Vector3d()): Vector3d {

        point.sub(this.origin, target)

        val directionDistance = target.dot(this.direction)

        if (directionDistance < 0) {

            return target.set(this.origin)

        }

        return target.set(this.direction).mul(directionDistance).add(this.origin)

    }

    fun distanceToPoint(point: Vector3d): Double {

        return sqrt(this.distanceSqToPoint(point))

    }

    fun distanceSqToPoint(point: Vector3d): Double {

        val directionDistance = point.sub(this.origin, v).dot(this.direction)

        // point behind the ray

        if (directionDistance < 0) {

            return this.origin.distanceSquared(point)

        }

        v.set(this.direction).mul(directionDistance).add(this.origin)

        return v.distanceSquared(point)

    }

    fun distanceSqToSegment(
        v0: Vector3d,
        v1: Vector3d,
        optionalPointOnRay: Vector3d? = null,
        optionalPointOnSegment: Vector3d? = null
    ): Double {

        // from http://www.geometrictools.com/GTEngine/Include/Mathematics/GteDistRaySegment.h
        // It returns the min distance between the ray and the segment
        // defined by v0 and v1
        // It can also set two optional targets :
        // - The closest point on the ray
        // - The closest point on the segment

        segCenter.set(v0).add(v1).mul(0.5)
        segDir.set(v1).sub(v0).normalize()
        diff.set(this.origin).sub(segCenter)

        val segExtent = v0.distance(v1) * 0.5
        val a01 = -this.direction.dot(segDir)
        val b0 = diff.dot(this.direction)
        val b1 = -diff.dot(segDir)
        val c = diff.lengthSquared()
        val det = abs(1 - a01 * a01)

        var s0: Double
        var s1: Double
        val sqrDist: Double

        if (det > 0) {

            // The ray and segment are not parallel.

            s0 = a01 * b1 - b0
            s1 = a01 * b0 - b1
            val extDet = segExtent * det

            if (s0 >= 0) {

                if (s1 >= -extDet) {

                    if (s1 <= extDet) {

                        // region 0
                        // Minimum at interior points of ray and segment.

                        val invDet = 1.0 / det
                        s0 *= invDet
                        s1 *= invDet
                        sqrDist = s0 * (s0 + a01 * s1 + 2 * b0) + s1 * (a01 * s0 + s1 + 2 * b1) + c

                    } else {

                        // region 1

                        s1 = segExtent
                        s0 = max(0.0, -(a01 * s1 + b0))
                        sqrDist = -s0 * s0 + s1 * (s1 + 2 * b1) + c

                    }

                } else {

                    // region 5

                    s1 = -segExtent
                    s0 = max(0.0, -(a01 * s1 + b0))
                    sqrDist = -s0 * s0 + s1 * (s1 + 2 * b1) + c

                }

            } else {

                if (s1 <= -extDet) {

                    // region 4

                    s0 = max(0.0, -(-a01 * segExtent + b0))
                    s1 = if (s0 > 0) -segExtent else min(max(-segExtent, -b1), segExtent)
                    sqrDist = -s0 * s0 + s1 * (s1 + 2 * b1) + c

                } else if (s1 <= extDet) {

                    // region 3

                    s0 = 0.0
                    s1 = min(max(-segExtent, -b1), segExtent)
                    sqrDist = s1 * (s1 + 2 * b1) + c

                } else {

                    // region 2

                    s0 = max(0.0, -(a01 * segExtent + b0))
                    s1 = if (s0 > 0) segExtent else min(max(-segExtent, -b1), segExtent)
                    sqrDist = -s0 * s0 + s1 * (s1 + 2 * b1) + c

                }

            }

        } else {

            // Ray and segment are parallel.

            s1 = if (a01 > 0) -segExtent else segExtent
            s0 = max(0.0, -(a01 * s1 + b0))
            sqrDist = -s0 * s0 + s1 * (s1 + 2 * b1) + c

        }

        optionalPointOnRay?.set(this.direction)?.mul(s0)?.add(this.origin)

        optionalPointOnSegment?.set(segDir)?.mul(s1)?.add(segCenter)

        return sqrDist

    }

    fun intersectSphere(sphere: Sphere, target: Vector3d): Vector3d? {

        sphere.center.sub(this.origin, v)
        val tca = v.dot(this.direction)
        val d2 = v.dot(v) - tca * tca
        val radius2 = sphere.radius * sphere.radius

        if (d2 > radius2) return null

        val thc = sqrt(radius2 - d2)

        // t0 = first intersect point - entrance on front of sphere
        val t0 = tca - thc

        // t1 = second intersect point - exit point on back of sphere
        val t1 = tca + thc

        // test to see if both t0 and t1 are behind the ray - if so, return null
        if (t0 < 0 && t1 < 0) return null

        // test to see if t0 is behind the ray:
        // if it is, the ray is inside the sphere, so return the second exit point scaled by t1,
        // in order to always return an intersect point that is in front of the ray.
        if (t0 < 0) return this.at(t1, target)

        // else t0 is in front of the ray, so return the first collision point scaled by t0
        return this.at(t0, target)
    }

    fun intersectsSphere(sphere: Sphere): Boolean {

        return this.distanceSqToPoint(sphere.center) <= (sphere.radius * sphere.radius)

    }

    fun distanceToPlane(plane: Plane): Double? {

        val denominator = plane.normal.dot(this.direction)

        if (denominator == 0.0) {

            // line is coplanar, return origin
            if (plane.distanceToPoint(this.origin) == 0.0) {

                return 0.0

            }

            // Null is preferable to undefined since undefined means.... it is undefined

            return null

        }

        val t = -(this.origin.dot(plane.normal) + plane.constant) / denominator

        // Return if the ray never intersects the plane

        return if (t >= 0) t else null

    }

    @JvmOverloads
    fun intersectPlane(plane: Plane, target: Vector3d = Vector3d()): Vector3d? {

        val t = this.distanceToPlane(plane) ?: return null

        return this.at(t, target)

    }

    fun intersectsPlane(plane: Plane): Boolean {

        // check if the ray lies on the plane first

        val distToPoint = plane.distanceToPoint(this.origin)

        if (distToPoint == 0.0) {

            return true

        }

        val denominator = plane.normal.dot(this.direction)

        if (denominator * distToPoint < 0) {

            return true

        }

        // ray origin is behind the plane (and is pointing behind it)

        return false

    }

    fun intersectBox(box: Box3, target: Vector3d): Vector3d? {

        var tmin: Double
        var tmax: Double
        val tymin: Double
        val tymax: Double
        val tzmin: Double
        val tzmax: Double

        val invdirx = 1f / this.direction.x
        val invdiry = 1f / this.direction.y
        val invdirz = 1f / this.direction.z

        if (invdirx >= 0) {

            tmin = (box.min.x - origin.x) * invdirx
            tmax = (box.max.x - origin.x) * invdirx

        } else {

            tmin = (box.max.x - origin.x) * invdirx
            tmax = (box.min.x - origin.x) * invdirx

        }

        if (invdiry >= 0) {

            tymin = (box.min.y - origin.y) * invdiry
            tymax = (box.max.y - origin.y) * invdiry

        } else {

            tymin = (box.max.y - origin.y) * invdiry
            tymax = (box.min.y - origin.y) * invdiry

        }

        if ((tmin > tymax) || (tymin > tmax)) return null

        // These lines also handle the case where tmin or tmax is NaN
        // (result of 0 * Infinity). x !== x returns true if x is NaN

        if (tymin > tmin || tmin != tmin) tmin = tymin

        if (tymax < tmax || tmax != tmax) tmax = tymax

        if (invdirz >= 0) {

            tzmin = (box.min.z - origin.z) * invdirz
            tzmax = (box.max.z - origin.z) * invdirz

        } else {

            tzmin = (box.max.z - origin.z) * invdirz
            tzmax = (box.min.z - origin.z) * invdirz

        }

        if ((tmin > tzmax) || (tzmin > tmax)) return null

        if (tzmin > tmin || tmin != tmin) tmin = tzmin

        if (tzmax < tmax || tmax != tmax) tmax = tzmax

        //return point closest to the ray (positive side)

        if (tmax < 0) return null

        return this.at(if (tmin >= 0) tmin else tmax, target)

    }

    fun intersectsBox(box: Box3): Boolean {

        return this.intersectBox(box, v) !== null

    }

    fun intersectTriangle(
        a: Vector3d,
        b: Vector3d,
        c: Vector3d,
        backfaceCulling: Boolean,
        target: Vector3d
    ): Vector3d? {

        // from http://www.geometrictools.com/GTEngine/Include/Mathematics/GteIntrRay3Triangle3.h

        b.sub(a, edge1)
        c.sub(a, edge2)
        edge1.cross(edge2, normal)

        // Solve Q + t*D = b1*E1 + b2*E2 (Q = kDiff, D = ray direction,
        // E1 = kEdge1, E2 = kEdge2, N = Cross(E1,E2)) by
        //   |Dot(D,N)|*b1 = sign(Dot(D,N))*Dot(D,Cross(Q,E2))
        //   |Dot(D,N)|*b2 = sign(Dot(D,N))*Dot(D,Cross(E1,Q))
        //   |Dot(D,N)|*t = -sign(Dot(D,N))*Dot(Q,N)
        var DdN = this.direction.dot(normal)
        val sign: Int

        if (DdN > 0) {

            if (backfaceCulling) return null
            sign = 1

        } else if (DdN < 0) {

            sign = -1
            DdN = -DdN

        } else {

            return null

        }

        this.origin.sub(a, diff)
        val DdQxE2 = sign * this.direction.dot(diff.cross(edge2, edge2))

        // b1 < 0, no intersection
        if (DdQxE2 < 0) {

            return null

        }

        val DdE1xQ = sign * this.direction.dot(edge1.cross(diff))

        // b2 < 0, no intersection
        if (DdE1xQ < 0) {

            return null

        }

        // b1+b2 > 1, no intersection
        if (DdQxE2 + DdE1xQ > DdN) {

            return null

        }

        // Line intersects triangle, check if ray does.
        val QdN = -sign * diff.dot(normal)

        // t < 0, no intersection
        if (QdN < 0) {

            return null

        }

        // Ray intersects triangle.
        return this.at(QdN / DdN, target)

    }

    fun applyMatrix4(matrix4: Matrix4dc): Ray {

        this.origin.mulPosition(matrix4)
        this.direction.mulDirection(matrix4)

        return this

    }

}
