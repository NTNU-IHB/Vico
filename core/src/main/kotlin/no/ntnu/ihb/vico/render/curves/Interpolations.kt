package info.laht.krender.curves


object Interpolations {

    fun CatmullRom(t: Double, p0: Double, p1: Double, p2: Double, p3: Double): Double {
        val v0 = (p2 - p0) * 0.5
        val v1 = (p3 - p1) * 0.5
        val t2 = t * t
        val t3 = t * t2
        return (2 * p1 - 2 * p2 + v0 + v1) * t3 + (-3 * p1 + 3 * p2 - 2 * v0 - v1) * t2 + v0 * t + p1
    }

    fun QuadraticBezierP0(t: Double, p: Double): Double {
        val k = 1 - t
        return k * k * p
    }

    fun QuadraticBezierP1(t: Double, p: Double): Double {
        return 2 * (1 - t) * t * p
    }

    fun QuadraticBezierP2(t: Double, p: Double): Double {
        return t * t * p
    }

    fun QuadraticBezier(t: Double, p0: Double, p1: Double, p2: Double): Double {
        return (QuadraticBezierP0(t, p0) + QuadraticBezierP1(t, p1)
                + QuadraticBezierP2(t, p2))
    }

    fun CubicBezierP0(t: Double, p: Double): Double {
        val k = 1 - t
        return k * k * k * p
    }

    fun CubicBezierP1(t: Double, p: Double): Double {
        val k = 1 - t
        return 3 * k * k * t * p
    }

    fun CubicBezierP2(t: Double, p: Double): Double {
        return 3 * (1 - t) * t * t * p
    }

    fun CubicBezierP3(t: Double, p: Double): Double {
        return t * t * t * p
    }

    fun CubicBezier(t: Double, p0: Double, p1: Double, p2: Double, p3: Double): Double {
        return (CubicBezierP0(t, p0) + CubicBezierP1(t, p1) + CubicBezierP2(t, p2)
                + CubicBezierP3(t, p3))
    }

}
