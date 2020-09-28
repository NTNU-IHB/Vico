package info.laht.krender.curves

import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3fc
import org.slf4j.LoggerFactory
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

class CatmullRomCurve3(
    private val points: List<Vector3fc>,
    private val type: Type? = null
) : Curve3() {

    enum class Type(val typeName: String) {
        CENTRIPETAL("centripetal"), CHORDAL("chordal"), CATMULLROM("catmullrom");
    }

    private val tmp = Vector3f()
    private val px = CubicPoly()
    private val py = CubicPoly()
    private val pz = CubicPoly()

    var tension: Float? = null
    var closed = false

    override fun getPoint(t: Double): Vector3d {
        val l = points.size
        check(l >= 2) { "duh, you need at least 2 points! Input: $points" }
        val point = (l - if (closed) 0 else 1) * t
        var intPoint = floor(point).toInt()
        var weight = point - intPoint
        if (closed) {
            intPoint += if (intPoint > 0) 0 else (floor(abs(intPoint) / points.size.toDouble()) + 1).toInt() * points.size
        } else if (weight == 0.0 && intPoint == l - 1) {
            intPoint = l - 2
            weight = 1.0
        }
        val p0: Vector3fc
        val p1: Vector3fc
        val p2: Vector3fc
        val p3: Vector3fc // 4 points
        p0 = if (closed || intPoint > 0) {
            points[(intPoint - 1) % l]
        } else {
            // extrapolate first point
            tmp.set(points[0]).sub(points[1]).add(points[0])
            tmp
        }
        p1 = points[intPoint % l]
        p2 = points[(intPoint + 1) % l]
        p3 = if (closed || intPoint + 2 < l) {
            points[(intPoint + 2) % l]
        } else {
            // extrapolate last point
            tmp.set(points[l - 1]).sub(points[l - 2]).add(points[l - 1])
            tmp
        }
        if (type == null || type == Type.CENTRIPETAL || type == Type.CHORDAL) {

            // init Centripetal / Chordal Catmull-Rom
            val pow = if (type == Type.CHORDAL) 0.5f else 0.25f
            var dt0 = p0.distanceSquared(p1).pow(pow)
            var dt1 = p1.distanceSquared(p2).pow(pow)
            var dt2 = p2.distanceSquared(p3).pow(pow)

            // safety check for repeated points
            if (dt1 < 1e-4) {
                dt1 = 1f
            }
            if (dt0 < 1e-4) {
                dt0 = dt1
            }
            if (dt2 < 1e-4) {
                dt2 = dt1
            }
            px.initNonuniformCatmullRom(p0.x(), p1.x(), p2.x(), p3.x(), dt0, dt1, dt2)
            py.initNonuniformCatmullRom(p0.y(), p1.y(), p2.y(), p3.y(), dt0, dt1, dt2)
            pz.initNonuniformCatmullRom(p0.z(), p1.z(), p2.z(), p3.z(), dt0, dt1, dt2)
        } else if (type == Type.CATMULLROM) {
            tension = if (tension != null) tension else 0.5f
            px.initCatmullRom(p0.x(), p1.x(), p2.x(), p3.x(), tension!!)
            py.initCatmullRom(p0.y(), p1.y(), p2.y(), p3.y(), tension!!)
            pz.initCatmullRom(p0.z(), p1.z(), p2.z(), p3.z(), tension!!)
        }
        return Vector3d(
            px.calc(weight),
            py.calc(weight),
            pz.calc(weight)
        )
    }

    private class CubicPoly {

        var c0 = 0f
        var c1 = 0f
        var c2 = 0f
        var c3 = 0f

        fun init(x0: Float, x1: Float, t0: Float, t1: Float) {
            c0 = x0
            c1 = t0
            c2 = -3 * x0 + 3 * x1 - 2 * t0 - t1
            c3 = 2 * x0 - 2 * x1 + t0 + t1
        }

        fun initNonuniformCatmullRom(
            x0: Float,
            x1: Float,
            x2: Float,
            x3: Float,
            dt0: Float,
            dt1: Float,
            dt2: Float
        ) {

            // compute tangents when parameterized in [t1,t2]
            var t1 = (x1 - x0) / dt0 - (x2 - x0) / (dt0 + dt1) + (x2 - x1) / dt1
            var t2 = (x2 - x1) / dt1 - (x3 - x1) / (dt1 + dt2) + (x3 - x2) / dt2

            // rescale tangents for parametrization in [0,1]
            t1 *= dt1
            t2 *= dt1

            // initCubicPoly
            init(x1, x2, t1, t2)
        }

        /**
         * Standard Catmull-Rom spline: interpolate between x1 and x2 with
         * previous/following points x1/x4
         */
        fun initCatmullRom(x0: Float, x1: Float, x2: Float, x3: Float, tension: Float) {
            init(x1, x2, tension * (x2 - x0), tension * (x3 - x1))
        }

        fun calc(t: Double): Double {
            val t2 = t * t
            val t3 = t2 * t
            return c0 + c1 * t + c2 * t2 + c3 * t3
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CatmullRomCurve3::class.java)
    }

}
