package no.ntnu.ihb.acco.physics

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.RealLambdaVar
import no.ntnu.ihb.acco.math.fromArray
import no.ntnu.ihb.acco.math.toArray
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

    init {
        registerVariables(listOf(
            RealLambdaVar("linearVelocity", 3,
                getter = { ref -> linearVelocity.toArray(ref) },
                setter = { values -> linearVelocity.fromArray(values) }
            ),
            RealLambdaVar("angularVelocity", 3,
                getter = { ref -> angularVelocity.toArray(ref) },
                setter = { values -> angularVelocity.fromArray(values) }
            )
        ))
    }

}
