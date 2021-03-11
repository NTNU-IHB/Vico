package no.ntnu.ihb.vico.math


class Angle private constructor(
    var phi: Double = 0.0
) {

    enum class Unit {
        DEG, RAD;

        fun ensureRadians(value: Float): Float {
            return when (this) {
                RAD -> value
                DEG -> Math.toRadians(value.toDouble()).toFloat()
            }
        }

        fun ensureRadians(value: Double): Double {
            return when (this) {
                RAD -> value
                DEG -> Math.toRadians(value)
            }
        }

    }

    constructor() : this(0.0)
    constructor(phi: Double, repr: Unit) : this(repr.ensureRadians(phi))


    fun set(phi: Double, repr: Unit) = apply {
        this.phi = repr.ensureRadians(phi)
    }

    fun inDegrees(): Double {
        return Math.toDegrees(phi)
    }

    fun inRadians(): Double {
        return phi
    }

    fun cos(): Double {
        return kotlin.math.cos(phi)
    }

    fun sin(): Double {
        return kotlin.math.sin(phi)
    }

    fun acos(): Double {
        return kotlin.math.acos(phi)
    }

    fun asin(): Double {
        return kotlin.math.asin(phi)
    }

    fun scale(s: Double): Angle {
        phi *= s
        return this
    }

    fun multiply(phi: Double, repr: Unit) = apply {
        this.phi *= repr.ensureRadians(phi)
    }

    fun multiply(angle: Angle) = apply {
        phi *= angle.phi
    }

    fun divide(phi: Double, repr: Unit) = apply {
        this.phi /= repr.ensureRadians(phi)
    }

    fun divide(angle: Angle) = apply {
        phi /= angle.phi
    }

    fun add(phi: Double, repr: Unit) = apply {
        this.phi += repr.ensureRadians(phi)
    }

    fun add(angle: Angle) = apply {
        phi += angle.phi
        return this
    }

    fun sub(angle: Angle) = apply {
        phi -= angle.phi
    }

    fun sub(phi: Double, repr: Unit) = apply {
        this.phi -= repr.ensureRadians(phi)
    }

    fun negate() = apply {
        phi *= -1.0
    }

    fun isGreaterThan(other: Angle): Boolean {
        return phi > other.phi
    }

    fun isLessThan(other: Angle): Boolean {
        return phi < other.phi
    }

    fun copy(): Angle {
        return Angle().copy(this)
    }

    fun copy(a: Angle) = apply {
        phi = a.phi
    }

    operator fun plus(angle: Angle) = copy().add(angle)
    operator fun plusAssign(angle: Angle) {
        add(angle)
    }

    operator fun minus(angle: Angle) = copy().sub(angle)
    operator fun minusAssign(angle: Angle) {
        sub(angle)
    }

    override fun toString(): String {
        return "Angle{" + "inDegrees=" + inDegrees() + ", inRadians=" + inRadians() + '}'
    }

    companion object {

        @JvmStatic
        fun rad(phi: Double): Angle {
            return Angle(phi, Unit.RAD)
        }

        @JvmStatic
        fun deg(phi: Double): Angle {
            return Angle(phi, Unit.DEG)
        }

    }

}
