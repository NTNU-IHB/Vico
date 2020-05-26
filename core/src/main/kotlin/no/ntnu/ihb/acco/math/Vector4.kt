package no.ntnu.ihb.acco.math

import kotlin.math.abs
import kotlin.math.sqrt

data class Vector4(
    @JvmField
    var x: Double = 0.0,
    @JvmField
    var y: Double = 0.0,
    @JvmField
    var z: Double = 0.0,
    @JvmField
    var w: Double = 0.0
) : Cloneable {

    operator fun set(index: Int, value: Double): Vector4 {
        when (index) {
            0 -> x = value
            1 -> y = value
            2 -> z = value
            3 -> w = value
            else -> throw IndexOutOfBoundsException()
        }
        return this
    }

    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException()
        }
    }

    fun set(x: Double, y: Double, z: Double, w: Double): Vector4 {
        this.x = x
        this.y = y
        this.z = z
        this.w = w

        return this
    }

    fun add(v: Vector4): Vector4 {

        this.x += v.x
        this.y += v.y
        this.z += v.z
        this.w += v.w

        return this

    }

    fun addScalar(s: Double): Vector4 {

        this.x += s
        this.y += s
        this.z += s
        this.w += s

        return this

    }

    fun addVectors(a: Vector4, b: Vector4): Vector4 {

        this.x = a.x + b.x
        this.y = a.y + b.y
        this.z = a.z + b.z
        this.w = a.w + b.w

        return this

    }

    fun addScaledVector(v: Vector4, s: Double): Vector4 {

        this.x += v.x * s
        this.y += v.y * s
        this.z += v.z * s
        this.w += v.w * s

        return this

    }

    fun sub(v: Vector4): Vector4 {

        this.x -= v.x
        this.y -= v.y
        this.z -= v.z
        this.w -= v.w

        return this

    }

    fun subScalar(s: Double): Vector4 {

        this.x -= s
        this.y -= s
        this.z -= s
        this.w -= s

        return this

    }

    fun subVectors(a: Vector4, b: Vector4): Vector4 {

        this.x = a.x - b.x
        this.y = a.y - b.y
        this.z = a.z - b.z
        this.w = a.w - b.w

        return this

    }

    fun multiplyScalar(scalar: Double): Vector4 {

        this.x *= scalar
        this.y *= scalar
        this.z *= scalar
        this.w *= scalar

        return this

    }

    fun applyMatrix4(m: Matrix4): Vector4 {

        val x = this.x
        val y = this.y
        val z = this.z
        val w = this.w
        val e = m.elements

        this.x = e[0] * x + e[4] * y + e[8] * z + e[12] * w
        this.y = e[1] * x + e[5] * y + e[9] * z + e[13] * w
        this.z = e[2] * x + e[6] * y + e[10] * z + e[14] * w
        this.w = e[3] * x + e[7] * y + e[11] * z + e[15] * w

        return this

    }

    fun divideScalar(scalar: Double): Vector4 {

        return this.multiplyScalar(1f / scalar)

    }

    fun floor(): Vector4 {

        this.x = kotlin.math.floor(this.x)
        this.y = kotlin.math.floor(this.y)
        this.z = kotlin.math.floor(this.z)
        this.w = kotlin.math.floor(this.w)

        return this

    }


    fun ceil(): Vector4 {

        this.x = kotlin.math.ceil(this.x)
        this.y = kotlin.math.ceil(this.y)
        this.z = kotlin.math.ceil(this.z)
        this.w = kotlin.math.ceil(this.w)

        return this

    }

    fun round(): Vector4 {

        this.x = kotlin.math.round(this.x)
        this.y = kotlin.math.round(this.y)
        this.z = kotlin.math.round(this.z)
        this.w = kotlin.math.round(this.w)

        return this

    }

    fun roundToZero(): Vector4 {

        this.x = if (this.x < 0) kotlin.math.ceil(this.x) else kotlin.math.floor(this.x)
        this.y = if (this.y < 0) kotlin.math.ceil(this.y) else kotlin.math.floor(this.y)
        this.z = if (this.z < 0) kotlin.math.ceil(this.z) else kotlin.math.floor(this.z)
        this.w = if (this.w < 0) kotlin.math.ceil(this.w) else kotlin.math.floor(this.w)

        return this

    }

    fun negate(): Vector4 {

        this.x = -this.x
        this.y = -this.y
        this.z = -this.z
        this.w = -this.w

        return this

    }

    fun dot(v: Vector4): Double {

        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w

    }

    fun lengthSq(): Double {

        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w

    }

    fun length(): Double {

        return sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w)

    }

    fun manhattanLength(): Double {

        return abs(this.x) + abs(this.y) + abs(this.z) + abs(this.w)

    }

    fun normalize(): Vector4 {

        val l = this.length()
        return this.divideScalar(if (l.isNaN()) 1.0 else l)

    }

    fun setLength(length: Double): Vector4 {

        return this.normalize().multiplyScalar(length)

    }

    fun lerp(v: Vector4, alpha: Double): Vector4 {

        this.x += (v.x - this.x) * alpha
        this.y += (v.y - this.y) * alpha
        this.z += (v.z - this.z) * alpha
        this.w += (v.w - this.w) * alpha

        return this

    }

    fun lerpVectors(v1: Vector4, v2: Vector4, alpha: Double): Vector4 {

        return this.subVectors(v2, v1).multiplyScalar(alpha).add(v1)

    }

    fun fromArray(array: DoubleArray, offset: Int = 0): Vector4 {

        this.x = array[offset]
        this.y = array[offset + 1]
        this.z = array[offset + 2]
        this.w = array[offset + 3]

        return this

    }

    /**
     * Returns an array [x, y, z], or copies x, y and z into the provided array.
     * @param array (optional) array to store the vector to. If Vector3 is not provided, a new array will be created.
     * @param offset (optional) optional offset into the array.
     * @return The created or provided array.
     */
    fun toArray(array: DoubleArray?, offset: Int): DoubleArray {

        @Suppress("NAME_SHADOWING")
        val array = array ?: DoubleArray(4)

        array[offset] = this.x
        array[offset + 1] = this.y
        array[offset + 2] = this.z
        array[offset + 3] = this.w

        return array
    }

    public override fun clone() = copy()

    fun copy(v: Vector4): Vector4 {
        return set(v.x, v.y, v.z, v.w)
    }

}

operator fun Vector4.plus(v: Vector4): Vector4 {
    return copy().add(v)
}

operator fun Vector4.plusAssign(v: Vector4) {
    add(v)
}

operator fun Vector4.minus(v: Vector4): Vector4 {
    return copy().sub(v)
}

operator fun Vector4.minusAssign(v: Vector4) {
    sub(v)
}
