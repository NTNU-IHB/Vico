package info.laht.acco.physics

import info.laht.acco.core.Component
import info.laht.acco.math.Vector3

enum class MotionControl {
    DYNAMIC,
    KINEMATIC,
    STATIC,
}

class RigidBodyComponent @JvmOverloads constructor(
    var mass: Double = 1.0,
    var motionControl: MotionControl = MotionControl.DYNAMIC
) : Component {

    var linearVelocity = Vector3()
    var angularVelocity = Vector3()

}
