package info.laht.krender.curves

import org.joml.Matrix4d
import org.joml.Vector3d
import java.util.*
import kotlin.math.acos

abstract class Curve3 {

    var needsUpdate = false
    val lengths: List<Double>
        get() = getLengths(200)
    private var cacheArcLengths: List<Double>? = null


    fun getLengths(divisions: Int): List<Double> {
        if (cacheArcLengths != null && cacheArcLengths!!.size == divisions + 1 && !needsUpdate) {
            return cacheArcLengths!!
        }
        needsUpdate = false
        val cache: MutableList<Double> = ArrayList(divisions)
        cache.add(0.0)
        var sum = 0.0
        var last: Vector3d? = getPoint(0.0)
        for (p in 1..divisions) {
            val current = getPoint(p.toDouble() / divisions)
            sum += current.distance(last)
            cache.add(sum)
            last = current
        }
        cacheArcLengths = cache
        return cache
    }

    abstract fun getPoint(t: Double): Vector3d

    fun getPointAt(u: Double): Vector3d {
        val t = getUtoTmapping(u)
        return getPoint(t)
    }

    fun getPoints(divisions: Int): List<Vector3d> {
        val points: MutableList<Vector3d> = ArrayList()
        for (d in 0..divisions) {
            points.add(getPoint(d.toDouble() / divisions))
        }
        return points
    }

    fun getSpacedPoints(divisions: Int): List<Vector3d> {
        val points: MutableList<Vector3d> = ArrayList()
        for (d in 0..divisions) {
            points.add(getPointAt(d.toDouble() / divisions))
        }
        return points
    }

    private fun getUtoTmapping(u: Double, distance: Double? = null): Double {
        val arcLengths = lengths
        val il = arcLengths.size
        val targetArcLength = distance ?: u * arcLengths[il - 1]

        // binary search for the index with largest value smaller than target u distance
        var low = 0
        var high = il - 1
        while (low <= high) {
            val i = Math.floor(low + (high - low) / 2.toDouble())
                .toInt() // less likely to overflow, though probably not issue here, JS doesn't really have integers, all numbers are floats
            val comparison = arcLengths[i] - targetArcLength
            if (comparison < 0) {
                low = i + 1
            } else if (comparison > 0) {
                high = i - 1
            } else {
                high = i
                break
                // DONE
            }
        }
        val i = high
        if (arcLengths[i] == targetArcLength) {
            return (i / (il - 1)).toDouble()
        }

        // we could get finer grain at lengths, or use simple interpolation between two points
        val lengthBefore = arcLengths[i]
        val lengthAfter = arcLengths[i + 1]
        val segmentLength = lengthAfter - lengthBefore

        // determine where we are between the 'before' and 'after' points
        val segmentFraction = (targetArcLength - lengthBefore) / segmentLength

        // add that fractional amount to t
        return (i + segmentFraction) / (il - 1)
    }

    fun getTangent(t: Double): Vector3d {
        val delta = 0.0001
        var t1 = t - delta
        var t2 = t + delta

        // Capping in case of danger
        if (t1 < 0) {
            t1 = 0.0
        }
        if (t2 > 1) {
            t2 = 1.0
        }
        val pt1 = getPoint(t1)
        val pt2 = getPoint(t2)
        return Vector3d(pt2).sub(pt1).normalize()
    }

    fun getTangentAt(u: Double): Vector3d {
        val t = this.getUtoTmapping(u)
        return getTangent(t)
    }

    fun computeFrenetFrames(segments: Int, closed: Boolean): FrenetFrame3 {

        // see http://www.cs.indiana.edu/pub/techreports/TR425.pdf
        val normal = Vector3d()
        val tangents: MutableList<Vector3d> = ArrayList()
        val normals: MutableList<Vector3d> = ArrayList()
        val binormals: MutableList<Vector3d> = ArrayList()
        var theta: Double
        val vec = Vector3d()

        // compute the tangent vectors for each segment on the curve
        for (i in 0..segments) {
            val u = i.toDouble() / segments
            val tangentAt = getTangentAt(u)
            tangentAt.normalize()
            tangents.add(tangentAt)
        }

        // select an initial normal vector perpendicular to the first tangent vector,
        // and in the direction of the minimum tangent xyz component
        normals.add(Vector3d())
        binormals.add(Vector3d())
        var min = Double.MAX_VALUE
        val tx = Math.abs(tangents[0].x())
        val ty = Math.abs(tangents[0].y())
        val tz = Math.abs(tangents[0].z())
        if (tx <= min) {
            min = tx
            normal[1.0, 0.0] = 0.0
        }
        if (ty <= min) {
            min = ty
            normal[0.0, 1.0] = 0.0
        }
        if (tz <= min) {
            normal[0.0, 0.0] = 1.0
        }
        vec.set(tangents[0]).cross(normal).normalize()
        normals[0].set(tangents[0]).cross(vec)
        binormals[0].set(tangents[0]).cross(normals[0])

        // compute the slowly-varying normal and binormal vectors for each segment on the curve
        for (i in 1..segments) {
            normals.add(Vector3d(normals[i - 1]))
            binormals.add(Vector3d(binormals[i - 1]))
            vec.set(tangents[i - 1]).cross(tangents[i])
            if (vec.length() > Double.MIN_VALUE) {
                vec.normalize()
                theta = acos(tangents[i - 1].dot(tangents[i]).coerceIn(-1.0, 1.0))
                // clamp for floating pt errors
                normals[i].mulPosition(Matrix4d().rotate(theta, vec))
            }
            binormals[i].set(tangents[i]).cross(normals[i])
        }

        // if the curve is closed, postprocess the vectors so the first and last normal vectors are the same
        if (closed) {
            theta = acos(normals[0].dot(normals[segments]).coerceIn(-1.0, 1.0))
            theta *= (1.0 / segments)
            if (tangents[0].dot(vec.set(normals[0]).cross(normals[segments])) > 0) {
                theta -= theta
            }
            for (i in 1..segments) {

                // twist a little...
                normals[i].mulPosition(Matrix4d().rotate(theta * i, tangents[i]))
                binormals[i].set(tangents[i]).cross(normals[i])
            }
        }
        return FrenetFrame3(tangents, normals, binormals)
    }

    data class FrenetFrame3(
        val tangents: List<Vector3d>,
        val normals: List<Vector3d>,
        val binormals: List<Vector3d>
    )

}
