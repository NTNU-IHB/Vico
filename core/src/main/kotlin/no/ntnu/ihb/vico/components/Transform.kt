package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.RealLambdaProperty
import no.ntnu.ihb.vico.math.Frame
import no.ntnu.ihb.vico.math.fromArray
import no.ntnu.ihb.vico.math.toArray
import org.joml.Vector3d

class Transform : Component() {

    val frame = Frame()

    init {
        val tmp = Vector3d()
        registerProperties(
            RealLambdaProperty("localPosition", 3,
                getter = { ref -> frame.getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> frame.setLocalTranslation(tmp.fromArray(values)) }
            ),
            RealLambdaProperty(
                "worldPosition", 3,
                getter = { ref -> frame.getTranslation(tmp).toArray(ref) },
                setter = { values -> frame.setTranslation(tmp.fromArray(values)) }
            )
        )
    }

    fun setParent(parent: Transform) {
        frame.setParent(parent.frame)
    }

}
