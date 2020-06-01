package no.ntnu.ihb.acco.math

import org.joml.Matrix4dc
import org.joml.Vector3d

data class Line3 @JvmOverloads constructor(
    var start: Vector3d = Vector3d(),
    var end: Vector3d = Vector3d()
) {

    fun set(start: Vector3d, end: Vector3d): Line3 {
        this.start.set(start)
        this.end.set(end)

        return this
    }

    fun copy(): Line3 {
        return Line3().copy(this)
    }

    fun copy(line: Line3): Line3 {
        this.start.set(line.start)
        this.end.set(line.end)

        return this
    }

    fun getCenter(target: Vector3d): Vector3d {
        return this.start.add(this.end, target).mul(0.5)
    }

    fun delta(target: Vector3d): Vector3d {
        return this.end.sub(this.start, target)
    }

    fun distanceSq(): Double {
        return this.start.distanceSquared(this.end)
    }

    fun distance(): Double {
        return this.start.distance(this.end)
    }

    fun at(t: Double, target: Vector3d): Vector3d {
        return this.delta(target).mul(t).add(this.start)
    }

    fun closestPointToPointParameter(point: Vector3d, clampToLine: Boolean = false): Double {
        val startP = Vector3d()
        val startEnd = Vector3d()

        point.sub(this.start, startP)
        this.end.sub(this.start, startEnd)

        val startEnd2 = startEnd.dot(startEnd)
        val startEnd_startP = startEnd.dot(startP)

        var t = startEnd_startP / startEnd2

        if (clampToLine) {

            t = clamp(t, 0, 1)

        }

        return t
    }

    fun closestPointToPoint(
        point: Vector3d,
        clampToLine: Boolean,
        target: Vector3d
    ): Vector3d {
        val t = this.closestPointToPointParameter(point, clampToLine)
        return this.delta(target).mul(t).add(this.start)
    }

    fun applyMatrix4(matrix: Matrix4dc): Line3 {
        this.start.mulPosition(matrix)
        this.end.mulPosition(matrix)

        return this
    }

}
