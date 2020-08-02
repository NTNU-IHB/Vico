package no.ntnu.ihb.vico.physics

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.RealLambdaProperty
import no.ntnu.ihb.vico.math.fromArray
import no.ntnu.ihb.vico.math.toArray
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
        registerProperties(listOf(
            RealLambdaProperty("linearVelocity", 3,
                getter = { ref -> linearVelocity.toArray(ref) },
                setter = { values -> linearVelocity.fromArray(values) }
            ),
            RealLambdaProperty("angularVelocity", 3,
                getter = { ref -> angularVelocity.toArray(ref) },
                setter = { values -> angularVelocity.fromArray(values) }
            )
        ))
    }

}
