package no.ntnu.ihb.acco.math

import org.joml.Vector2d
import org.joml.Vector2dc
import org.joml.Vector3d
import kotlin.math.sqrt

data class Triangle @JvmOverloads constructor(
    var a: Vector3d = Vector3d(),
    var b: Vector3d = Vector3d(),
    var c: Vector3d = Vector3d()
) {

    private val closestPointToPointHelper by lazy { ClosestPointToPointHelper() }

    fun copy(triangle: Triangle): Triangle {

        this.a.set(triangle.a)
        this.b.set(triangle.b)
        this.c.set(triangle.c)

        return this

    }

    fun set(a: Vector3d, b: Vector3d, c: Vector3d): Triangle {

        this.a.set(a)
        this.b.set(b)
        this.c.set(c)

        return this

    }

    fun setFromPointsAndIndices(points: List<Vector3d>, i0: Int, i1: Int, i2: Int): Triangle {

        this.a.set(points[i0])
        this.b.set(points[i1])
        this.c.set(points[i2])

        return this

    }

    fun getArea(): Double {

        this.c.sub(this.b, v0)
        this.a.sub(this.b, v1)

        return v0.cross(v1).length() * 0.5

    }

    @JvmOverloads
    fun getMidpoint(target: Vector3d = Vector3d()): Vector3d {
        return this.a.add(this.b, target).add(this.c).mul(1.0 / 3)
    }

    @JvmOverloads
    fun getNormal(target: Vector3d = Vector3d()): Vector3d {
        return getNormal(this.a, this.b, this.c, target)
    }

    @JvmOverloads
    fun getPlane(target: Plane = Plane()): Plane {
        return target.setFromCoplanarPoints(this.a, this.b, this.c)
    }

    @JvmOverloads
    fun getBarycoord(point: Vector3d, target: Vector3d = Vector3d()): Vector3d {
        return getBarycoord(point, this.a, this.b, this.c, target)
    }

    @JvmOverloads
    fun getUV(
        point: Vector3d,
        uv1: Vector2dc,
        uv2: Vector2dc,
        uv3: Vector2dc,
        target: Vector2d = Vector2d()
    ): Vector2d {

        return getUV(point, this.a, this.b, this.c, uv1, uv2, uv3, target)

    }

    fun containsPoint(point: Vector3d): Boolean {

        return containsPoint(point, this.a, this.b, this.c)

    }

    fun isFrontFacing(direction: Vector3d): Boolean {

        return isFrontFacing(this.a, this.b, this.c, direction)

    }

    fun intersectsBox(box: Box3): Boolean {

        return box.intersectsTriangle(this)

    }

    @JvmOverloads
    fun closestPointToPoint(p: Vector3d, target: Vector3d = Vector3d()): Vector3d {

        with(closestPointToPointHelper) {

            val v: Double
            val w: Double

            // algorithm thanks to Real-Time Collision Detection by Christer Ericson,
            // published by Morgan Kaufmann Publishers, (c) 2005 Elsevier Inc.,
            // under the accompanying license; see chapter 5.1.5 for detailed explanation.
            // basically, we're distinguishing which of the voronoi regions of the triangle
            // the point lies in with the minimum amount of redundant computation.

            b.sub(a, vab)
            c.sub(a, vac)
            p.sub(a, vap)
            val d1 = vab.dot(vap)
            val d2 = vac.dot(vap)
            if (d1 <= 0 && d2 <= 0) {

                // vertex region of A; barycentric coords (1, 0, 0)
                return target.set(a)

            }

            p.sub(b, vbp)
            val d3 = vab.dot(vbp)
            val d4 = vac.dot(vbp)
            if (d3 >= 0 && d4 <= d3) {

                // vertex region of B; barycentric coords (0, 1, 0)
                return target.set(b)

            }

            val vc = d1 * d4 - d3 * d2
            if (vc <= 0 && d1 >= 0 && d3 <= 0) {

                v = d1 / (d1 - d3)
                // edge region of AB; barycentric coords (1-v, v, 0)
                return target.set(a).addScaledVector(vab, v)

            }

            p.sub(c, vcp)
            val d5 = vab.dot(vcp)
            val d6 = vac.dot(vcp)
            if (d6 >= 0 && d5 <= d6) {

                // vertex region of C; barycentric coords (0, 0, 1)
                return target.set(c)

            }

            val vb = d5 * d2 - d1 * d6
            if (vb <= 0 && d2 >= 0 && d6 <= 0) {

                w = d2 / (d2 - d6)
                // edge region of AC; barycentric coords (1-w, 0, w)
                return target.set(a).addScaledVector(vac, w)

            }

            val va = d3 * d6 - d5 * d4
            if (va <= 0 && (d4 - d3) >= 0 && (d5 - d6) >= 0) {

                c.sub(b, vbc)
                w = (d4 - d3) / ((d4 - d3) + (d5 - d6))
                // edge region of BC; barycentric coords (0, 1-w, w)
                return target.set(b).addScaledVector(vbc, w) // edge region of BC

            }

            // face region
            val denom = 1.0 / (va + vb + vc)
            // u = va * denom
            v = vb * denom
            w = vc * denom
            return target.set(a).addScaledVector(vab, v).addScaledVector(vac, w)

        }

    }

    companion object {

        private val v0 by lazy { Vector3d(); }
        private val v1 by lazy { Vector3d(); }
        private val v2 by lazy { Vector3d(); }
        private val barycoord by lazy { Vector3d() }

        @JvmOverloads
        fun getNormal(a: Vector3d, b: Vector3d, c: Vector3d, target: Vector3d = Vector3d()): Vector3d {

            c.sub(b, target)
            a.sub(b, v0)
            target.cross(v0)

            val targetLengthSq = target.lengthSquared()
            if (targetLengthSq > 0) {

                return target.mul(1.0 / sqrt(targetLengthSq))

            }

            return target.set(0.0, 0.0, 0.0)

        }

        @JvmOverloads
        fun getBarycoord(
            point: Vector3d,
            a: Vector3d,
            b: Vector3d,
            c: Vector3d,
            target: Vector3d = Vector3d()
        ): Vector3d {

            c.sub(a, v0)
            b.sub(a, v1)
            point.sub(a, v2)

            val dot00 = v0.dot(v0)
            val dot01 = v0.dot(v1)
            val dot02 = v0.dot(v2)
            val dot11 = v1.dot(v1)
            val dot12 = v1.dot(v2)

            val denom = (dot00 * dot11 - dot01 * dot01)

            // collinear or singular triangle
            if (denom == 0.0) {

                // arbitrary location outside of triangle?
                // not sure if this is the best idea, maybe should be returning undefined
                return target.set(-2.0, -1.0, -1.0)

            }

            val invDenom = 1.0 / denom
            val u = (dot11 * dot02 - dot01 * dot12) * invDenom
            val v = (dot00 * dot12 - dot01 * dot02) * invDenom

            // barycentric coordinates must always sum to 1
            return target.set(1 - u - v, v, u)

        }

        fun containsPoint(point: Vector3d, a: Vector3d, b: Vector3d, c: Vector3d): Boolean {

            getBarycoord(point, a, b, c, v1)

            return (v1.x >= 0) && (v1.y >= 0) && ((v1.x + v1.y) <= 1)

        }

        @JvmOverloads
        fun getUV(
            point: Vector3d,
            p1: Vector3d,
            p2: Vector3d,
            p3: Vector3d,
            uv1: Vector2dc,
            uv2: Vector2dc,
            uv3: Vector2dc,
            target: Vector2d = Vector2d()
        ): Vector2d {

            this.getBarycoord(point, p1, p2, p3, barycoord)

            target.set(0.0, 0.0)
            target.addScaledVector(uv1, barycoord.x)
            target.addScaledVector(uv2, barycoord.y)
            target.addScaledVector(uv3, barycoord.z)

            return target

        }

        fun isFrontFacing(a: Vector3d, b: Vector3d, c: Vector3d, direction: Vector3d): Boolean {
            c.sub(b, v0)
            a.sub(b, v1)

            // strictly front facing
            return v0.cross(v1).dot(direction) < 0
        }

    }

    private class ClosestPointToPointHelper {
        val vab = Vector3d()
        val vac = Vector3d()
        val vbc = Vector3d()
        val vap = Vector3d()
        val vbp = Vector3d()
        val vcp = Vector3d()
    }

}
