package no.ntnu.ihb.acco.physics

import no.ntnu.ihb.acco.core.Component
import org.joml.Vector3d

enum class MotionControl {
    DYNAMIC,
    KINEMATIC,
    STATIC,
}

class RigidBodyComponent @JvmOverloads constructor(
    var mass: Double = 1.0,
    var motionControl: MotionControl = MotionControl.DYNAMIC
) : Component() {

    var linearVelocity = Vector3d()
    var angularVelocity = Vector3d()

}
