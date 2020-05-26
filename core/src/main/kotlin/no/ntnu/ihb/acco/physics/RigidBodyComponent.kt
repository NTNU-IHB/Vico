package no.ntnu.ihb.acco.physics

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.math.Vector3

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
