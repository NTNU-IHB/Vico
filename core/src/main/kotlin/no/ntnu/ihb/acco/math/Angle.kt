package no.ntnu.ihb.acco.math

import java.lang.Math.toDegrees

class Angle private constructor(
    var phi: Double = 0.0
) {

    enum class Unit {
        DEG, RAD
    }

    constructor() : this(0.0)
    constructor(phi: Double, repr: Unit) : this(ensureRadians(phi, repr))


    fun set(phi: Double, repr: Unit) = apply {
        this.phi = ensureRadians(phi, repr)
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
        this.phi *= ensureRadians(phi, repr)
    }

    fun multiply(angle: Angle) = apply {
        phi *= angle.phi
    }

    fun divide(phi: Double, repr: Unit) = apply {
        this.phi /= ensureRadians(phi, repr)
    }

    fun divide(angle: Angle) = apply {
        phi /= angle.phi
    }

    fun add(phi: Double, repr: Unit) = apply {
        this.phi += ensureRadians(phi, repr)
    }

    fun add(angle: Angle) = apply {
        phi += angle.phi
        return this
    }

    fun sub(angle: Angle) = apply {
        phi -= angle.phi
    }

    fun sub(phi: Double, repr: Unit) = apply {
        this.phi -= ensureRadians(phi, repr)
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
        return "Angle{" + "inDegrees=" + toDegrees(phi) + ", inRadians=" + phi + '}'
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

        private fun ensureRadians(value: Double, repr: Unit): Double {
            return when (repr) {
                Unit.RAD -> value
                Unit.DEG -> Math.toRadians(value)
            }
        }

    }

}
