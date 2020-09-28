package info.laht.krender.curves

import org.joml.Vector2d
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class EllipseCurve
/**
 * Constructs an Ellipse.
 * <br></br>
 * Note: When going clockwise it's best to set the start angle to (Math.PI *
 * 2) and then work towards lower numbers.
 *
 * @param aX The X center of the ellipse.
 * @param aY The Y center of the ellipse.
 * @param xRadius The radius of the ellipse in the x direction.
 * @param yRadius The radius of the ellipse in the y direction.
 * @param aStartAngle The start angle of the curve in radians starting from
 * the middle right side.
 * @param aEndAngle The end angle of the curve in radians starting from the
 * middle right side.
 * @param aClockWise Whether the ellipse is drawn clockwise.
 * @param aRotation The rotation angle of the ellipse in radians,
 * counterclockwise from the positive X axis.
 */
@JvmOverloads constructor(
    private val aX: Double,
    private val aY: Double,
    private val xRadius: Double,
    private val yRadius: Double,
    private val aStartAngle: Double,
    private val aEndAngle: Double,
    private val aClockWise: Boolean = false,
    private val aRotation: Double = 0.0
) : Curve2() {

    override fun getPoint(t: Double): Vector2d {
        val twoPhi = Math.PI * 2
        var deltaAngle = aEndAngle - aStartAngle
        val samePoints = abs(deltaAngle) < Double.MIN_VALUE
        while (deltaAngle < 0) {
            deltaAngle += twoPhi
        }
        while (deltaAngle > twoPhi) {
            deltaAngle -= twoPhi
        }
        if (deltaAngle < Double.MIN_VALUE) {
            deltaAngle = if (samePoints) {
                0.0
            } else {
                twoPhi
            }
        }
        if (aClockWise && !samePoints) {
            if (deltaAngle == twoPhi) {
                deltaAngle -= twoPhi
            } else {
                deltaAngle -= twoPhi
            }
        }
        val angle = aStartAngle + t * deltaAngle
        var x = aX + xRadius * cos(angle)
        var y = aY + yRadius * sin(angle)
        if (aRotation != 0.0) {
            val cos = cos(aRotation)
            val sin = sin(aRotation)
            val tx = x - aX
            val ty = y - aY

            // Rotate the point about the center of the ellipse.
            x = tx * cos - ty * sin + aX
            y = tx * sin + ty * cos + aY
        }
        return Vector2d(x, y)
    }

}
